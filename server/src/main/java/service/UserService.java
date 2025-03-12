package service;
import dataaccess.exceptions.AlreadyTaken;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.Unauthorized;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.results.LoginResult;
import service.requests.RegisterRequest;
import service.results.LogoutResult;
import service.results.RegisterResult;
import service.requests.LogoutRequest;

import java.util.Objects;

public class UserService extends ParentService {
    public UserService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();

        if(userDAO.exists(username)){
            throw new AlreadyTaken("Error: already taken");
        }
        else{
            if(registerRequest.password() == null || registerRequest.email() == null){
                throw new BadRequest("Error: bad request");
            }
            UserData userData = new UserData(username, registerRequest.password(), registerRequest.email());
            AuthData authData = new AuthData(generateToken(), username);

            userDAO.createData(userData);  //createUser(userData)
            authDAO.createData(authData);  //createAuth(authData)

            return new RegisterResult(username, authData.authToken());
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        String username = loginRequest.username();
        UserData user = userDAO.readData(username);
        userDAO.verifyUser(user);
        if(user == null || !Objects.equals(user.password(), loginRequest.password())){
            throw new Unauthorized("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), username);
        authDAO.createData(authData);

        return new LoginResult(username, authData.authToken());
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        getAuth(logoutRequest.authToken());
        authDAO.deleteData(logoutRequest.authToken());
        return new LogoutResult();
    }

}

