package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    private final GameData game;
    private final String color;

    public LoadGameMessage(ServerMessageType type, GameData game, String color) {
        super(type);
        this.game = game;
        this.color = color;
    }

    public GameData getGame() {
        return game;
    }

    public String getColor(){
        return color;
    }
}
