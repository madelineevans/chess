package service;
import dataaccess.memory.MemoryAuthDAO;
import exceptions.DataAccessException;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    UserService uService = new UserService(memoryUserDAO, authDAO, memoryGameDAO);

    @BeforeEach
    void clear() throws DataAccessException {
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
        memoryUserDAO.createData(new UserData("user1", "pass1", "email1"));
        RegisterRequest rr = new RegisterRequest("user1", "pass1", "email1");

        assertThrows(DataAccessException.class, ()-> {
            RegisterResult rR = uService.register(rr);
        });
    }

    @Test
    void login() throws DataAccessException {  //make sure it's in auth
        memoryUserDAO.createData(new UserData("user1", "pass1", "email1"));
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
    void logout() throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        authDAO.createData(new AuthData(authToken, "user1"));
        LogoutRequest lr = new LogoutRequest(authToken);
        LogoutResult lR = uService.logout(lr);


        Collection<AuthData> auths = uService.listAuths();
        assertEquals(0, auths.size());
        assertFalse(auths.contains(new AuthData(authToken, "user1")));
    }

    @Test
    void logoutBad() throws DataAccessException{ //never was logged in
        String authToken = UUID.randomUUID().toString();
        LogoutRequest lr = new LogoutRequest(authToken);

        assertThrows(DataAccessException.class, ()-> {
            LogoutResult lR = uService.logout(lr);
        });
    }
}