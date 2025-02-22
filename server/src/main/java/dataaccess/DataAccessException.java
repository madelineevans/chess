package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
}

//public class DataNotFound extends DataAccessException{
//    public DataNotFound(String message);
//}