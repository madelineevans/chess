package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.requests.ListRequest;
import service.results.CreateResult;
import service.results.JoinResult;
import service.results.ListResult;

import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static service.ParentService.generateToken;

class GameServiceTest {
    static UserDAO userDAO = new UserDAO();
    static AuthDAO authDAO = new AuthDAO();
    static GameDAO gameDAO = new GameDAO();
    static GameService gService = new GameService(userDAO, authDAO, gameDAO);

    static String authToken;

    @BeforeEach
    void clear(){
        gService.clear();
    }

    @Test
    void listGames() throws DataAccessException {
        GameData game1 = new GameData(1234, "game1");
        gameDAO.createData(game1);

        authToken = generateToken();
        AuthData ad = new AuthData(authToken, "user1");
        authDAO.createData(ad);

        ListRequest lr = new ListRequest(authToken);
        ListResult lR = gService.listGames(lr);

        Collection<GameData> games = gService.testListGames();
        assertEquals(games, lR.games());
    }

    @Test
    void listGamesBad() throws DataAccessException {
        GameData game1 = new GameData(1234, "game1");
        gameDAO.createData(game1);

        authToken = generateToken();
        ListRequest lr = new ListRequest(authToken);


        assertThrows(DataAccessException.class, ()-> {
            ListResult lR = gService.listGames(lr);
        });
    }

    @Test
    void createGame() throws DataAccessException {
        authToken = generateToken();
        AuthData ad = new AuthData(authToken, "user1");
        authDAO.createData(ad);
        CreateRequest cr = new CreateRequest(authToken, "game1");
        CreateResult cR = gService.createGame(cr);

        Collection<GameData> games = gService.testListGames();
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
    void joinGame() throws DataAccessException {
        authToken = generateToken();
        AuthData ad = new AuthData(authToken, "user1");
        authDAO.createData(ad);
        GameData game = new GameData(1234, "game1");
        gameDAO.createData(game);


        JoinRequest jr = new JoinRequest(authToken, ChessGame.TeamColor.BLACK, 1234);
        JoinResult jR = gService.joinGame(jr);

        Collection<GameData> games = gService.testListGames();
        assertTrue(games.stream().anyMatch(game1 -> (game1.gameID() == jr.gameID() && Objects.equals(game1.blackUsername(), "user1"))));
    }

    @Test
    void joinGameBad() throws DataAccessException {
        authToken = generateToken();
        AuthData ad = new AuthData(authToken, "user1");
        authDAO.createData(ad);
        GameData game = new GameData(1234, null, "user2", "game1", new ChessGame());
        gameDAO.createData(game);


        JoinRequest jr = new JoinRequest(authToken, ChessGame.TeamColor.BLACK, 1234);
        assertThrows(DataAccessException.class, ()-> {
            JoinResult jR = gService.joinGame(jr);
        });
    }
}