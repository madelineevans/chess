package service;
import chess.ChessGame;
import dataaccess.*;
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
import java.util.Objects;


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
        GameData game = new GameData(gameID, "none", "none", createRequest.gameName(), new ChessGame());
        gameDAO.createData(game);
        return new CreateResult(gameID);
    }
    public JoinResult joinGame(JoinRequest joinRequest) throws DataAccessException{
        AuthData authData = getAuth(joinRequest.authToken());
        GameData game = gameDAO.readData(String.valueOf(joinRequest.gameID()));
        if(game == null){
            throw  new BadRequest("Error: bad request");
        }
        if(Objects.equals(joinRequest.playerColor(), "WHITE")) {
            if (!Objects.equals(game.whiteU(), "none")) {
                throw new AlreadyTaken("Error: already taken");
            }
            else{
                gameDAO.updateGame(new GameData(game.gameID(), authData.username(), game.blackU(), game.gameName(), game.game()));
            }
        }
        else if(Objects.equals(joinRequest.playerColor(), "BLACK")){
            if(!Objects.equals(game.blackU(), "none")) {
                throw new AlreadyTaken("Error: already taken");
            }
            else{
                gameDAO.updateGame(new GameData(game.gameID(), game.whiteU(), authData.username(), game.gameName(), game.game()));
            }
        }
        else{
            throw new BadRequest("Error: bad request");
        }
        return new JoinResult();
    }
}
