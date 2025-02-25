package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

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
}
