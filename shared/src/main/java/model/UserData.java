package model;

public record UserData(String username, String password, String email) implements Data{
    public String returnUsername(){
        return this.username;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserData userData = (UserData) obj;
        return username.equals(userData.username) &&
                password.equals(userData.password) &&
                email.equals(userData.email);
    }
}



