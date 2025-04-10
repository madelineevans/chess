package ui.websocket;
import chess.ChessGame;
import ui.clients.Client;
import ui.clients.GameplayClient;
import websocket.messages.*;

public class NotificationHandler {
    private final Client client;

    public NotificationHandler(Client client) {
        this.client = client;
    }

    public void notify(ServerMessage notification) {
        System.out.println("in notification handler");
        if (notification instanceof LoadGameMessage loadM) {
            ChessGame updated = loadM.getGame().game();
            client.renderBoard("white", updated);
        }
        else if (notification instanceof ErrorNotification errorM) {
            //System.out.println("Got to errorNotificationHandler");
            System.out.println("Error from server: " + errorM.getErrorMessage());
        }
        else{
            System.out.println("Notification: " + notification.toString());
        }
    }
}