package ui;
import chess.ChessGame;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import requests.*;
import results.*;
import java.util.Arrays;
import java.util.Objects;

public class PostloginClient extends Client{
    private final ServerFacade server;

    public PostloginClient(String serverUrl, String authToken) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit_to_prelogin";
                case "help" -> help();
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String... params) throws DataAccessException {
        if(params.length != 1){
            return "Error: please enter create <NAME>";
        }
        CreateRequest req = new CreateRequest(authToken, params[0]);
        CreateResult res = null;
        try{
            res = server.createGames(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return "Created game " + params[0] + "Game ID: " + Integer.toString(res.gameID());
    }

    public String listGames() throws DataAccessException {
        //add something to check if any games
        ListRequest req = new ListRequest(authToken);
        try{
            ListResult res = server.listGames(req);
            return res.toString();
        } catch (BadRequest e){
            return "Error: no games available. Please add a game to list them";
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    public String joinGame(String... params) throws DataAccessException {
        if(params.length!=2) {
            return "Error: please enter join <ID> [WHITE|BLACK]";
        }

        //add something to check if that came exists

        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        if(Objects.equals(params[1], "BLACK")){
            color = ChessGame.TeamColor.BLACK;
        }

        JoinRequest req = new JoinRequest(authToken, color, Integer.parseInt(params[0]));

        try{
            JoinResult res = server.joinGame(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        return String.format("Joined game %s.", params[0]);
    }

    public String observeGame(String ... params) throws DataAccessException{
        if(params.length!=1) {
            return "Error: please enter observe <ID>";
        }

        // check there's a game to observe

        return String.format("Observing game %s.", params[0]);
    }

    public String logout() throws DataAccessException {
//        if (loggedIn) {
//            throw new ResponseException(400, "You must login");
//        }
        LogoutRequest req = new LogoutRequest(authToken);

        try{
            LogoutResult res = server.logout(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        //add a bit to exit postLogin

        //state = State.SIGNEDOUT;
        return "You are logged out";
    }

    public String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit
                help
                """;
    }
}
