package model;

public record AuthData(String authToken, String username) implements Data{
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        AuthData authData = (AuthData) obj;
        return authToken.equals(authData.authToken) &&
                username.equals(authData.username);
    }

    public AuthData setAuthToken(String authToken){
        return new AuthData(authToken, this.username);
    }
}
