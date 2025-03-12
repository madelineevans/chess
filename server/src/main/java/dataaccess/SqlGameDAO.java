package dataaccess;
import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTaken;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.GameData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlGameDAO implements GameDAO {

    public SqlGameDAO() throws DataAccessException{
        configureDatabase(createStatements);
    }

    @Override
    public void createData(GameData game) throws DataAccessException {
        var checkStatement = "SELECT COUNT(*) FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(checkStatement)){
                ps.setInt(1, game.gameID());
                try (var rs = ps.executeQuery()){
                    if(rs.next() && rs.getInt(1)>0){
                        throw new AlreadyTaken("Error: gameID already taken");
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error checking gameID preexistance" + e.getMessage());
        }
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?, ?)";
        var json = new Gson().toJson(game);
        var id = executeUpdate(statement, game.gameID(), game.whiteUsername(),
                game.blackUsername(), game.gameName(), json);
    }

    @Override
    public GameData readData(String stringID) throws DataAccessException {
        int gameID = Integer.parseInt(stringID);
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return readGame(rs);
                    }
                    else{
                        throw new BadRequest("Error: bad request");
                    }
                }
            }
        } catch (Exception e){
            throw new BadRequest("Error: bad request");
        }
    }

    @Override
    public void deleteAllData() throws DataAccessException {
        var statement = "TRUNCATE game";
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

    @Override
    public void updateGame(GameData game) throws DataAccessException{
        var statement = "UPDATE game SET gameID = ?, whiteUsername = ?, blackUsername = ?, gameName = ?, json = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                //var json = updateGameJson(game);
                var json = new Gson().toJson(game);
                ps.setInt(1, game.gameID());
                ps.setString(2, game.whiteUsername());
                ps.setString(3, game.blackUsername());
                ps.setString(4, game.gameName());
                ps.setString(5, json);
                ps.setInt(6, game.gameID());
                var id = ps.executeUpdate();

                if(id==0){
                    throw new BadRequest("Error: no game found with this gameID");
                }
            }
        } catch (Exception e){
            //System.err.println("Unable to update game: " + e.getMessage());
            throw new DataAccessException("Unable to update game: " + e.getMessage());
        }
    }

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
                `whiteUsername` varchar(255) NULL,
                `blackUsername` varchar(255) NULL,
                `gameName` varchar(255) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
