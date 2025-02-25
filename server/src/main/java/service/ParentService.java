package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

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

}
