package dataaccess;

import exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlUserDAOTest {
    private SqlUserDAO db;

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

        assertThrows(DataAccessException.class, ()-> db.listData());
    }

    @Test
    void listData() throws DataAccessException {
        List<UserData> expected = new ArrayList<>();
        UserData bob = new UserData("bob", "b0brulz", "bob@gmail.com");
        UserData bobby = new UserData("bobby", "b0brulz2", "bobbyBOB");
        UserData jeff = new UserData("jeff", "je0ffr3", "jeff@gmail.com");
        expected.add(bob);
        expected.add(bobby);
        expected.add(jeff);

        try{
            db.createData(bob);
            db.createData(bobby);
            db.createData(jeff);
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
    void exists() {
        UserData bob = new UserData("bob", "b0brulz", "bob@gmail.com");
        assertDoesNotThrow(()->db.createData(bob));
        boolean b = assertDoesNotThrow(()-> db.exists("bob"));
        assertTrue(b);
    }

    @Test
    void existsBad() throws DataAccessException {
        assertFalse(db.exists("bob"));
    }

    public static void assertUserEqual(UserData expected, UserData actual) {
        assertNotNull(actual, "Retrieved user is null");
        assertEquals(expected.username(), actual.username());
        //assertEquals(expected.password(), actual.password());
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