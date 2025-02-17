package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface PieceMovesCalculator {
    Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition);

    default Collection<ChessMove> canMove(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessPosition pos = new ChessPosition(row+direction[0], col+direction[1]);
            if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                if (board.getPiece(pos) == null) {
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    allMoves.add(move);
                }
                else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    allMoves.add(move);
                }
            }
        }
        return allMoves;
    }

    default Collection<ChessMove> getChessMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean run = true;

            while(1<=row && row <=8 && 1<=col && col<=8 && run){
                row = row+direction[0];
                col = col+direction[1];
                ChessPosition pos = new ChessPosition(row, col);
                if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                    if (board.getPiece(pos) == null) {
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                    }
                    else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        run = false;
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                    }
                    else{
                        run = false;
                    }
                }
            }
        }
        return allMoves;
    }
}

class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        return getChessMoves(board, myPosition, directions);
    }
}

class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        return getChessMoves(board, myPosition, directions);
    }
}

class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,0}, {1,1}, {0,1}, {1,-1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};
        return canMove(board, myPosition, directions);
    }
}

class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>(); //moves to return
        ChessPiece.PieceType[] allPro = { //all possible promotions
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
        };
        ArrayList<int[]> allDir = new ArrayList<>();       //directions to check if take another piece
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean blocked = false;

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {       //if black
            allDir.add(new int[]{-1,-1});
            allDir.add(new int[]{-1,1});
            allDir.add(new int[]{-1,0});
            if(row == 7){
                allDir.add(new int[]{-2,0});
            }
        }
        else{       //if white
            allDir.add(new int[]{1,-1});
            allDir.add(new int[]{1,1});
            allDir.add(new int[]{1,0});
            if(row== 2){
                allDir.add(new int[]{2,0});
            }
        }

        int i = 0;
        for(int[] di : allDir){
            ChessPosition pos = new ChessPosition(row+di[0], col+di[1]);
            if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                if (i<2){
                    if (board.getPiece(pos) != null ) {
                        if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //can I take a piece
                            pawnPromoCheck(myPosition, allMoves, allPro, pos);
                        }
                    }
                }
                else if (!blocked && board.getPiece(pos) == null) { //if no one's blocking it's move
                    pawnPromoCheck(myPosition, allMoves, allPro, pos);

                }
                else {
                    blocked = true;
                }
            }
            i++;
        }
        return allMoves;
    }

    private void pawnPromoCheck(ChessPosition myPosition, ArrayList<ChessMove> allMoves, ChessPiece.PieceType[] allPro, ChessPosition pos) {
        if (pos.getRow() == 8 | pos.getRow() == 1) {
            for(ChessPiece.PieceType p : allPro) {
                ChessMove move = new ChessMove(myPosition, pos, p);
                allMoves.add(move);
            }

        }
        else{
            ChessMove move = new ChessMove(myPosition, pos, null);
            allMoves.add(move);
        }
    }
}

class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{2,1},{-2,1},{-2,-1},{2,-1},{1,2},{-1,2},{-1,-2},{1,-2}};
        return canMove(board, myPosition, directions);
    }
}

class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,0}, {1,1}, {0,1}, {1,-1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};
        return getChessMoves(board, myPosition, directions);
    }
}
