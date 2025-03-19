package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.AlreadyTaken;
import exceptions.BadRequest;
import exceptions.DataAccessException;
import exceptions.Unauthorized;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import results.LoginResult;
import requests.RegisterRequest;
import results.LogoutResult;
import results.RegisterResult;
import requests.LogoutRequest;

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
        String password = loginRequest.password();
        //UserData user = new UserData(username, password, loginRequest.email())
        UserData user = userDAO.readData(username);
        userDAO.verifyUser(user, password);
        if(user == null){// || !Objects.equals(user.password(), loginRequest.password())){
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

