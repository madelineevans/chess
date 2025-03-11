package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class SqlUserDAOTest {
    private DataAccess<UserData> db;

    @BeforeEach
    void setup() throws DataAccessException {
        db = new SqlUserDAO();
        db.deleteAllData();
    }

    @Test
    void createData() throws DataAccessException {
        var user = new UserData("bob", "b0brulz", "bobbymyboy@gmail.com");
        assertDoesNotThrow(()-> db.createData(user));
    }

    @Test
    void createDataBad() throws DataAccessException {
        var user = new UserData("bob", "b0brulz", "bobbymyboy@gmail.com");
        assertDoesNotThrow(()-> db.createData(user));
        var user2 = new UserData("bob", "newBob", "bobbymyBETTERboy@gmail.com");
        assertThrows(DataAccessException.class, ()-> db.createData(user2));
    }

    @Test
    void readData() {
        var user = new UserData("bob", "b0brulz", "bobbymyboy@gmail.com");
        assertDoesNotThrow(()-> db.createData(user));
        var actualUser = assertDoesNotThrow(()->db.readData("bob"));
        assertUserEqual(user, actualUser);
    }

    @Test
    void readDataBad() {    //not found or exists
        assertThrows(DataAccessException.class, ()-> db.readData("geofferie"));
    }

    @Test
    void deleteAllData(){
        assertDoesNotThrow(()-> db.createData(new UserData("bob", "b0brulz", "bob@gmail.com")));
        assertDoesNotThrow(()-> db.createData(new UserData("bobby", "b0brulz2", "bobbyBOB")));
        assertDoesNotThrow(()-> db.deleteAllData());

        var actual = assertDoesNotThrow(()-> db.listData());
        assertEquals(0, actual.size());
    }

    @Test
    void listData() {
    }

    @Test
    void listDataBad() {
    }

    @Test
    void exists() {
    }

    @Test
    void existsBad() {
    }

    public static void assertUserEqual(UserData expected, UserData actual) {
        assertNotNull(actual, "Retrieved user is null");
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.password(), actual.password());
        assertEquals(expected.email(), actual.email());
    }

    public static void assertUserCollectionEqual(Collection<UserData> expected, Collection<UserData> actual) {
        UserData[] actualList = actual.toArray(new UserData[]{});
        UserData[] expectedList = expected.toArray(new UserData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertUserEqual(expectedList[i], actualList[i]);
        }
    }

}