package dataaccess;
import model.UserData;

public interface UserDAO extends DataAccess<UserData>{
    boolean exists(String username);

    void verifyUser(UserData user);
}
