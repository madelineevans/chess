package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.Data;
import model.GameData;
import model.UserData;

public interface DataAccess {
    void createData();
    Data readData();
    void deleteData();

}
