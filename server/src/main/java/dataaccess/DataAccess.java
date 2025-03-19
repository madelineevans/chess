package dataaccess;
import exceptions.DataAccessException;
import model.Data;
import java.sql.SQLException;
import java.util.Collection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public interface DataAccess< T extends Data> {
    void createData(T data) throws DataAccessException;
    T readData(String id) throws DataAccessException;
    void deleteAllData() throws DataAccessException;
    Collection<T> listData() throws DataAccessException;

    default int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }
            }

            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    String.format("unable to update database: %s, %s", statement, e.getMessage())
            );
        }
    }

    default void configureDatabase(String[] createStatements) throws DataAccessException{
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
