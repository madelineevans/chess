package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements DataAccess<AuthData>{
    final private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public void createData(AuthData auth){ //add authData
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData readData(String authToken) throws DataAccessException{  //find authData by authToken
        AuthData auth = auths.get(authToken);
        if(auth == null){
            throw new DataAccessException("Auth with authToken " + authToken + " is not found");
        }
        else{
            return auth;
        }
    }

    @Override
    public void deleteAllData() {
        auths.clear();
    }

    @Override
    public Collection<AuthData> listData(){
        return auths.values();
    }
}
