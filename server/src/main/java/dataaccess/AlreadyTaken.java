package dataaccess;

public class AlreadyTaken extends DataAccessException{   //400
    public AlreadyTaken(String message){super(message);}
}
