package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.Data;

import java.util.Collection;

public interface DataAccess< T extends Data> {
    void createData(T data);
    T readData(String id) throws DataAccessException;
    void deleteAllData();
    Collection<T> listData();
}
