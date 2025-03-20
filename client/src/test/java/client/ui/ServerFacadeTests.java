package ui;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;

class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + String.valueOf(port));
    }

    @BeforeEach
    public void cleanUp() throws DataAccessException {
        try{
            facade.clear();
        }catch(ResponseException e){
            throw new DataAccessException("Error with clearing: " + e);
        }

    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerBad() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void listGames() {
    }

    @Test
    void createGames() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void clear() {
    }
}