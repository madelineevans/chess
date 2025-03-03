package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteU, String blackU, String gameName, ChessGame game) implements Data{

    public GameData(int gameID, String gameName){
        this(gameID, null, null, gameName, new ChessGame());
    }

    public int returnID(){
        return this.gameID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameData gameData = (GameData) obj;
        return (gameID == gameData.gameID) &&
                whiteU.equals(gameData.whiteU) &&
                blackU.equals(gameData.blackU) &&
                gameName.equals(gameData.gameName) &&
                game.equals(gameData.game);
    }
}
