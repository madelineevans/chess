package ui;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class PreloginClient extends Client{
    private final ServerFacade server;
    //private boolean registered = false;
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
                case "login" -> Login(params);
                case "register" -> register(params);
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String Login(String... params) throws DataAccessException {
        if(!(params.length == 2)) {
            return "Error: please enter login <username> <password>";
        }

//        if(!checkRegistered()){
//            return "Please register before logging in.";
//        }

        LoginRequest req = new LoginRequest(params[0], params[1]);

        try{
            LoginResult res = server.login(req);
            authToken = res.authToken();

//        } catch (ResponseException e){
//            if(e.getMessage().startsWith("Cannot invoke")){
//                //registered = true;
//                return "That username is already registered, please login";
//            }
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        //add a bit to send to postLogin
//        PostloginClient post = new PostloginClient(serverUrl, authToken);
//        post.eval("help");

        loggedin = true;
        return String.format("Logged in as %s.\n", params[0]);
    }

    public String register(String... params) throws DataAccessException {
        if(params.length<3) {
            return "Error: please enter register <username> <password> <email>";
        }

        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);

        try{
            RegisterResult res = server.register(req);
//        } catch (ResponseException e){
//            if(e.getMessage().startsWith("Cannot invoke")){
//                //registered = true;
//                return "That username is already registered, please login";
//            }
        } catch (DataAccessException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error: " + e.getMessage());
        }
        //registered = true;
        return String.format("Registered as %s.", params[0]);

    }

    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create and account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }

//    public boolean checkRegistered(){
//        return registered;
//    }

}
