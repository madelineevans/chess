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

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
                if (client instanceof PreloginClient && result.startsWith("Logged in")) {
                    transitionTo(new PostloginClient(client.getServerUrl(), client.getAuthToken()));
                } else if (client instanceof PostloginClient && result.startsWith("Joined game")) {
                    transitionTo(new GameplayClient(client.getServerUrl(), client.getAuthToken()));
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

}
