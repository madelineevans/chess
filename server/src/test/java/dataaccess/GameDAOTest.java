package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        gameDAO = new GameDAO();
    }

    @Test
    void testCreateAndReadGame() throws DataAccessException {
        // Arrange: Create a game
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Cool Chess Game", new ChessGame());

        // Act: Add game data and retrieve it
        gameDAO.createData(game);
        GameData retrievedGame = gameDAO.readData("1");

        // Assert: Check if stored game data is correct
        assertNotNull(retrievedGame);
        assertEquals(1, retrievedGame.returnID());
        assertEquals("whitePlayer", retrievedGame.whiteU());
        assertEquals("blackPlayer", retrievedGame.blackU());
        assertEquals("Cool Chess Game", retrievedGame.gameName());
    }

    @Test
    void testReadNonExistentGameThrowsException() {
        // Act & Assert: Should throw DataAccessException when gameID is not found
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.readData("999");
        });

        assertTrue(exception.getMessage().contains("Game with gameID 999 is not found"));
    }

    @Test
    void testReadDataWithInvalidStringThrowsException() {
        // Act & Assert: Should throw NumberFormatException when string is not a valid integer
        assertThrows(NumberFormatException.class, () -> {
            gameDAO.readData("invalidID");
        });
    }

    @Test
    void testDeleteAllData() throws DataAccessException {
        // Arrange: Add a game
        GameData game = new GameData(2, "playerA", "playerB", "Tournament Game", new ChessGame());
        gameDAO.createData(game);

        // Act: Clear all game data
        gameDAO.deleteAllData();

        // Assert: Ensure data is gone
        assertThrows(DataAccessException.class, () -> gameDAO.readData("2"));
    }
}