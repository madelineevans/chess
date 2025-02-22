package dataaccess;

import chess.ChessGame;
import model.Data;
import model.GameData;

import java.util.HashMap;

public class GameDAO implements DataAccess {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Data createData(int ID, String w, String b, String name, ChessGame g) {
        GameData game = new GameData(ID, w, b, name, g);
        games.put(ID, game);
        return game;
    }

    @Override
    public Data readData() {
        return null;
    }

    @Override
    public Data updateData() {
        return null;
    }

    @Override
    public void deleteData() {

    }
}
