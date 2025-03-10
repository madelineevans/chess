package dataaccess;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.UserData;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class SqlUserDAO implements DataAccessSQL<UserData> {

    public SqlUserDAO() throws DataAccessException{
        configureDatabase(createStatements);
    }

    @Override
    public void createData(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(user);
        var id = executeUpdate(statement, user.username(), user.password(), user.email(), json);
    }

    @Override
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
    public void deleteAllData() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    @Override
    public Collection<UserData> listData() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public boolean exists(String username) throws DataAccessException{
        var statement = "SELECT 1 FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (var rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(String.format("Unable to check exists: %s", e.getMessage()));
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException{
        var username = rs.getString("username");
        var json = rs.getString("json");
        var user = new Gson().fromJson(json, UserData.class);
        return user.setUsername(username);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `password` varchar(255) NOT NULL,
                `email` varchar(255) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
