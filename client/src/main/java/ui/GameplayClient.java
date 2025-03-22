package ui;
import exceptions.DataAccessException;
import java.util.Arrays;

public class GameplayClient extends Client{
    private final ServerFacade server;
    private final String serverUrl;

    public GameplayClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
    }

    public void PrintGame(){

    }
}
