package ui.clients;

import chess.ChessGame;
import ui.DrawBoard;
import ui.ServerFacade;
import ui.websocket.GameState;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public abstract class Client {
    protected String serverUrl;
    protected String authToken;
    private final ServerFacade server;
    protected PrintStream out;
    protected GameState gameState;
    //protected String color;

    public Client(String serverUrl, GameState gameState) {
        this.serverUrl = serverUrl;
        this.gameState = gameState;
        server = new ServerFacade(serverUrl);
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public String getClientColor() {
        return gameState.getClientColor();  // Assuming `color` is a field that holds "white" or "black"
    }

    public void setClientColor(String newColor) {
        gameState.setClientColor(newColor);  // Assuming `color` is a field that holds "white" or "black"
    }

    public abstract String eval(String command);

    public abstract String help();

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public ServerFacade getServer() {
        return server;
    }

    public void updateGame(ChessGame newGame) {
        gameState.setCurrentGame(newGame);
    }
    public ChessGame getCurrGame(){
        return gameState.getCurrentGame();
    }

    public void renderBoard(String color) {
        //ChessGame game = new ChessGame();
        if ("white".equals(color)) {
            printBoard();
        } else {
            printBlackBoard();
        }
    }

    public void printBoard(){
        System.out.println("\n");
        //find the current game somehow and call it game
        DrawBoard.drawChessBoard(out, gameState.getCurrentGame());
    }

    public void printBlackBoard(){
        System.out.println("\n");
        //find the current game somehow and call it game
        DrawBoard.drawChessBoardUpsidedown(out, gameState.getCurrentGame());
    }
}
