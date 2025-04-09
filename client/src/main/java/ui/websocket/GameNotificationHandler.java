package ui.websocket;

import chess.ChessGame;
import ui.clients.GameplayClient;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class GameNotificationHandler implements NotificationHandler{
    private final GameplayClient client;

    public GameNotificationHandler(GameplayClient client){
        this.client = client;
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println("in notification handler");
        if (notification instanceof LoadGameMessage loadM) {
            ChessGame updated = loadM.getGame().game();
            client.redraw(updated);
        }
        else{
            System.out.println("Notification: " + notification.toString());
        }
    }
}
