package exceptions;

public class Unauthorized extends DataAccessException {   //400
    public Unauthorized(String message){super(message);}
}
