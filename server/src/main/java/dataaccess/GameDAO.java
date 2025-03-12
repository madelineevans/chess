package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.GameData;

public interface GameDAO extends DataAccess<GameData>{
    void updateGame(GameData updatedGame) throws DataAccessException;
}
