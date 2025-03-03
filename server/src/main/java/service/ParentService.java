package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.Collection;
import java.util.UUID;
import java.util.Random;

public class ParentService {
    protected final UserDAO userDAO;
    protected final AuthDAO authDAO;
    protected final GameDAO gameDAO;
    public ParentService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() {
        userDAO.deleteAllData();
        authDAO.deleteAllData();
        gameDAO.deleteAllData();
    }

    public Collection<UserData> listUsers(){
        return userDAO.listData();
    }
    public Collection<AuthData> listAuths(){
        return authDAO.listData();
    }
    public Collection<GameData> listGames(){
        return gameDAO.listData();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData authData = authDAO.readData(authToken);
        if(authData == null){
            throw new Unauthorized("Error: unauthorized");
        }
        return authData;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public static Integer generateInt() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
