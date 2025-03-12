package dataaccess;
import dataaccess.exceptions.AlreadyTaken;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import model.AuthData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlAuthDAO implements DataAccessSQL<AuthData> {

    public SqlAuthDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public void createData(AuthData auth) throws DataAccessException {
        var checkStatement = "SELECT COUNT(*) FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(checkStatement)){
                ps.setString(1, auth.authToken());
                try (var rs = ps.executeQuery()){
                    if(rs.next() && rs.getInt(1)>0){
                        throw new AlreadyTaken("Error: authToken already taken");
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error checking authToken preexistance" + e.getMessage());
        }
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        var id = executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData readData(String authToken) throws DataAccessException { //find data by username
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return readAuth(rs);
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

    @Override
    public void deleteAllData() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    @Override
    public Collection<AuthData> listData() throws DataAccessException {
        var result = new ArrayList<AuthData>();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth";
            try(var ps = conn.prepareStatement(statement)) {
                try(var rs = ps.executeQuery()) {
                    if(!rs.isBeforeFirst()) {
                        throw new BadRequest("Error: list is empty");
                    }
                    while(rs.next()) {
                        result.add(readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteData(String authToken) throws DataAccessException{
        var statement = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                var id = ps.executeUpdate();

                if(id==0){
                    throw new BadRequest("Error: no auth found with this authToken");
                }
            }
        } catch (Exception e){
            throw new DataAccessException("Unable to delete Auth: " + e.getMessage());
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException{
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    private final String[] createStatements = {
            """            
            CREATE TABLE IF NOT EXISTS  auth (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `authToken` varchar(255) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
