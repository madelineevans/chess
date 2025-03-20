package ui;

import chess.ChessGame;
import exceptions.DataAccessException;
import requests.JoinRequest;
import requests.LoginRequest;
import results.JoinResult;
import results.LoginResult;

import java.util.Arrays;
import java.util.Objects;

public class PostloginClient extends Client{
    private final ServerFacade server;
    private final String serverUrl;

    public PostloginClient(String serverUrl, DataAccessException exception) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame();
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout(params);
                case "quit" -> quit();
                case "help" -> help();
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String joinGame(String... params) throws DataAccessException {
        if(params.length<2) {
            return "Error: please enter join <ID> [WHITE|BLACK]";
        }

        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        if(Objects.equals(params[1], "BLACK")){
            color = ChessGame.TeamColor.BLACK;
        }

        JoinRequest req = new JoinRequest(authToken, color, params[0]);

        try{
            JoinResult res = server.joinGame(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        //add a bit to send to gamePlay
        GameplayClient game = new GameplayClient();
        game.eval();

        return String.format("Joined game %s.", params[0]);
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
