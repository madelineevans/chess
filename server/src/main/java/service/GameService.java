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

import java.util.ArrayList;
import java.util.List;


public class GameService extends ParentService{
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public ListResult listGames(ListRequest listRequest) throws DataAccessException{
        AuthData authData = getAuth(listRequest.authToken());
        List<GameData> games = new ArrayList<>(gameDAO.listData());
        return new ListResult(games);
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException{
        AuthData authData = getAuth(createRequest.authToken());
        int gameID = generateInt();
        GameData game = new GameData(gameID, "white", "black", createRequest.gameName(), new ChessGame());
        gameDAO.createData(game);
        return new CreateResult(gameID);
    }
    public JoinResult joinGame(JoinRequest joinRequest) throws DataAccessException{
        //AuthData authData = getAuth(joinRequest.authToken());
        GameData game = gameDAO.readData(String.valueOf(joinRequest.gameID()));
        gameDAO.updateGame();

    }
}
