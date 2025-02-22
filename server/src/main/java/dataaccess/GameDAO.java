package dataaccess;

import chess.ChessGame;
import model.Data;
import model.GameData;

import java.util.HashMap;

public class GameDAO implements DataAccess {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void createData(GameData g) {
        int ID = g.returnID();
        games.put(ID, g);
    }

    @Override
    public Data readData() {
        return null;
    }

//    @Override                           Add this when I figure out what form the input is
//    public Data updateData() {
//        return null;
//    }

    @Override
    public void deleteData() {
        games.clear();
    }
}
