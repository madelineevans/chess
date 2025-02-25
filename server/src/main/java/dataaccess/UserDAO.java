package dataaccess;
import model.UserData;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class UserDAO implements DataAccess<UserData> {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void createData(UserData user){   //add userdata
        users.put(user.username(), user);
    }

    @Override
    public UserData readData(String username) throws DataAccessException{ //find data by username
        UserData user = users.get(username);
        if(user == null){
            throw new DataAccessException("User with username " + username + " is not found");
        }
        else{
            return user;
        }
    }

    @Override
    public void deleteAllData(){
        users.clear();
    }

    @Override
    public Collection<UserData> listData(){
        return users.values();
    }
}
