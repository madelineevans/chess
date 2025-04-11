package ui.clients;
import chess.*;
import exceptions.DataAccessException;
import model.GameData;
import requests.ListRequest;
import results.ListResult;
import ui.DrawBoard;
import ui.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameplayClient extends Client {
    private final ServerFacade server;
    //PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private int gameNum;
    //private String color;
    //private ChessGame game;

    public GameplayClient(String serverUrl, String authToken, String color, int gameNum, NotificationHandler notificationHandler, WebSocketFacade ws) {
        super(serverUrl);
        //super(game);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        //this.color = color;
        this.gameNum = gameNum;
        this.notificationHandler = notificationHandler;
        this.ws = ws;
    }

    @Override
    public void updateGame(ChessGame newGame) {
        super.updateGame(newGame);
    }

    public int getGameID() {
        //int gameNum = Integer.parseInt(params[0]);
        int gameID=99999;
        try{
            ListResult lRes = server.listGames(new ListRequest(authToken));
            int i = 1;
            for(GameData game : lRes.games()){
                if(i==gameNum){
                    gameID = game.gameID();
                }
                i++;
            }
        }catch (Error | DataAccessException e){
            System.out.println("Error converting gameNum to gameID");
        }
        return gameID;
    }

    public ServerFacade getServer() {
        return server;
    }

    public NotificationHandler getNotificationHandler() {
        return this.notificationHandler;
    }

    public void printBoard(){
        System.out.println("\n");
        //ChessGame game = getCurrGame();
        DrawBoard.drawChessBoard(out, getCurrGame());
    }

    public void printBlackBoard(){
        System.out.println("\n");
        //ChessGame game = getCurrGame();
        DrawBoard.drawChessBoardUpsidedown(out, getCurrGame());
    }

    public String redraw(){
        String color = getClientColor();
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
        System.out.println("gameplay mM GameID: " + getGameID());

        try{
            ws.makeMove(authToken, getGameID(), move);
        }catch(Exception e){
            throw new DataAccessException("Error: " + e.getMessage());
        }
        //get updated Game
        //redraw(game);
        return String.format("Making move %s", params[0]);
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

        System.out.println("gameplay resign GameID: " + getGameID());
        try{
            ws.resign(authToken, getGameID());
        }catch(Exception e){
            throw new DataAccessException("Error: " + e.getMessage());
        }
        //redraw();
        return "Resigned from game";
    }

    public String highlightMoves(String... params) {

        if(params.length != 1){
            return "Error: please enter move ____ ex: c1c4";
        }

        ChessPosition position = parsePosition(params[0]);

        // Assume the player is trying to highlight moves for their pieces
        ChessGame game = getCurrGame();
        ChessBoard board = game.getBoard();
        Collection<ChessMove> legalMoves = game.validMoves(position);


        // Call DrawBoard with legal moves
        DrawBoard.drawChessBoardWithHighlights(out, game, legalMoves);
        return "Legal moves highlighted!";
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
                case "highlight" -> highlightMoves(params);
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
                highlight __- highlights legal moves you can make. ex. a2
                """;
    }
}
