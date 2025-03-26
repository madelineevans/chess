package ui;

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

    public void renderBoard(String color) {
        if (this instanceof GameplayClient) {
            if ("white".equals(color)) {
                ((GameplayClient)this).printBoard();
            } else {
                ((GameplayClient)this).printBlackBoard();
            }
        }
    }
}
