package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.DataAccessException;
import exceptions.Unauthorized;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.ArrayList;
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

    public void clear() throws DataAccessException {
        userDAO.deleteAllData();
        authDAO.deleteAllData();
        gameDAO.deleteAllData();
    }

    public Collection<UserData> listUsers() throws DataAccessException {
        return userDAO.listData();
    }
    public Collection<AuthData> listAuths() throws DataAccessException {
        return authDAO.listData();
    }

    public Collection<GameData> testListGames() throws DataAccessException {
        return new ArrayList<>(gameDAO.listData());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData authData = authDAO.readData(authToken);
        if(authData == null){
            throw new Unauthorized("Error: unauthorized");
        }
        return authData;
    }

    public String getUsername(String auth) throws DataAccessException {
        AuthData authData = getAuth(auth);
        return authData.username();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static Integer generateInt() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
