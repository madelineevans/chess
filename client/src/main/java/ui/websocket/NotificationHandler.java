package ui.websocket;
import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.clients.Client;
import ui.clients.GameplayClient;
import websocket.messages.*;

import java.util.Objects;

public class NotificationHandler {
    private Client client;
    private GameState gameState;

    public NotificationHandler(Client client, GameState gameState) {
        this.client = client;
        this.gameState = gameState;
    }

    public void updateClientReference(Client newClient) {
        this.client = newClient;
    }

    public void notify(ServerMessage notification) {
        //System.out.println("in notification handler");
        ServerMessage.ServerMessageType type = notification.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME && notification instanceof LoadGameMessage msg) {
            //System.out.println("Loading Game");
            ChessGame updated = msg.getGame().game();
            String color = msg.getColor();
            gameState.setCurrentGame(updated);
            String clientColor = gameState.getClientColor();  // This method can return "white" or "black" based on the client's role

            if (clientColor.equals(color)) {
                // If the client is the one who made the move, render normally
                client.renderBoard(clientColor);
            } else {
                // If the client is not the one who made the move, check if they are observing
                if (clientColor.equals("white")) {
                    // If client is white, render white's perspective
                    client.renderBoard("white");
                } else {
                    // If client is black, render black's perspective
                    client.renderBoard("black");
                }
//            ChessGame.TeamColor turnColor = updated.getTeamTurn();
//            if(Objects.equals(color, "black") && turnColor== ChessGame.TeamColor.BLACK){
//                client.renderBoard("black", updated);
//            }
//            else{
//                client.renderBoard("white", updated);
            }
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