package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlGameDAOTest {
    private SqlGameDAO db;

    @BeforeEach
    void setup() throws DataAccessException {
        db = new SqlGameDAO();
        db.deleteAllData();
        //db.configureDatabase(createStatements);
    }

    @Test
    void createData() {
        var game = new GameData(1234, "game1");
        assertDoesNotThrow(()-> db.createData(game));
    }

    @Test
    void createDataBad() {
        var game = new GameData(1234, "game1");
        assertDoesNotThrow(()-> db.createData(game));
        var game2 = new GameData(1234, "game2");
        assertThrows(DataAccessException.class, ()-> db.createData(game2));
    }

    @Test
    void readData() {
        var game = new GameData(1234, "game1");
        assertDoesNotThrow(()-> db.createData(game));
        var actualGame = assertDoesNotThrow(()->db.readData("1234"));
        assertGameEqual(game, actualGame);
    }

    @Test
    void readDataBad() {
        assertThrows(DataAccessException.class, ()->db.readData("1234"));
    }

    @Test
    void deleteAllData() {
        assertDoesNotThrow(()-> db.createData(new GameData(1234, "game1")));
        assertDoesNotThrow(()-> db.createData(new GameData(1254, "game2")));
        assertDoesNotThrow(()-> db.deleteAllData());

        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void listData() throws DataAccessException {
        List<GameData> expected = new ArrayList<>();
        GameData g1 = new GameData(1234, "game1");
        GameData g2 = new GameData(1254, "game2");
        GameData g3 = new GameData(666, "game3");
        expected.add(g1);
        expected.add(g2);
        expected.add(g3);

        try{
            db.createData(g1);
            db.createData(g2);
            db.createData(g3);
        } catch(DataAccessException e){
            throw new DataAccessException("Error: " + e.toString());
        }

        var actual = db.listData();
        assertGameCollectionEqual(expected, actual);
    }

    @Test
    void listBadData() {
        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void updateGame() {

    }

    @Test
    void updateBadGame() {

    }

    public static void assertGameEqual(GameData expected, GameData actual) {
        assertNotNull(actual, "Retrieved game is null");
        assertEquals(expected.gameID(), actual.gameID());
        assertEquals(expected.whiteUsername(), actual.whiteUsername());
        assertEquals(expected.blackUsername(), actual.blackUsername());
        assertEquals(expected.gameName(), actual.gameName());
        assertEquals(expected.game(), actual.game());
    }

    public static void assertGameCollectionEqual(Collection<GameData> expected, Collection<GameData> actual) {
        GameData[] actualList = actual.toArray(new GameData[]{});
        GameData[] expectedList = expected.toArray(new GameData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertGameEqual(expectedList[i], actualList[i]);
        }
    }
//    private final String[] createStatements = {
//            """
//            DROP TABLE IF EXISTS game;
//            """,
//            """
//            CREATE TABLE IF NOT EXISTS  game (
//                `id` int NOT NULL AUTO_INCREMENT,
//                `gameID` int NOT NULL,
//                `whiteUsername` varchar(255) NOT NULL,
//                `blackUsername` varchar(255) NOT NULL,
//                `gameName` varchar(255) NOT NULL,
//                `json` TEXT DEFAULT NULL,
//                PRIMARY KEY (`id`),
//                INDEX(gameID)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """
//    };
}