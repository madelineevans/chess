package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) implements Data{

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
                whiteUsername.equals(gameData.whiteUsername) &&
                blackUsername.equals(gameData.blackUsername) &&
                gameName.equals(gameData.gameName) &&
                game.equals(gameData.game);
    }
}
