package ui.clients;

import chess.ChessGame;

public abstract class Client {
    protected String serverUrl;
    protected String authToken;

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public abstract String eval(String command);

    public abstract String help();

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void renderBoard(String color, ChessGame game) {
        //ChessGame game = new ChessGame();
        if (this instanceof GameplayClient) {
            if ("white".equals(color)) {
                ((GameplayClient)this).printBoard(game);
            } else {
                ((GameplayClient)this).printBlackBoard(game);
            }
        }
    }
}
