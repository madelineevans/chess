package ui.clients;
import java.util.Arrays;
import exceptions.DataAccessException;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import ui.ServerFacade;

public class PreloginClient extends Client {
    private final ServerFacade server;
    private boolean loggedin = false;

    public PreloginClient(String serverUrl) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws DataAccessException {
        if(!(params.length == 2)) {
            return "Error: please enter login <username> <password>";
        }

        LoginRequest req = new LoginRequest(params[0], params[1]);

        try{
            LoginResult res = server.login(req);
            authToken = res.authToken();

        } catch (DataAccessException e) {
            if(e.getMessage().startsWith("Cannot invoke")) {
                return "That username/password is not recognized. Please register or use correct password";
            }
            throw new DataAccessException("Error: " + e.getMessage());
        }

        loggedin = true;
        return String.format("Logged in as %s.\n", params[0]);
    }

    public String register(String... params) throws DataAccessException {
        if(params.length!=3) {
            return "Error: please enter register <username> <password> <email>";
        }

        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);

        try{
            RegisterResult res = server.register(req);
            authToken = res.authToken();
        } catch (DataAccessException e) {
            if(e.getMessage().startsWith("Cannot invoke")) {
                return "That username is already registered, please login";
            }
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return String.format("Registered as %s. \n", params[0]);

    }

    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create and account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }

}
