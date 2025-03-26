package ui;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl{
    private Client client;

    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
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
                    transitionTo(new PostloginClient(client.getServerUrl(), client.getAuthToken()));
                    result = "";
                    continue;
                }

                result = client.eval(line);

                System.out.print(result);
                if (client instanceof PreloginClient && result.startsWith("Logged in")) {
                    transitionTo(new PostloginClient(client.getServerUrl(), client.getAuthToken()));
                } else if (client instanceof PostloginClient && result.startsWith("Joined game")) {
                    String color;
                    if(result.endsWith("black.")){
                        color = "black";
                    }
                    else{
                        color = "white";
                    }
                    transitionToGame(new GameplayClient(client.getServerUrl(), client.getAuthToken()), color);
                } else if (client instanceof PostloginClient && result.startsWith("Observing")) {
                    transitionToGame(new GameplayClient(client.getServerUrl(), client.getAuthToken()), "white");
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
        client.renderBoard(color);
        System.out.print(client.help());
    }

}
