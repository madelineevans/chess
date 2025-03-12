package dataaccess;
import model.GameData;

public interface GameDAO extends DataAccess<GameData>{
    void updateGame(GameData updatedGame);
}
