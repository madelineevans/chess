import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
        try{
            var port = 8080;
            Server s = new Server();
            s.run(port);
            System.out.printf("Server started on port %d", port);
            return;
        } catch(Throwable ex){
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        }
}