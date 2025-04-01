package ui;
import chess.ChessGame;
import chess.ChessMove;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GameplayClient extends Client{
    private final ServerFacade server;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public GameplayClient(String serverUrl, String authToken) {
        super(serverUrl);
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public void printBoard(){
        System.out.println("\n");
        DrawBoard.drawChessBoard(out);
    }

    public void printBlackBoard(){
        System.out.println("\n");
        DrawBoard.drawChessBoardUpsidedown(out);
    }

    public void redraw(){
        ChessGame.TeamColor color; // = //determine own color
        if(color = ChessGame.TeamColor.WHITE){
            printBoard();
        }
        else{
            printBlackBoard();
        }
    }

    public void makeMove(String... params){


    }

    public void resign(){
        //ask the user if they really want to resign
        //if yes, resign them

    }

    public void highlightMoves(){

    }

    @Override
    public String eval(String command) {
        var tokens = command.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "help" -> help();
            case "redraw" -> redraw();
            case "leave" -> "quit_to_postlogin";
            case "move" -> makeMove(params);
            case "resign" -> resign();
            case "highlight" -> highlightMoves();

            default -> help();
        };
    }

    @Override
    public String help() {
        return """
                help
                redraw - redraws the chess board
                leave - removes you from the game
                move - makes your desired move
                resign - forfeit the game
                highlight - highlights legal moves you can make                
                """;
    }
}
