package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.UserData;

public interface UserDAO extends DataAccess<UserData>{
    boolean exists(String username) throws DataAccessException;

    void verifyUser(UserData user, String password) throws DataAccessException;
}
