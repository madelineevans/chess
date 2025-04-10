package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import websocket.messages.*;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Connection find(String username){
        return connections.get(username);
    }

    public void add(String username, Session session, int gameID) {
        var connection = new Connection(username, session, gameID);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String excludeVisitorName, int gameID, NotificationMessage notification) throws IOException {
        //make sure it will only send to people in the same game
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == gameID){
                    if (!c.username.equals(excludeVisitorName)) {
                        Gson gson = new Gson();
                        c.send(gson.toJson(notification));
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void broadcastAll(int gameID, ServerMessage notification) throws IOException {
        //make sure it will only send to people in the same game
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == gameID){
                    Gson gson = new Gson();
                    c.send(gson.toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void broadcastSelf(String selfName, ServerMessage notification) {
        try{
            //make sure it will only send to people in the same game
            var removeList = new ArrayList<Connection>();
            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (c.username.equals(selfName)) {
                        Gson gson = new Gson();
                        c.send(gson.toJson(notification));
                        return;
                    }
                } else {
                    removeList.add(c);
                }
            }

            // Clean up any connections that were left open.
            for (var c : removeList) {
                connections.remove(c.username);
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
