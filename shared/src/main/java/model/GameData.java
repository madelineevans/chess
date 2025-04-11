package model;
import chess.ChessGame;
import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) implements Data{
    //public boolean resigned = false;
    public GameData(int gameID, String gameName){
        this(gameID, null, null, gameName, new ChessGame());
    }
    public int returnID(){
        return this.gameID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        GameData gameData = (GameData) obj;
        return (gameID == gameData.gameID) &&
                Objects.equals(whiteUsername, gameData.whiteUsername) &&
                Objects.equals(blackUsername, gameData.blackUsername) &&
                Objects.equals(gameName, gameData.gameName) &&
                Objects.equals(game, gameData.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName); // Include other fields if necessary
    }

    public GameData setGameID(int gameID){
        return new GameData(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
    }

    public String getColor(String username){
        if(Objects.equals(username, blackUsername)){
            return "black";
        }
        if(Objects.equals(username, whiteUsername)){
            return "white";
        }
        return null;
    }
}
