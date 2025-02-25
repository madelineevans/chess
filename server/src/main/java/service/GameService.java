package service;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.requests.ListRequest;
import service.results.CreateResult;
import service.results.JoinResult;
import service.results.ListResult;


public class GameService extends ParentService{
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }
    public ListResult listGames(ListRequest) throws DataAccessException{}
    public CreateResult createGame(CreateRequest) throws DataAccessException{}
    public JoinResult joinGame(JoinRequest) throws DataAccessException{}
    }
