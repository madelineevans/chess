package dataaccess.memory;
import dataaccess.UserDAO;
import exceptions.DataAccessException;
import exceptions.Unauthorized;
import model.UserData;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void createData(UserData user){   //add userdata
        users.put(user.username(), user);
    }

    @Override
    public UserData readData(String username) throws DataAccessException { //find data by username
        UserData user = users.get(username);
        if(user==null){
            throw new Unauthorized("Error: unauthorized");
        }
        try{
            verifyUser(user, user.password());
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
    public void verifyUser(UserData user, String password) throws Unauthorized {
        if(user == null){
            throw new Unauthorized("Error: unauthorized");
        }
        if(!Objects.equals(user.password(), password)){
            throw new Unauthorized("Error: unauthorized");
        }
    }
}
