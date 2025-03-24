package ui;
import exceptions.DataAccessException;
import java.util.Arrays;

public class GameplayClient extends Client{
    private final ServerFacade server;

    public GameplayClient(String serverUrl, String authToken) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public void PrintGame(){

    }

    @Override
    public String eval(String command) {
        return "nothing yet to eval";
    }

    @Override
    public String help() {
        return "not yet implemented";
    }
}
