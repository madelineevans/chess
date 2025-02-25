package dataaccess;

import chess.ChessGame;
import model.Data;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements DataAccess<GameData> {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void createData(GameData game) { //add gameData
        games.put(game.returnID(), game);
    }

    @Override
    public GameData readData(String stringID) throws DataAccessException{  //find a game by a STRING gameID
        int gameID = Integer.parseInt(stringID);
        GameData game = games.get(gameID);
        if(game == null){
            throw new DataAccessException("Game with gameID " + gameID + " is not found");
        }
        else{
            return game;
        }
    }

    @Override
    public void deleteAllData() {
        games.clear();
    }

    @Override
    public Collection<GameData> listData(){
        return games.values();
    }

//    @Override                           Add this when I figure out what form the input is
//    public Data updateGame(gameData game) {
//        return null;
//    }
}
