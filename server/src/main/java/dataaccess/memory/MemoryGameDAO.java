package dataaccess.memory;
import dataaccess.GameDAO;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void createData(GameData game) { //add gameData
        games.put(game.returnID(), game);
    }

    @Override
    public GameData readData(String stringID) throws DataAccessException {  //find a game by a STRING gameID
        int gameID = Integer.parseInt(stringID);
        GameData game = games.get(gameID);
        if(game == null){
            throw new BadRequest("Error: bad request");
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

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }
}
