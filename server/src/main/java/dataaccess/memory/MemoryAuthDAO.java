package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    final private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public void createData(AuthData auth){ //add authData
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData readData(String authToken) throws DataAccessException {  //find authData by authToken
        AuthData auth = auths.get(authToken);
        if(auth == null){
            throw new Unauthorized("Error: unauthorized");
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

    @Override
    public void deleteData(String authToken) throws DataAccessException {
        if(auths.get(authToken)==null){
            throw new Unauthorized("Error: authToken doesn't exist");
        }
        auths.remove(authToken);
    }

}
