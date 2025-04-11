package ui.clients;
import chess.*;
import exceptions.DataAccessException;
import model.GameData;
import requests.ListRequest;
import results.ListResult;
import ui.DrawBoard;
import ui.ServerFacade;
import ui.websocket.GameState;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import java.util.*;

public class GameplayClient extends Client {
    private final ServerFacade server;
    //PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private int gameNum;
    private String color;
    //private ChessGame game;

    public GameplayClient(String serverUrl, String authToken, String color, int gameNum,
                          NotificationHandler notificationHandler, WebSocketFacade ws, GameState gameState) {
        super(serverUrl, gameState);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.color = color;
        this.gameNum = gameNum;
        this.notificationHandler = notificationHandler;
        this.ws = ws;
        this.gameState = gameState;
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
            return "Error: please enter move ____ ex: c1c4 or a2a1Q";
        }
        ChessMove move = parseMove(params[0]);
        System.out.println("gameplay mM GameID: " + getGameID());

        try{
            ws.makeMove(authToken, getGameID(), move);
        }catch(Exception e){
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return String.format("Making move %s", params[0]);
    }

    public String leave() throws DataAccessException {
        int gameID = getGameID();
        System.out.println("Leaving game with ID: " + gameID);

        try {
            // Send a leave notification through the WebSocket
            ws.leave(authToken, gameID);

            // Return a message indicating successful transition
            return "quit_to_postlogin";
        } catch (Exception e) {
            throw new DataAccessException("Error leaving game: " + e.getMessage());
        }
    }

    public String resign() throws DataAccessException {
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

        ChessGame game = getCurrGame();
        Collection<ChessMove> legalMoves = game.validMoves(position);

        if(color== "white"){
            DrawBoard.drawChessBoardWithHighlights(out, game, legalMoves);
        }
        else{
            DrawBoard.drawBlackChessBoardWithHighlights(out, game, legalMoves);
        }

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
                case "leave" -> leave();
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
        if (moveStr.length() < 4 || moveStr.length() > 5) {
            throw new IllegalArgumentException("Invalid move string: " + moveStr);
        }

        ChessPosition start = parsePosition(moveStr.substring(0, 2));
        ChessPosition end = parsePosition(moveStr.substring(2, 4));

        ChessPiece.PieceType promotion = null;
        if (moveStr.length() == 5) {
            char promoChar = Character.toLowerCase(moveStr.charAt(4));
            switch (promoChar) {
                case 'q' -> promotion = ChessPiece.PieceType.QUEEN;
                case 'r' -> promotion = ChessPiece.PieceType.ROOK;
                case 'b' -> promotion = ChessPiece.PieceType.BISHOP;
                case 'n' -> promotion = ChessPiece.PieceType.KNIGHT;
                default -> throw new IllegalArgumentException("Invalid promotion piece: " + promoChar);
            }
        }

        return new ChessMove(start, end, promotion);
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
                move ____ - moves pieces ex. a2b3 or for promoting a pawn a2a1n
                resign - forfeit the game
                highlight __- highlights legal moves you can make. ex. a2
                """;
    }
}
