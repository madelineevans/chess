package server;
import com.google.gson.Gson;
import dataaccess.*;
import service.GameService;
import service.UserService;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.LogoutResult;
import service.results.RegisterResult;
import spark.*;
import java.util.Map;

public class Server {
    private final UserService us;
    private final GameService gs;

    public Server(){
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        this.us = new UserService(userDAO, authDAO, gameDAO);
        this.gs = new GameService(userDAO, authDAO, gameDAO);
    }

//    public Server(UserService userService, GameService gameService) {
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register); //registration
        Spark.post("/session",this::login); //login
        Spark.delete("/session",this::logout); //logout
//        Spark.get("/game",this::listGames); //list games
//        Spark.post("/game",this::createGames); //create game
//        Spark.put("/game",this::joinGames); //join game
        Spark.delete("/db",this::clear); //clear

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        try{
            //RegisterRequest rReq = Integer.parseInt(req.params("auth"));
            RegisterRequest rReq = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult rRes = us.register(rReq);
            res.status(200);
            return new Gson().toJson(rRes);
        } catch(DataAccessException e){
            if(e instanceof BadRequest){
                res.status(400); //bad request
            }
            else if(e instanceof AlreadyTaken){
                res.status(403); //already taken
            }
            else{
                res.status(500); //other error
            }
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
    private Object login(Request req, Response res) throws DataAccessException {
        try{
            LoginRequest lReq = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult lRes = us.login(lReq);
            res.status(200);
            return new Gson().toJson(lRes);

        } catch(DataAccessException e){
            if(e instanceof Unauthorized){
                res.status(401); //unauthorized
            }
            else{
                res.status(500); //other error
            }
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException{
        try{
            LogoutRequest lReq = new LogoutRequest(req.headers("Authorization"));
            LogoutResult lRes = us.logout(lReq);
            res.status(200);
            return new Gson().toJson(lRes);

        } catch(DataAccessException e){
            if(e instanceof Unauthorized){
                res.status(401); //unauthorized
            }
            else{
                res.status(500); //other error
            }
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object clear(Request req, Response res) throws DataAccessException{
        try{
            us.clear();
            gs.clear();
            res.status(200);
            return "";
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: " + e.toString()));
        }

    }
}
