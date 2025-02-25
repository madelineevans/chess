package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.Data;
import model.GameData;
import model.UserData;

public interface DataAccess< T extends Data> {
    void createData(T data);
    T readData(String id) throws DataAccessException;
    void deleteAllData();
}
