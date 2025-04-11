package ui.websocket;
import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.clients.Client;
import ui.clients.GameplayClient;
import websocket.messages.*;

public class NotificationHandler {
    //private final GameplayClient client;
    private final Client client;

    public NotificationHandler(Client client) {
        this.client = client;
    }
//    public NotificationHandler(GameplayClient client) {
//        this.client = client;
//    }

    public void notify(ServerMessage notification) {
        //System.out.println("in notification handler");
        ServerMessage.ServerMessageType type = notification.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME && notification instanceof LoadGameMessage msg) {
            //System.out.println("Loading Game");
            ChessGame updated = msg.getGame().game();
            String color = msg.getColor();
            client.renderBoard(color, updated);
        }
        else if (type == ServerMessage.ServerMessageType.ERROR && notification instanceof ErrorNotification msg) {
            System.out.println("Error from server: " + msg.getErrorMessage());
            System.out.println("Try a different command");
        }
        else if (type == ServerMessage.ServerMessageType.NOTIFICATION && notification instanceof NotificationMessage msg) {
            System.out.println("Notification from server: " + msg.getMessage());
        }
        else{
            System.out.println("Notification: " + notification.toString());
        }
    }
}