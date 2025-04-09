package ui.clients;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import ui.DrawBoard;
import ui.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class GameplayClient extends Client {
    private final ServerFacade server;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private int gameID;
    private String color;

    public GameplayClient(String serverUrl, String authToken, String color, int gameID, NotificationHandler notificationHandler) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.color = color;
        this.gameID = gameID;
        this.notificationHandler = notificationHandler;
    }

    public void printBoard(){
        System.out.println("\n");
        DrawBoard.drawChessBoard(out);
    }

    public void printBlackBoard(){
        System.out.println("\n");
        DrawBoard.drawChessBoardUpsidedown(out);
    }

    public String redraw(){
        if(Objects.equals(color, "black")){
            printBlackBoard();
        }
        else if (Objects.equals(color, "white")){
            printBoard();
        }
        return "Current Board";
    }

    public String makeMove(String... params) throws DataAccessException {
        if(params.length != 1){
            return "Error: please enter move ____ ex: c1c4";
        }
        ChessMove move = parseMove(params[0]);

        ws = new WebSocketFacade(serverUrl, notificationHandler);
        try{
            ws.makeMove(authToken, gameID, move);
        }catch(Exception e){
            throw new DataAccessException("Error: " + e.getMessage());
        }

        redraw();
        return String.format("Made move %s", params[0]);
    }

    public String resign() throws DataAccessException {
        //ask the user if they really want to resign
        //if yes, resign them
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if (!input.equals("yes")) {
            return "Resignation cancelled.";
        }

        ws = new WebSocketFacade(serverUrl, notificationHandler);
        try{
            ws.resign(authToken, gameID);
        }catch(Exception e){
            throw new DataAccessException("Error: " + e.getMessage());
        }
        redraw();
        return "Resigned from game";
    }

    public void highlightMoves(){

    }

    @Override
    public String eval(String command) {
        var tokens = command.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try{
            return switch (cmd) {
                case "help" -> help();
                case "redraw" -> redraw();
                case "leave" -> "quit_to_postlogin";
                case "move" -> makeMove(params);
                case "resign" -> resign();
                //case "highlight" -> highlightMoves();

                default -> help();
            };
        }catch(Exception e){
            return e.getMessage();
        }
    }

    public ChessMove parseMove(String moveStr) {
        if (moveStr.length() < 4) {
            throw new IllegalArgumentException("Invalid move string: " + moveStr);
        }

        ChessPosition start = parsePosition(moveStr.substring(0, 2));
        ChessPosition end = parsePosition(moveStr.substring(2, 4));

        return new ChessMove(start, end, null);  // Assuming no promotion
    }

    private ChessPosition parsePosition(String posStr) {
        char colChar = posStr.charAt(0);
        char rowChar = posStr.charAt(1);

        int col = colChar - 'a' + 1;
        int row = rowChar - '1' + 1;

        return new ChessPosition(row, col);
    }

    @Override
    public String help() {
        return """
                help
                redraw - redraws the chess board
                leave - removes you from the game
                move ____ - moves piece in one space to another. ex. a2b3
                resign - forfeit the game
                highlight - highlights legal moves you can make                
                """;
    }
}
