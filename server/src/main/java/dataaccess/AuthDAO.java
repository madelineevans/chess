package dataaccess;
import exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO extends DataAccess<AuthData>{
    void deleteData(String s) throws DataAccessException;
}