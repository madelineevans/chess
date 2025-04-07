package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.AlreadyTaken;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import results.CreateResult;
import results.JoinResult;
import results.ListResult;
import java.util.ArrayList;
import java.util.List;


public class GameService extends ParentService{
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public ListResult listGames(ListRequest listRequest) throws DataAccessException {
        AuthData authData = getAuth(listRequest.authToken());
        List<GameData> games = new ArrayList<>(gameDAO.listData());
        return new ListResult(games);
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException{
        AuthData authData = getAuth(createRequest.authToken());
        int gameID = generateInt();
        GameData game = new GameData(gameID, createRequest.gameName());
        gameDAO.createData(game);
        return new CreateResult(gameID);
    }

    public JoinResult joinGame(JoinRequest joinRequest) throws DataAccessException{
        AuthData authData = getAuth(joinRequest.authToken());
        GameData game = gameDAO.readData(String.valueOf(joinRequest.gameID()));

        if(game == null){
            throw  new BadRequest("Error: bad request");
        }

        GameData updatedGame;
        if(joinRequest.playerColor() == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTaken("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
        }
        else if(joinRequest.playerColor() == ChessGame.TeamColor.BLACK){
            if(game.blackUsername() != null) {
                throw new AlreadyTaken("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
        }
        else{
            throw new BadRequest("Error: bad request");
        }

        gameDAO.updateGame(updatedGame);
        return new JoinResult();
    }

    public GameData getGame(int ID) throws DataAccessException {
        return gameDAO.readData(Integer.toString(ID));
    }
}
