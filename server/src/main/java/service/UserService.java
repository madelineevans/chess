package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.results.LoginResult;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import service.requests.LogoutRequest;
import java.util.UUID;

public class UserService extends ParentService {
    public UserService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();

        if(userDAO.readData(username) != null){
            throw new AlreadyTaken("Error: already taken");
        }
        else{
            UserData userData = new UserData(username, registerRequest.password(), registerRequest.email());
            AuthData authData = new AuthData(generateToken(), username);

            userDAO.createData(userData);  //createUser(userData)
            authDAO.createData(authData);  //createAuth(authData)

            return new RegisterResult(username, authData.authToken());
        }
    }

    //public LoginResult login(LoginRequest loginRequest) throws DataAccessException{}
    //public void logout(LogoutRequest logoutRequest) throws DataAccessException{}

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}

