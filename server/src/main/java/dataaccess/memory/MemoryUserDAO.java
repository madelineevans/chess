package dataaccess.memory;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.UserData;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void createData(UserData user){   //add userdata
        users.put(user.username(), user);
    }

    @Override
    public UserData readData(String username) throws DataAccessException { //find data by username
        UserData user = users.get(username);
        try{
            verifyUser(user);
        }catch(DataAccessException e){
            throw new Unauthorized("Error: unauthorized");
        }
        return users.get(username);
    }

    @Override
    public void deleteAllData(){
        users.clear();
    }

    @Override
    public Collection<UserData> listData(){
        return users.values();
    }

    @Override
    public boolean exists(String username) {
        return users.containsKey(username);
    }

    @Override
    public void verifyUser(UserData user) throws Unauthorized {
        if(user == null){
            throw new Unauthorized("Error: unauthorized");
        }
    }
}
