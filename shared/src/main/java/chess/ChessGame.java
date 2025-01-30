package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard gameBoard;

    public ChessGame() {
        this.turn=TeamColor.WHITE;
        ChessBoard b = new ChessBoard();
        b.resetBoard();
        this.gameBoard = b;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    //context, message, audience, purpose, product
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(gameBoard.getPiece(startPosition)==null){
            return null;
        }
        ArrayList<ChessMove> validMvs = new ArrayList<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);
//        for(ChessMove move : moves){    //see if it would leave king in danger of check
//
//        }
        return validMvs;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid -- invalid if puts
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        if(gameBoard.getPiece(start)==null){
            throw new InvalidMoveException("Invalid move: " + move);
        }
        ChessPiece piece = gameBoard.getPiece(start);
        if(piece.getTeamColor()!=turn){
            throw new InvalidMoveException("Invalid move: " + move);
        }
        Collection<ChessMove> valids = validMoves(start);
        HashSet<ChessMove> validSet = new HashSet<>(valids);
        if(!validSet.contains(move)){
            throw new InvalidMoveException("Invalid move: " + move);
        }
        else{
            gameBoard.addPiece(move.getEndPosition(), piece);
            gameBoard.removePiece(start);
            System.out.println("turn = "+getTeamTurn().toString());
            TeamColor newColor=TeamColor.WHITE;
            if(turn==TeamColor.WHITE){
                newColor = TeamColor.BLACK;
            }
            setTeamTurn(newColor);
            System.out.println("Now turn = "+getTeamTurn().toString());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find the king's location
        //check from all possible attack locations if there is a piece that could attack

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

}
