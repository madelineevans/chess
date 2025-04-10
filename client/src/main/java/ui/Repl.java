package ui;

import chess.ChessGame;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import ui.clients.Client;
import ui.clients.GameplayClient;
import ui.clients.PostloginClient;
import ui.clients.PreloginClient;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl{
    private Client client;
    //private GameplayClient gClient;
    private NotificationHandler nh;
    //private NotificationHandler gnh;
    private WebSocketFacade ws;
    private final String serverUrl;

    public Repl(String serverUrl){
        this.serverUrl = serverUrl;
        this.client = new PreloginClient(serverUrl);
        //this.nh = new NotificationHandler(client);
        this.nh = new NotificationHandler(client);
        try{
            this.ws = new WebSocketFacade(serverUrl, nh);
        } catch (ResponseException e) {  // Catching specific ResponseException
            System.out.println("Error connecting to WebSocket: " + e.getMessage());
            e.printStackTrace();
            this.ws = null;
        }
    }

    public void run() {
        System.out.println("Login to start playing Chess.");
        System.out.print(client.help());
        String line = "";

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {

            if (!result.equals("quit_to_prelogin") && !result.equals("quit_to_postlogin")) {
                printPrompt();
                line = scanner.nextLine();
            }

            try {
                if ("quit_to_prelogin".equals(result)) {
                    System.out.println("Returning to pre-login screen...\n");
                    transitionTo(new PreloginClient(client.getServerUrl()));
                    result = "";
                    continue;
                }
                else if ("quit_to_postlogin".equals(result)) {
                    System.out.println("Returning to post-login screen...\n");
                    transitionTo(new PostloginClient(client.getServerUrl(), client.getAuthToken(), nh, ws));
                    result = "";
                    continue;
                }

                result = client.eval(line);

                System.out.print(result);
                if (client instanceof PreloginClient && (result.startsWith("Logged in") || (result.startsWith("Registered")))) {
                    transitionTo(new PostloginClient(client.getServerUrl(), client.getAuthToken(), nh, ws));
                } else if (client instanceof PostloginClient && result.startsWith("Joined game")) {

                    String color;
                    if(result.endsWith("black.")){
                        color = "black";
                    }
                    else{
                        color = "white";
                    }

//                    int gameID = findGameID(result);
//                    ChessGame gameBoard = client.getServer().getGame(client.getAuthToken(), gameID);

                    transitionToGame(new GameplayClient(client.getServerUrl(), client.getAuthToken(), color, findGameID(result), nh, ws), color);
                } else if (client instanceof PostloginClient && result.startsWith("Observing")) {
                    //int gameID = findGameID(result);
                    //ChessGame gameBoard = client.getServer().getGame(client.getAuthToken(), gameID);
                    transitionToGame(new GameplayClient(client.getServerUrl(), client.getAuthToken(), "white", findGameID(result), nh, ws), "white");
                }

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private void transitionTo(Client newClient){
        this.client = newClient;
        System.out.print(client.help());
    }

    private void transitionToGame(GameplayClient newClient, String color){
        this.client = newClient;

        //ChessGame currentGame = newClient.getGame();
        ChessGame currentGame;
        try {
            currentGame = newClient.getServer().getGame(client.getAuthToken(), newClient.getGameID());
        } catch (DataAccessException e) {
            //System.out.println("Could not load current game state: " + e.getMessage());
            currentGame = new ChessGame(); // Fallback to empty board if needed
        }
        //client.renderBoard(color, currentGame);
        System.out.print(client.help());
    }

    private int findGameID(String result){
        String[] parts = result.split(" ");
        int gameID = Integer.parseInt(parts[2]);
        //System.out.println("Game ID: " + gameID);
        return gameID;
    }

}
