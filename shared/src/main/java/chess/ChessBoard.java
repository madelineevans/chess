package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[(position.getRow())-1][(position.getColumn())-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 1; row<=8; row++){
            ChessGame.TeamColor c = ChessGame.TeamColor.WHITE;
//            if(row==1|row==2){
//                c = ChessGame.TeamColor.WHITE;
//            }
            if(row==7|row==8){
                c = ChessGame.TeamColor.BLACK;
            }
            if (row == 2){
                for (int col = 1; col <= 8; col++) {
                    addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.PAWN));
                }
            }
            else if(row == 7){
                for (int col = 1; col <= 8; col++) {
                    addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.PAWN));
                }
            }
            else if(row == 1|row==8){
                for (int col = 1; col <=8; col++) {
                    if(col ==1| col==8){
                        addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.ROOK));
                    }
                    if(col==2|col==7){
                        addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.KNIGHT));
                    }
                    if(col==3|col==6){
                        addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.BISHOP));
                    }
                    if(col==4){
                        addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.QUEEN));
                    }
                    if(col==5){
                        addPiece(new ChessPosition(row,col), new ChessPiece(c,ChessPiece.PieceType.KING));
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 7; i >= 0; i--) {
            ChessPiece[] row = squares[i];
            for(ChessPiece piece : row) {
                sb.append("|");
                if(piece != null) {
                    System.out.println("Piece type: " +piece.toString() +" at row " +i);
                    sb.append(piece.toString());
                }
                else{
                    sb.append(" ");
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
