package server;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import exceptions.ResponseException;
import exceptions.Unauthorized;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;
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
        } catch (Unauthorized ex){
            //serialize and send error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex){
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException {
        //send a message to everyone else that a play has connected
        int gameID = command.getGameID();
        connections.add(username, session, gameID);
        var message = String.format("%s connected to the game as color ____ ", username); //add in a way to say what color their joining as
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, gameID, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws ResponseException {
        try {
            ChessMove move = command.getMove();
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();

            var message = String.format("%s moves from %s to %s", username, start.toString(), end.toString());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(username, command.getGameID(), notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void leaveGame(Session session, String username, LeaveCommand command) throws IOException {
        connections.remove(username);
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, command.getGameID(), notification);
    }

    private void resign(Session session, String username, ResignCommand command) {
        //render game unplayable, how to implement???
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
