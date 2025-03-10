package dataaccess;

public class SqlAuthDAO {

    private final String[] createStatements = {
            """            
            CREATE TABLE IF NOT EXISTS  auth (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(255) NOT NULL,
                `authToken` varchar(255) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(authToken),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
