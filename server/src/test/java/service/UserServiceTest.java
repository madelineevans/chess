package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.LogoutResult;
import service.results.RegisterResult;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();
    UserService uService = new UserService(userDAO, authDAO, gameDAO);

    @BeforeEach
    void clear(){
        uService.clear();
    }

    @Test
    void register() throws DataAccessException{
        RegisterRequest rr = new RegisterRequest("user1", "pass1", "email1");
        RegisterResult rR = uService.register(rr);

        Collection<UserData> users = uService.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(new UserData("user1", "pass1", "email1")));
    }
    @Test
    void registerBad(){ //already user in there
        userDAO.createData(new UserData("user1", "pass1", "email1"));
        RegisterRequest rr = new RegisterRequest("user1", "pass1", "email1");

        assertThrows(DataAccessException.class, ()-> {
            RegisterResult rR = uService.register(rr);
        });
    }

    @Test
    void login() throws DataAccessException {  //make sure it's in auth
        userDAO.createData(new UserData("user1", "pass1", "email1"));
        LoginRequest lr = new LoginRequest("user1", "pass1");
        LoginResult lR = uService.login(lr);


        Collection<AuthData> auths = uService.listAuths();
        assertEquals(1, auths.size());
        assertTrue(auths.contains(new AuthData(lR.authToken(), "user1")));
    }

    @Test
    void loginBad() throws DataAccessException{   //user isn't in system
        LoginRequest lr = new LoginRequest("user1", "pass1");

        assertThrows(DataAccessException.class, ()-> {
            LoginResult lR = uService.login(lr);
        });
    }

    @Test
    void logout() {
        userDAO.createData(new UserData("user1", "pass1", "email1"));
        LogoutRequest lr = new LogoutRequest("user1", "pass1");
        LogoutResult lR = uService.logout(lr);


        Collection<AuthData> auths = uService.listAuths();
        assertEquals(1, auths.size());
        assertTrue(auths.contains(new AuthData(lR.authToken(), "user1")));
    }

    @Test
    void logoutBad() {
    }
}