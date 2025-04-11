package ui.clients;

import chess.ChessGame;
import ui.DrawBoard;
import ui.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public abstract class Client {
    protected String serverUrl;
    protected String authToken;
    private final ServerFacade server;
    protected PrintStream out;

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
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

    public void renderBoard(String color, ChessGame game) {
        //ChessGame game = new ChessGame();
        if ("white".equals(color)) {
            printBoard(game);
        } else {
            printBlackBoard(game);
        }
    }

    public void printBoard(ChessGame game){
        System.out.println("\n");
        //find the current game somehow and call it game
        DrawBoard.drawChessBoard(out, game);
    }

    public void printBlackBoard(ChessGame game){
        System.out.println("\n");
        //find the current game somehow and call it game
        DrawBoard.drawChessBoardUpsidedown(out, game);
    }
}
