package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.RegisterRequest;
import service.results.CreateResult;
import service.results.RegisterResult;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static service.ParentService.generateToken;

class GameServiceTest {
    static UserDAO userDAO = new UserDAO();
    static AuthDAO authDAO = new AuthDAO();
    static GameDAO gameDAO = new GameDAO();
    //UserService uService = new UserService(userDAO, authDAO, gameDAO);
    static GameService gService = new GameService(userDAO, authDAO, gameDAO);

    static String authToken;

    @BeforeEach
    void clear(){
        gService.clear();
    }

//    @BeforeAll
//    static void setUp() throws DataAccessException {
//        authToken = generateToken();
//        AuthData ad = new AuthData(authToken, "user1");
//        authDAO.createData(ad);
////        RegisterRequest rr = new RegisterRequest("user1", "pass1", "email1");
////        RegisterResult rR = uService.register(rr);
//    }

    @Test
    void listGames() {
    }

    @Test
    void createGame() throws DataAccessException {
        authToken = generateToken();
        AuthData ad = new AuthData(authToken, "user1");
        authDAO.createData(ad);
        CreateRequest cr = new CreateRequest(authToken, "game1");
        CreateResult cR = gService.createGame(cr);

        Collection<GameData> games = gService.listGames();
        assertEquals(1, games.size());
        assertTrue(games.stream().anyMatch(game -> game.gameID() == cR.gameID()));
    }

    @Test
    void createGameBad() { //not signed in
        authToken = generateToken();
        CreateRequest cr = new CreateRequest(authToken, "game1");

        assertThrows(DataAccessException.class, ()-> {
            CreateResult cR = gService.createGame(cr);
        });
    }

    @Test
    void joinGame() {
    }
}