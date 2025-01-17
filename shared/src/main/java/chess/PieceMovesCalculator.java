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
        System.out.println("All possible bishop moves:");
        return new ArrayList<>();
    }
}

//class KingMovesCalculator implements PieceMovesCalculator{}
//class PawnMovesCalculator implements PieceMovesCalculator{}
//class KnightMovesCalculator implements PieceMovesCalculator{}
//class QueenMovesCalculator implements PieceMovesCalculator{}
//class RookMovesCalculator implements PieceMovesCalculator{}
