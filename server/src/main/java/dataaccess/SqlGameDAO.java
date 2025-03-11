package dataaccess;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO implements DataAccessSQL<GameData> {

    public SqlGameDAO() throws DataAccessException{
        configureDatabase(createStatements);
    }

    @Override
    public void createData(GameData game) throws DataAccessException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?)";
        var json = new Gson().toJson(game);
        var id = executeUpdate(statement, game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), json);
    }

    @Override
    public GameData readData(String stringID) throws DataAccessException {
        int gameID = Integer.parseInt(stringID);
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAllData() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    @Override
    public Collection<GameData> listData() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

//    public void updateGame(GameData game) {
//        try ( var conn = DatabaseManager.getConnection()) {
//            var statement = "UPDATE game SET ____ WHERE gameID = ?"
//            try (var ps = conn.prepareStatement(statement)){
//                ps.setInt(1, game.gameID());
//                try (var rs = ps.executeQuery()){
//                    if (rs.next()){
//                        return readGame(rs);
//                    }
//                }
//            }
//        } catch (Exception e){
//            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
//        }
//            games.put(game.gameID(), game);
//        }
//    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var json = rs.getString("json");
        var game = new Gson().fromJson(json, GameData.class);
        return game.setGameID(gameID);
    }

    private final String[] createStatements = {
            """            
            CREATE TABLE IF NOT EXISTS  game (
                `id` int NOT NULL AUTO_INCREMENT,
                `gameID` int NOT NULL,
                `whiteUsername` varchar(255) NOT NULL,
                `blackUsername` varchar(255) NOT NULL,
                `gameName` varchar(255) NOT NULL,
                `game` varchar(255) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(gameID),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
