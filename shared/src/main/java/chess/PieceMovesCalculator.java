package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface PieceMovesCalculator {
    Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition);

    default Collection<ChessMove> canMove(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> allMoves, int[][] directions, int row, int col) {
        for (int[] direction : directions) {
            ChessPosition pos = new ChessPosition(row+direction[0], col+direction[1]);
            System.out.println(pos.toString());
            if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                if (board.getPiece(pos) == null) {
                    System.out.println(row + ", " + col);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    allMoves.add(move);
                } else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    System.out.println(row + ", " + col);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    allMoves.add(move);
                    //remove the enemy piece right here???
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

            while(1<=row && row <=7 && 1<=col && col<=7 && run){
                row = row+direction[0];
                col = col+direction[1];
                ChessPosition pos = new ChessPosition(row, col);
                if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                    if (board.getPiece(pos) == null) {
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                    } else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
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
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        int[][] directions = {{1,0}, {1,1}, {0,1}, {1,-1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        System.out.println("King pos: " + row + ", " + col);

        return canMove(board, myPosition, allMoves, directions, row, col);
    }
}

class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        ArrayList<int[]> allDir = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {       //if black
            allDir.add(new int[]{-1,0});
            if(row == 7){
                allDir.add(new int[]{-2,0});
            }
        }
        else{       //if white
            allDir.add(new int[]{1,0});
            if(row== 2){
                allDir.add(new int[]{2,0});
            }
        }
        return allMoves;
    }
}

class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{2,1},{-2,1},{-2,-1},{2,-1},{1,2},{-1,2},{-1,-2},{1,-2}};
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        return canMove(board, myPosition, allMoves, directions, row, col);
    }
}

class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,0}, {1,1}, {0,1}, {1,-1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};
        return getChessMoves(board, myPosition, directions);
    }
}
