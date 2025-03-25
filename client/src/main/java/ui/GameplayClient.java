package ui;
import exceptions.DataAccessException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GameplayClient extends Client{
    private final ServerFacade server;
    //DrawBoard board = new DrawBoard();
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public GameplayClient(String serverUrl, String authToken) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public String printBoard(){
        DrawBoard.drawChessBoard(out);
        return "White board";
    }

    public String printUpsidedownBoard(){
        DrawBoard.drawChessBoardUpsidown(out);
        return "Black board";
    }

    @Override
    public String eval(String command) {
        var tokens = command.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "white" -> printBoard();
            case "black" -> printUpsidedownBoard();
            case "quit" -> "quit_to_postlogin";
            case "help" -> help();
            default -> help();
        };
    }

    @Override
    public String help() {
        return "not yet implemented";
    }
}
