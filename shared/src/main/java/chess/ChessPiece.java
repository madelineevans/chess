package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    public ChessPiece(ChessPiece other){
        this.pieceColor = other.pieceColor;
        this.pieceType = other.pieceType;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new ArrayList<>();

        if (pieceType == PieceType.BISHOP) {
            PieceMovesCalculator ourPiece = new BishopMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        else if (pieceType == PieceType.KNIGHT) {
            PieceMovesCalculator ourPiece = new KnightMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        else if (pieceType == PieceType.KING) {
            PieceMovesCalculator ourPiece = new KingMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        else if (pieceType == PieceType.QUEEN) {
            PieceMovesCalculator ourPiece = new QueenMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        else if (pieceType == PieceType.ROOK) {
            PieceMovesCalculator ourPiece = new RookMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        else if (pieceType == PieceType.PAWN) {
            PieceMovesCalculator ourPiece = new PawnMovesCalculator();
            allMoves = ourPiece.findAllMoves(board, myPosition);
        }
        return allMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            return switch (pieceType) {
                case KING -> "k";
                case QUEEN -> "q";
                case BISHOP -> "b";
                case KNIGHT -> "n";
                case ROOK -> "r";
                case PAWN -> "p";
            };
        }
        else {
            return switch (pieceType) {
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case PAWN -> "P";
            };
        }
    }
}
