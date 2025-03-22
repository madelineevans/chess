package ui;
import java.util.Arrays;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class PreloginClient extends Client{
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;

    public PreloginClient(String serverUrl) {
        System.out.println("Server URL: " + serverUrl);
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        //this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "register" -> register();
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String signIn(String... params) throws DataAccessException {
        if(params.length<2) {
            return "Error: please enter signin <username> <password>";
        }

        LoginRequest req = new LoginRequest(params[0], params[1]);

        try{
            LoginResult res = server.login(req);
            authToken = res.authToken();

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        //add a bit to send to postLogin
        //PostloginClient post = new PostloginClient();
        //post.eval();

        state = State.SIGNEDIN;
        return String.format("Logged in as %s.", params[0]);
    }

    public String register(String... params) throws DataAccessException {
        if(params.length<3) {
            return "Error: please enter register <username> <password> <email>";
        }

        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);

        try{
            RegisterResult res = server.register(req);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        state = State.SIGNEDIN;
        return String.format("Registered as %s.", params[0]);

    }

//    public String quit(){
//        state = State.SIGNEDOUT;
//        return String.format("You signed out");
//    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create and account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }

//    private void assertSignedIn() throws ResponseException {
//        if (state == State.SIGNEDOUT) {
//            throw new DataAccessException(400, "You must sign in");
//        }
//    }
}
