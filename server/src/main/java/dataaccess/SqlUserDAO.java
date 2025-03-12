package dataaccess;
import dataaccess.exceptions.AlreadyTaken;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUserDAO implements UserDAO {

    public SqlUserDAO() throws DataAccessException{
        configureDatabase(createStatements);
    }

    @Override
    public void createData(UserData user) throws DataAccessException {
        var checkStatement = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(checkStatement)){
                ps.setString(1, user.username());
                try (var rs = ps.executeQuery()){
                    if(rs.next() && rs.getInt(1)>0){
                        throw new AlreadyTaken("Error: username already taken");
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error checking username preexistance" + e.getMessage());
        }
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        var id = executeUpdate(statement, user.username(), createHashedPassword(user.password()), user.email());
    }

    @Override
    public UserData readData(String username) throws DataAccessException { //find data by username
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return readUser(rs);
                    }
                    else{
                        throw new Unauthorized("Error: unauthorized");
                    }
                }
            }
        } catch (Exception e){
            throw new Unauthorized("Error: unauthorized" + e.getMessage());
        }
    }

    @Override
    public void deleteAllData() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    @Override
    public Collection<UserData> listData() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user";
            try(var ps = conn.prepareStatement(statement)) {
                try(var rs = ps.executeQuery()) {
                    if(!rs.isBeforeFirst()) {
                        throw new BadRequest("Error: list is empty");
                    }
                    while(rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public boolean exists(String username) throws DataAccessException{
        var statement = "SELECT 1 FROM user WHERE username = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(var rs = ps.executeQuery()){
                    if(!rs.isBeforeFirst()) {
                        return false;
                    }
                    return rs.next();
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(String.format("Unable to check exists: %s", e.getMessage()));
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException{
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private String createHashedPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    @Override
    public void verifyUser(UserData user, String providedClearTextPassword) throws DataAccessException {
        String username = user.username();
        //String providedClearTextPassword = user.password();
        var hashedPassword = readHashedPasswordFromDatabase(username);
        if(!BCrypt.checkpw(providedClearTextPassword, hashedPassword)){
            throw new Unauthorized("Error: unauthorized");
        }
    }

    private String readHashedPasswordFromDatabase(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password");
                    }
                    else{
                        throw new Unauthorized("Error: unauthorized");
                    }
                }
            }
        } catch (Exception e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
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

    private void configureDatabase(String[] createStatements) throws DataAccessException{
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `password` varchar(255) NOT NULL,
                `email` varchar(255) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            //ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    };
}
