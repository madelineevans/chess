package ui.websocket;

import chess.ChessGame;

public class GameState {
    private ChessGame currentGame;
    private String clientColor;
    private int gameID;

    // Getters and setters
    public ChessGame getCurrentGame() { return currentGame; }
    public void setCurrentGame(ChessGame game) { this.currentGame = game; }

    public String getClientColor() { return clientColor; }
    public void setClientColor(String color) { this.clientColor = color; }

    public int getGameID() { return gameID; }
    public void setGameID(int id) { this.gameID = id; }
}
