package model;

public record AuthData(String authToken, String username) implements Data{
    public String returnAuthToken(){
        return this.authToken;
    }
}
