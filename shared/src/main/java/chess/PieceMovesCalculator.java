package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface PieceMovesCalculator {
    Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition);
}

class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            System.out.println("Bishop at:" + row + "," + col);

            while(1<row && row <=7 && 1<col && col<=7){
                row = row+direction[0];
                col = col+direction[1];
                System.out.println("row: " + row + " col: " + col);
                ChessPosition pos = new ChessPosition(row, col);
                System.out.println("piece there: " + board.getPiece(pos));
//                if(board.getPiece(pos)==null){
//                    ChessMove move = new ChessMove(myPosition, pos, null);
//                    allMoves.add(move);
//                }
            }
        }
        return allMoves;
    }
}

class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        System.out.println("All possible king moves:");
        return new ArrayList<>();
    }
}

class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        System.out.println("All possible pawn moves:");
        return new ArrayList<>();
    }
}

class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        System.out.println("All possible rook moves:");
        return new ArrayList<>();
    }
}

class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        System.out.println("All possible knight moves:");
        return new ArrayList<>();
    }
}

class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        System.out.println("All possible queen moves:");
        return new ArrayList<>();
    }
}
