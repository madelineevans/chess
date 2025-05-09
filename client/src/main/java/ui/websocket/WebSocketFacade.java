package ui.websocket;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ResponseException;
import websocket.commands.*;
import websocket.messages.ErrorNotification;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                    String typeStr = jsonObject.get("serverMessageType").getAsString();
                    ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.valueOf(typeStr);

                    Gson gson = new Gson();
                    ServerMessage notification;

                    switch (type) {
                        case NOTIFICATION -> notification = gson.fromJson(message, NotificationMessage.class);
                        case ERROR -> notification = gson.fromJson(message, ErrorNotification.class);
                        case LOAD_GAME -> notification = gson.fromJson(message, LoadGameMessage.class);
                        default -> throw new IllegalArgumentException("Unknown serverMessageType: " + typeStr);
                    }
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //System.out.println("WebSocket connected: " + session.getId());  //debug
    }

    public void connect(String authToken, int gameID) throws ResponseException {
        try {
            //System.out.println("got to WSFacade connect");
            var command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            //System.out.println("Sending connect command for user with auth: " + authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            System.out.println("got to WSFacade makeMove");
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            System.out.println("Sending makeMove command for user with auth : " + authToken + " for gameID: " + gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        try {
            //System.out.println("got to WSFacade lave");
            var command = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            //System.out.println("Sending leave command for user with auth: " + authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try {
            //System.out.println("got to WSFacade resign");
            var command = new ResignCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            //System.out.println("Sending resign command for user with auth: " + authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
