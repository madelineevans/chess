package ui.clients;
import chess.ChessGame;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import model.GameData;
import requests.*;
import results.*;
import ui.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import java.util.Arrays;
import java.util.Objects;

public class PostloginClient extends Client {
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    public PostloginClient(String serverUrl, String authToken, NotificationHandler notificationHandler, WebSocketFacade ws) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.notificationHandler = notificationHandler;
        this.ws = ws;
    }

    public NotificationHandler getNotificationHandler() {
        return this.notificationHandler;
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
                case "quit" -> "quit";
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
        return "Created game " + params[0];
    }

    public String listGames() throws DataAccessException {
        //add something to check if any games
        ListRequest req = new ListRequest(authToken);
        try{
            ListResult res = server.listGames(req);
            StringBuilder games = new StringBuilder();
            games.append("Games: \n");
            int i = 1;
            for(GameData game : res.games()){
                games.append(Integer.toString(i)).append(": ");
                games.append("GameName=").append(game.gameName());
                games.append(", WhiteUsername=").append(game.whiteUsername());
                games.append(", BlackUsername=").append(game.blackUsername());
                games.append("\n");
                i++;
            }
            return games.toString();
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
        try {
            Integer.parseInt(params[0]); // Attempt to convert to int
        } catch (NumberFormatException e) {
            return "Error: please enter the number then color: join <ID> [WHITE|BLACK]";
        }
        if(!(params[1] instanceof String)){
            return "Error: please enter the number then color: join <ID> [WHITE|BLACK]";
        }

        ChessGame.TeamColor color = null;
        if(Objects.equals(params[1], "BLACK") || Objects.equals(params[1], "black")){
            color = ChessGame.TeamColor.BLACK;
        }
        else if(Objects.equals(params[1], "WHITE") || Objects.equals(params[1], "white")){
            color = ChessGame.TeamColor.WHITE;
        }
        else{
            return "Error: please only use white or black";
        }

        int gameNum = Integer.parseInt(params[0]);
        ListResult lRes = server.listGames(new ListRequest(authToken));

        int i = 1;
        int gameID=99999;
        for(GameData game : lRes.games()){
            if(i==gameNum){
                gameID = game.gameID();
            }
            i++;
        }
        System.out.println("GameID: " + gameID);
        JoinRequest req = new JoinRequest(authToken, color, gameID);
        //ws = new WebSocketFacade(serverUrl, notificationHandler);

        try{
            System.out.println("Player connecting to Game");
            JoinResult res = server.joinGame(req);
            ws.connect(authToken, gameID);
        } catch (DataAccessException e) {
            if(e.getMessage().startsWith("Cannot invoke")) {
                return "Not a game or that color is taken";
            }
            throw new DataAccessException("Error: " + e.getMessage());
        }

        return String.format("Joined game %s as %s.", params[0], params[1]);
    }

    public String observeGame(String ... params) throws DataAccessException{
        if(params.length!=1) {
            return "Error: please enter observe <ID>";
        }

        System.out.println("Observer connecting to Game");
        //ws = new WebSocketFacade(serverUrl, notificationHandler);
        try{
            int gameNum = Integer.parseInt(params[0]);
            ListResult lRes = server.listGames(new ListRequest(authToken));
            int i = 1;
            int gameID=99999;
            for(GameData game : lRes.games()){
                if(i==gameNum){
                    gameID = game.gameID();
                }
                i++;
            }
            ws.connect(authToken, gameID);
        } catch (Exception e){
            throw new Error("Error: "+ e.getMessage());
        }
        return String.format("Observing game %s from white side.", params[0]);
    }

    public String logout() throws DataAccessException {
        LogoutRequest req = new LogoutRequest(authToken);

        try{
            LogoutResult res = server.logout(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        return "quit_to_prelogin";
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
