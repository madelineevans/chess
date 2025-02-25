package service;
import dataaccess.DataAccess;
import service.requests.LoginRequest;
import service.results.LoginResult;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import service.requests.LogoutRequest;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    public RegisterResult register(RegisterRequest registerRequest) {
        dataAccess.readData(registerRequest.username());
        dataAccess.createData(registerRequest.username());
        dataAccess.createData(generateToken());
    }


    public LoginResult login(LoginRequest loginRequest) {}
    public void logout(LogoutRequest logoutRequest) {}

    public void clear(){
        dataAccess.deleteData();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

