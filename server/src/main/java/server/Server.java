package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.post("/user",this::register); //registration
//        Spark.post("/session",this::); //login
//        Spark.delete("/session",this::); //logout
//        Spark.get("/game",this::); //list games
//        Spark.post("/game",this::); //create game
//        Spark.put("/game",this::); //join game
//        Spark.delete("/db",this::); //clear

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    private Object register(Request req, Response res) throws ResponseExeption{
//        var
//    }
}
