package server;

import com.google.gson.Gson;
import exception.ResponseException;
import exceptions.Unauthorized;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.Action;
import webSocketMessages.Notification;
import websocket.commands.*;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try{
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            //maybe change this bit? throwing a custom unauthorized exception
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch(command.getCommandType()){
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE-> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException ex){
            //serialize and send error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex){
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage());
        }
    }

    private void connect(Session session, String username, ConnectCommand command){
        //send a message to everyone else that a play has connected
    }
    private void enter(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(visitorName, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command){

    }
    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void leaveGame(Session session, String username, LeaveCommand command){

    }
    private void resign(Session session, String username, ResignCommand command){

    }
    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    private void saveSession(int gameID, Session session){
        //checks if session is already in connectionManager
        if(connections.find(gameID)==null){
            //if not, adds it
            connections.add(gameID, session); //key and value
        }
    }

    private String getUsername(String authToken){
        int authInt = Integer.parseInt(authToken);
        //validate the authtoken
        String username =
        return username;
    }
}
