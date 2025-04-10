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
        ServerMessage.ServerMessageType type = notification.getServerMessageType();
        switch (notification) {
            case LoadGameMessage msg when type == ServerMessage.ServerMessageType.LOAD_GAME -> {
                ChessGame updated = msg.getGame().game();
                client.renderBoard("white", updated);
            }
            case ErrorNotification msg when type == ServerMessage.ServerMessageType.ERROR ->{
                System.out.println("Error from server: " + msg.getErrorMessage());
                System.out.println("Try a different command");
                //System.out.println(client.help());
            }
            case NotificationMessage msg when type == ServerMessage.ServerMessageType.NOTIFICATION -> {
                System.out.println("Notification from server: " + msg.getMessage());
            }
            default -> System.out.println("Notification: " + notification.toString());
        }
    }
}