package dataaccess;
import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class UserDAO implements DataAccess {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public UserData createData(String u, String p, String e){
        UserData user = new UserData(u, p, e);
        users.put(u, user);
        return user;
    }

    @Override
    public UserData readData(String u){
        return users.get(u);
    }

//    @Override
//    public UserData updateData(){
//
//    }

    @Override
    public void deleteData(){
        users.clear();
    }
}
