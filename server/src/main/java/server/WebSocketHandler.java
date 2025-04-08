package server;
import chess.*;
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

            //System.out.println("Received raw message: " + message);             //debug
            //System.out.println("Command type: " + command.getCommandType());    //debug

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
        //System.out.println("got inside WSHandler.connect");
        int gameID = command.getGameID();
        //System.out.println("GameID: " + gameID + " username: " + username);

        //verify gameID first
        GameData game;
        try{
            game = gService.getGame(gameID);
        } catch (Exception e){
            String error = String.format("GameID %s does not exist", gameID);
            sendMessage(session.getRemote(), new ErrorNotification(ServerMessage.ServerMessageType.ERROR, error));
            return;
        }

        connections.add(username, session, gameID);
        var message = String.format("%s connected to the game ", username);
        //System.out.println(message);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, gameID, notification);

        //GameData game = gService.getGame(gameID);
        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        sendMessage(session.getRemote(), loadMessage);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws ResponseException {
        try {
            System.out.println("in wsH makeMove");
            ChessMove move = command.getMove();
            ChessPosition start = move.getStartPosition();
            System.out.println("move " + move.toString());

            GameData gameD = gService.getGame(command.getGameID());
            ChessGame game = gameD.game();

            ChessPiece piece = game.getBoard().getPiece(start);
            ChessGame.TeamColor playerColor = piece.getTeamColor();
            ChessGame.TeamColor turnColor = game.getTeamTurn();

            if (piece == null) {
                throw new ResponseException(400, "No piece at start position.");
            }

            //check correct player's turn
            if (playerColor != turnColor) {
                throw new ResponseException(403, "It's not this color's turn.");
            }

            //Check correct player for that color
            if ((playerColor == ChessGame.TeamColor.WHITE && !username.equals(gameD.whiteUsername())) ||
                    (playerColor == ChessGame.TeamColor.BLACK && !username.equals(gameD.blackUsername()))) {
                throw new ResponseException(403, "It's not your turn.");
            }

            try{
                game.makeMove(move);    //this should check if valid move
            } catch (InvalidMoveException e){
                System.out.println("Invalid move!!!");
                ErrorNotification errorN = new ErrorNotification(ServerMessage.ServerMessageType.ERROR, "invalid move");
                connections.broadcastSelf(username, errorN);
                return;
                //throw new Error("Invalid move");
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }

            GameData updatedGameD = new GameData(
                    gameD.gameID(),
                    gameD.whiteUsername(),
                    gameD.blackUsername(),
                    gameD.gameName(),
                    game
            );

            gService.updateGame(updatedGameD);
            var loadNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGameD);
            connections.broadcastAll(command.getGameID(), loadNotification);

            var message = String.format("%s made move %s", username, move.toString());
            var updateNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(username, command.getGameID(), updateNotification);

            //if check, stalemate or checkmate, send notification to ALL
            ChessGame.TeamColor currentTurn = game.getTeamTurn();
            if (game.isInCheckmate(currentTurn)) {
                connections.broadcastAll(command.getGameID(), new NotificationMessage(
                        ServerMessage.ServerMessageType.NOTIFICATION, "Checkmate! Game over."));
            } else if (game.isInStalemate(currentTurn)) {
                connections.broadcastAll(command.getGameID(), new NotificationMessage(
                        ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate! Game drawn."));
            } else if (game.isInCheck(currentTurn)) {
                connections.broadcastAll(command.getGameID(), new NotificationMessage(
                        ServerMessage.ServerMessageType.NOTIFICATION, "Check!"));
            }
        } catch (Exception ex) {
//            ErrorNotification errorN = new ErrorNotification(ServerMessage.ServerMessageType.ERROR, ex.toString());
//            connections.broadcastAll(gameD.gameID, errorN);
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
