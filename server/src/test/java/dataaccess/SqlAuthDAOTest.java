package dataaccess;

import exceptions.BadRequest;
import exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthDAOTest {
    private SqlAuthDAO db;

    @BeforeEach
    void setup() throws DataAccessException {
        db = new SqlAuthDAO();
        db.deleteAllData();
    }

    @Test
    void createData() throws DataAccessException {
        var auth = new AuthData("authToken", "user1");
        assertDoesNotThrow(()-> db.createData(auth));
    }

    @Test
    void createDataBad() throws DataAccessException {
        var auth = new AuthData("authToken", "user1");
        assertDoesNotThrow(()-> db.createData(auth));
        var auth2 = new AuthData("authToken", "user2");
        assertThrows(DataAccessException.class, ()-> db.createData(auth2));
    }

    @Test
    void readData() {
        var auth = new AuthData("authToken", "user1");
        assertDoesNotThrow(()-> db.createData(auth));
        var actualAuth = assertDoesNotThrow(()->db.readData("authToken"));
        assertAuthEqual(auth, actualAuth);
    }

    @Test
    void readDataBad() {    //not found or exists
        assertThrows(DataAccessException.class, ()-> db.readData("authToken"));
    }

    @Test
    void deleteAllData(){
        assertDoesNotThrow(()-> db.createData(new AuthData("authToken", "user1")));
        assertDoesNotThrow(()-> db.createData(new AuthData("authTok2n", "user2")));
        assertDoesNotThrow(()-> db.deleteAllData());

        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void listData() throws DataAccessException {
        List<AuthData> expected = new ArrayList<>();
        AuthData a1 = new AuthData("authToken", "user1");
        AuthData a2 = new AuthData("authTok2n", "user2");
        expected.add(a1);
        expected.add(a2);

        try{
            db.createData(a1);
            db.createData(a2);
        } catch(DataAccessException e){
            throw new DataAccessException("Error: " + e.toString());
        }

        var actual = db.listData();
        assertUserCollectionEqual(expected, actual);
    }

    @Test
    void listDataBad() { //if nothing to list??
        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void deleteData() throws DataAccessException {
        var auth = new AuthData("authToken", "user1");
        assertDoesNotThrow(()-> db.createData(auth));
        assertDoesNotThrow(()-> db.deleteData("authToken"));
        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void deleteBadData(){
        assertThrows(DataAccessException.class, ()-> db.deleteData("authToken"));
    }

    public static void assertAuthEqual(AuthData expected, AuthData actual) {
        assertNotNull(actual, "Retrieved auth is null");
        assertEquals(expected.authToken(), actual.authToken());
        assertEquals(expected.username(), actual.username());
    }

    public static void assertUserCollectionEqual(Collection<AuthData> expected, Collection<AuthData> actual) {
        AuthData[] actualList = actual.toArray(new AuthData[]{});
        AuthData[] expectedList = expected.toArray(new AuthData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertAuthEqual(expectedList[i], actualList[i]);
        }
    }
}