package server;
import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.exceptions.AlreadyTaken;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import service.GameService;
import service.UserService;
import service.requests.*;
import service.results.*;
import spark.*;
import java.util.Map;

public class Server {
    private final UserService us;
    private final GameService gs;

    public Server(){
        UserDAO userDAO;
        AuthDAO authDAO;
        GameDAO gameDAO;
        try{
            userDAO = new SqlUserDAO();
            authDAO = new SqlAuthDAO();
            gameDAO = new SqlGameDAO();
        } catch(DataAccessException e){
            System.err.println("Error initializing DAOs: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize server DAOs", e);
        }

        this.us = new UserService(userDAO, authDAO, gameDAO);
        this.gs = new GameService(userDAO, authDAO, gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register); //registration
        Spark.post("/session",this::login); //login
        Spark.delete("/session",this::logout); //logout
        Spark.get("/game",this::listGames); //list games
        Spark.post("/game",this::createGame); //create game
        Spark.put("/game",this::joinGame); //join game
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

    private Object handleException(Response res, DataAccessException e){
        if(e instanceof Unauthorized){
            res.status(401);
        }
        else{
            res.status(500);
        }
        return new Gson().toJson(Map.of("message", e.getMessage()));
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
            return handleException(res, e);
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException{
        try{
            LogoutRequest lReq = new LogoutRequest(req.headers("Authorization"));
            LogoutResult lRes = us.logout(lReq);
            res.status(200);
            return new Gson().toJson(lRes);

        } catch(DataAccessException e){
            return handleException(res, e);
        }
    }

    private Object listGames(Request req, Response res) throws DataAccessException{
        try{
            ListRequest lReq = new ListRequest(req.headers("Authorization"));
            ListResult lRes = gs.listGames(lReq);

            res.status(200);
            return new Gson().toJson(lRes);

        } catch(DataAccessException e){
            return handleException(res, e);
        }
    }

    private Object createGame(Request req, Response res) throws DataAccessException{
        try{
            CreateRequest cRe= new Gson().fromJson(req.body(), CreateRequest.class);
            String name = cRe.gameName();
            CreateRequest cReq = new CreateRequest(req.headers("Authorization"), name);
            CreateResult cRes = gs.createGame(cReq);

            res.status(200);

            return new Gson().toJson(cRes);
        } catch(DataAccessException e){
            if(e instanceof BadRequest){
                res.status(400); //bad request
            }
            else if(e instanceof Unauthorized){
                res.status(401); //already taken
            }
            else{
                res.status(500); //other error
            }
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res) throws DataAccessException{
        try{
            JoinRequest jRe= new Gson().fromJson(req.body(), JoinRequest.class);
            JoinRequest jReq = new JoinRequest(req.headers("Authorization"), jRe.playerColor(), jRe.gameID());
            JoinResult jRes = gs.joinGame(jReq);

            res.status(200);

            return new Gson().toJson(jRes);
        } catch(DataAccessException e){
            if(e instanceof BadRequest){
                res.status(400); //bad request
            }
            else if(e instanceof Unauthorized){
                res.status(401); //
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
