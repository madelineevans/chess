package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
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
    //public ListResult listGames(ListRequest) throws DataAccessException{}
    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException{
        AuthData authData = getAuth(createRequest.authToken());
        int gameID = generateInt();
        GameData game = new GameData(gameID, "white", "black", createRequest.gameName(), new ChessGame());
        gameDAO.createData(game);
        return new CreateResult(gameID);
    }
    //public JoinResult joinGame(JoinRequest) throws DataAccessException{}
    }
