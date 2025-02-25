package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.Data;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess< T extends Data> {
    void createData(T data);
    T readData(String id) throws DataAccessException;
    void deleteAllData();
    Collection<T> listData();
}
