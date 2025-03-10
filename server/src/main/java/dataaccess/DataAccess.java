package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.Data;
import java.util.Collection;

public interface DataAccess< T extends Data> {
    void createData(T data) throws DataAccessException;
    T readData(String id) throws DataAccessException;
    void deleteAllData() throws DataAccessException;
    Collection<T> listData() throws DataAccessException;
}
