package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    public ChessGame(ChessGame other){
        this.turn = other.turn;
        this.gameBoard = new ChessBoard(other.gameBoard);
    }

    public ChessGame makeCopy(){
        return new ChessGame(this);
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(gameBoard.getPiece(startPosition)==null){
            return null;
        }
        ArrayList<ChessMove> validMvs = new ArrayList<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);
        for(ChessMove move : moves){    //see if it would leave king in danger of check
            ChessGame copy = makeCopy();
            copy.makeMove(move);
            if(!copy.isInCheck(piece.getTeamColor())){
                validMvs.add(move);
            }
        }
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
            if(move.getPromotionPiece()!= null){ //if we need to promote
                ChessPiece.PieceType t = move.getPromotionPiece();
                ChessPiece promoPiece = new ChessPiece(piece.getTeamColor(), t);
                gameBoard.addPiece(move.getEndPosition(), promoPiece);
            }
            else{
                gameBoard.addPiece(move.getEndPosition(), piece);
            }
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
        ChessPosition kingPos = gameBoard.findKing(teamColor);
        for(int r=0; r<8; r++){
            for(int c=0; c<8; c++){
                ChessPosition currPos = new ChessPosition(r,c);
                if(gameBoard.getPiece(currPos)!=null){ //if there's a piece there
                    ChessPiece curr = gameBoard.getPiece(new ChessPosition(r, c));
                    if(curr.getTeamColor() != teamColor){// if that piece is the opposite color
                        Collection<ChessMove> allMoves = curr.pieceMoves(gameBoard, currPos);
                        for(ChessMove move : allMoves){
                            if(move.getEndPosition().equals(kingPos)){ //check if one of it's valid moves has an endPosition of the king's Position
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition isInCheck(TeamColor teamColor, boolean retPos) {
        ChessPosition kingPos = gameBoard.findKing(teamColor);
        for(int r=0; r<8; r++){
            for(int c=0; c<8; c++){
                ChessPosition currPos = new ChessPosition(r,c);
                if(gameBoard.getPiece(currPos)!=null){ //if there's a piece there
                    ChessPiece curr = gameBoard.getPiece(new ChessPosition(r, c));
                    if(curr.getTeamColor() != teamColor){// if that piece is the opposite color
                        Collection<ChessMove> allMoves = curr.pieceMoves(gameBoard, currPos);
                        for(ChessMove move : allMoves){
                            if(move.getEndPosition().equals(kingPos)){ //check if one of it's valid moves has an endPosition of the king's Position
                                return currPos;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            ChessPosition attackPos = isInCheck(teamColor, true);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for(int r=0; r<8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPosition currPos = new ChessPosition(r, c);
                if (gameBoard.getPiece(currPos) != null) { //if there's a piece there
                    ChessPiece curr = gameBoard.getPiece(new ChessPosition(r, c));
                    if (curr.getTeamColor() == teamColor) {//for every piece that's our Teamcolor
                        if(validMoves(currPos).isEmpty() && !isInCheck(teamColor)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
