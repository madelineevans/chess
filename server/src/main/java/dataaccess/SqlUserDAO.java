package dataaccess;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.Data;
import model.UserData;
import java.util.Collection;
import java.sql.*;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUserDAO implements DataAccess<UserData> {

    public SqlUserDAO() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void createData(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        var json = new Gson().toJson(user);
        var id = executeUpdate(statement, user.username(), user.password(), user.email(), json);
    }

    public UserData readData(String username) throws DataAccessException { //find data by username
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAllData(){
        users.clear();
    }

    @Override
    public Collection<UserData> listData(){
        return users.values();
    }

    public boolean exists(String username) {
        return users.containsKey(username);
    }

    private UserData readUser(ResultSet rs) throws SQLException{
        var username = rs.getString("username");
        var json = rs.getString("json");
        var user = new Gson().fromJson(json, UserData.class);
        return user.setUsername(username);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i+1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    //else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `password` varchar(255) NOT NULL,
                `email` varchar(255) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci,
            
            CREATE TABLE IF NOT EXISTS  auth (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `authToken` varchar(255) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(authToken),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci,
            
            CREATE TABLE IF NOT EXISTS  game (
                `id` int NOT NULL AUTO_INCREMENT,
                `gameID` int NOT NULL,
                `whiteUsername` varchar(255) NOT NULL,
                `blackUsername` varchar(255) NOT NULL,
                `gameName` varchar(255) NOT NULL,
                `game` varchar(255) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(gameID),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
