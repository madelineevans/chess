package model;

public record UserData(String username, String password, String email) implements Data{
    public String returnUsername(){
        return this.username;
    }
}
