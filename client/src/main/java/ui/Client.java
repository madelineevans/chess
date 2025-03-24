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
}
