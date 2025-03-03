package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.results.LoginResult;
import service.requests.RegisterRequest;
import service.results.LogoutResult;
import service.results.RegisterResult;
import service.requests.LogoutRequest;

import java.util.Objects;
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
        if(user == null || !Objects.equals(user.password(), loginRequest.password())){
            throw new Unauthorized("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), username);
        authDAO.createData(authData);

        return new LoginResult(username, authData.authToken());
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        getAuth(logoutRequest.authToken());
        //AuthData authToken = authDAO.readData(logoutRequest.authToken());
        if(logoutRequest == null || logoutRequest.authToken() == null){// || authToken==null){ // || authDAO.readData(logoutRequest.authToken())==null){
            throw new Unauthorized("Error: unauthorized");
        }

        authDAO.deleteData(logoutRequest.authToken());
        return new LogoutResult();
    }

}

