package dataaccess;
import chess.ChessGame;
import model.Data;
import model.UserData;

public interface DataAccess {
    Data createData();
    UserData createData(String u, String p, String e);  //user
    Data createData(int ID, String w, String b, String name, ChessGame g); //game

    Data readData();
    UserData readData(String u);    //user

    Data updateData();

    void deleteData();
}
