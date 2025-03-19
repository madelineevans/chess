package dataaccess;
import exceptions.DataAccessException;
import model.GameData;

public interface GameDAO extends DataAccess<GameData>{
    void updateGame(GameData updatedGame) throws DataAccessException;
}
