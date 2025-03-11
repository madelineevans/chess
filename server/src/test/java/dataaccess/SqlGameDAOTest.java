package dataaccess;

import dataaccess.exceptions.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlGameDAOTest {
    private SqlGameDAO db;

    @BeforeEach
    void setup() throws DataAccessException {
        db = new SqlGameDAO();
        db.deleteAllData();
    }

    @Test
    void createData() {
    }

    @Test
    void createDataBad() {
    }

    @Test
    void readData() {
    }

    @Test
    void readDataBad() {
    }

    @Test
    void deleteAllData() {
    }

    @Test
    void listData() {
    }

    @Test
    void listBadData() {
    }

    @Test
    void updateGame() {

    }

    @Test
    void updateBadGame() {

    }
}