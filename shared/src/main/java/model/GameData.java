package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteU, String blackU, String gameNight, ChessGame game) implements Data{
//    GameData updateGame(){
//        return new GameData(this.gameID, this.
//    }
}
