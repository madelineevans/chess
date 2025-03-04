package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParentServiceTest {
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();
    ParentService parent = new ParentService(userDAO, authDAO, gameDAO);

    @Test
    void clear() {
        userDAO.createData(new UserData("exUsername", "exPassword", "exEmail"));
        authDAO.createData(new AuthData("1234", "exUsername"));
        gameDAO.createData(new GameData(1, "exWhite", "exBlack", "exGameName", new ChessGame()));

        try{
            assertNotNull(userDAO.readData("exUsername"));
            assertNotNull(authDAO.readData("1234"));
            assertNotNull(gameDAO.readData(String.valueOf(1)));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        parent.clear();

        assertThrows(DataAccessException.class, () -> userDAO.readData("exUsername"));
        assertThrows(DataAccessException.class, () -> authDAO.readData("1234"));
        assertThrows(DataAccessException.class, () -> gameDAO.readData("1"));
    }
}