package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.Data;

import java.util.Collection;
import java.sql.*;
import java.util.List;

public class SqlUserDAO implements DataAccess {

    public SqlUserDAO() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void createData(Data data) {

    }

    @Override
    public Data readData(String id) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllData() {

    }

    @Override
    public Collection listData() {
        return List.of();
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
