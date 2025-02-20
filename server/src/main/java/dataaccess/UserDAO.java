package dataaccess;
import model.UserData;

public class UserDAO implements DataAccess {

    @Override
    public UserData createData(String u, String p, String e) {
        UserData user = new UserData(u, p, e);

        //users.put
        return user;
    }
}
