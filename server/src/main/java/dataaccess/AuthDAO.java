package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements DataAccess{
    final private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public void createData(String authToken, String u){
        try{
            AuthData auth = new AuthData(authToken, u);
            auths.put(authToken, auth);
        }catch (DataAccessException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public AuthData readData(String authToken) throws DataAccessException{
        AuthData a = auths.get(authToken);
        if(a == null){
            throw new DataAccessException("Auth with authToken " + authToken + " is not found");
        }
        else{
            return a;
        }
    }

//    @Override
//    public Data updateData() {
//        return null;
//    }

    @Override
    public void deleteData() {
        auths.clear();
    }
}
