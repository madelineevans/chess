package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.RegisterRequest;
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
    void login() {
    }

    @Test
    void logout() {
    }
}