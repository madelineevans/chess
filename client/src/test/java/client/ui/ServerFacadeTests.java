package ui;
import chess.ChessGame;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import org.junit.jupiter.api.*;
import requests.*;
import results.CreateResult;
import results.JoinResult;
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
        assertThrows(DataAccessException.class, () -> facade.createGames(cReq));
    }

    @Test
    void listGames() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        CreateRequest cReq = new CreateRequest(res.authToken(), "game1");
        CreateResult cRes = facade.createGames(cReq);
        ListRequest lReq = new ListRequest(res.authToken());
        facade.listGames(lReq);
        assertTrue(res.authToken().length() > 10);
    }

    @Test
    void listBadGames() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        ListRequest lReq = new ListRequest(res.authToken());
        assertThrows(BadRequest.class, () -> facade.listGames(lReq));
        //assertTrue(res.authToken().length() > 10);
    }

    @Test
    void joinGame() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        CreateRequest cReq = new CreateRequest(res.authToken(), "game1");
        CreateResult cRes = facade.createGames(cReq);
        JoinRequest jReq = new JoinRequest(res.authToken(), ChessGame.TeamColor.WHITE, cRes.gameID());
        assertDoesNotThrow(facade.joinGame(jReq));
    }

    @Test
    void joinBadGame() {
        JoinRequest jReq = new JoinRequest("1234", ChessGame.TeamColor.WHITE, 1234);
        assertThrows(BadRequest.class, () -> facade.joinGame(jReq));
    }

    @Test
    void logout() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("player1", "password", "p1@email.com");
        var authData = facade.register(req);
        LoginResult res = facade.login(new LoginRequest("player1", "password"));
        LogoutResult Rres = facade.logout(new LogoutRequest(res.authToken()));
    }

    @Test
    void logoutBad() throws DataAccessException {
        LogoutResult Rres = facade.logout(new LogoutRequest("1234"));
    }

    @Test
    void clear() throws DataAccessException {
        assertDoesNotThrow(() -> facade.clear());
    }
}