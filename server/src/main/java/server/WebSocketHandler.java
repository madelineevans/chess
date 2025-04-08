package server;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import exceptions.Unauthorized;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import service.GameService;
import service.ParentService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService uService; // or UserService or GameService
    private final GameService gService;

    public WebSocketHandler(UserService uService, GameService gService) {
        this.uService = uService;
        this.gService = gService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try{
            //UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(jsonObject.get("commandType").getAsString());
            UserGameCommand command;
            Gson gson = new Gson();
            switch (type) {
                case CONNECT -> command = new Gson().fromJson(message, ConnectCommand.class);
                case MAKE_MOVE -> command = new Gson().fromJson(message, MakeMoveCommand.class);
                case LEAVE -> command = new Gson().fromJson(message, LeaveCommand.class);
                case RESIGN -> command = new Gson().fromJson(message, ResignCommand.class);
                default -> throw new IllegalArgumentException("Unknown command type: " + type);
            }

            //maybe change this bit? throwing a custom unauthorized exception
            String username = getUsername(command.getAuthToken());
            saveSession(command.getGameID(), session, username);

            System.out.println("Received raw message: " + message);             //debug
            System.out.println("Command type: " + command.getCommandType());    //debug

            switch(command.getCommandType()){
                //case CONNECT -> System.out.println("got in the CONNECT switch");
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE-> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
//        } catch (Unauthorized ex){
//            //serialize and send error message
//            sendMessage(session.getRemote(), new ErrorNotification(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception ex){
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorNotification(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException, DataAccessException {
        //send a message to everyone else that a player has connected
        System.out.println("got inside WSHandler.connect");
        int gameID = command.getGameID();
        System.out.println("GameID: " + gameID + " username: " + username);
        connections.add(username, session, gameID);
        var message = String.format("%s connected to the game ", username);
        System.out.println(message);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, gameID, notification);

        GameData game = gService.getGame(gameID);
        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        sendMessage(session.getRemote(), loadMessage);
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

    private void saveSession(int gameID, Session session, String username){
        //checks if session is already in connectionManager
        if(connections.find(username)==null){   //originally this checked for the gameID, so if doesnt work go back
            //if not, adds it
            connections.add(username, session, gameID); //key and value
        }
    }

    public void sendMessage(RemoteEndpoint remote, ServerMessage message) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(message);
            remote.sendString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername(String authToken) {
        try {
            AuthData authData = uService.getAuth(authToken);
            return authData.username();
            //return getUsername(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to access data ", e);
        }
    }
}
