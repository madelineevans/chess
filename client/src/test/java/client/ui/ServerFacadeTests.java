package ui;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import org.junit.jupiter.api.*;
import requests.*;
import results.CreateResult;
import results.LoginResult;
import results.LogoutResult;
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
    void registerBad() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        RegisterRequest req2 = new RegisterRequest("player1", "password", "p1@email.com");
        //facade.register(req2);
        assertThrows(DataAccessException.class, () -> facade.register(req2));
    }

    @Test
    void login() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        assertTrue(res.authToken().length() > 10);
    }

    @Test
    void loginBad() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> facade.login(new LoginRequest("player1", "password")));
    }

    @Test
    void createGames() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        CreateRequest cReq = new CreateRequest(res.authToken(), "game1");
        CreateResult cRes = facade.createGames(cReq);
        assertNotNull(cRes, "Game creation should return a result");
    }

    @Test
    void createBadGames() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        CreateRequest cReq = new CreateRequest(res.authToken(), "game1");
        CreateResult cRes = facade.createGames(cReq);
        CreateRequest c2Req = new CreateRequest(res.authToken(), "game2");
        assertThrows(DataAccessException.class, () -> facade.createGames(c2Req));
    }

    @Test
    void listGames() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        CreateRequest cReq = new CreateRequest(res.authToken(), "game1");
        CreateResult cRes = facade.createGames(cReq);
        ListRequest lReq = new ListRequest(res.authToken());
        facade.listGames();
        //assertTrue(res.authToken().length() > 10);
    }

    @Test
    void joinGame() {
    }

    @Test
    void logout() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        LogoutResult Rres = facade.logout(new LogoutRequest(res.authToken()));
    }

    @Test
    void clear() throws DataAccessException {
        assertDoesNotThrow(() -> facade.clear());
    }
}