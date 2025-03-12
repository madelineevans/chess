package service;

import chess.ChessGame;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParentServiceTest {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    ParentService parent = new ParentService(memoryUserDAO, authDAO, memoryGameDAO);

    @Test
    void clear() throws DataAccessException {
        memoryUserDAO.createData(new UserData("exUsername", "exPassword", "exEmail"));
        authDAO.createData(new AuthData("1234", "exUsername"));
        memoryGameDAO.createData(new GameData(1, "exWhite", "exBlack", "exGameName", new ChessGame()));

        try{
            assertNotNull(memoryUserDAO.readData("exUsername"));
            assertNotNull(authDAO.readData("1234"));
            assertNotNull(memoryGameDAO.readData(String.valueOf(1)));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        parent.clear();

        assertThrows(DataAccessException.class, () -> memoryUserDAO.readData("exUsername"));
        assertThrows(DataAccessException.class, () -> authDAO.readData("1234"));
        assertThrows(DataAccessException.class, () -> memoryGameDAO.readData("1"));
    }
}