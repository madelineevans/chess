package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface PieceMovesCalculator {
    Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition);

//    static ArrayList<ChessMove> canMove(ChessPosition pos, ChessBoard board, ChessPosition myPosition, int row, int col, ArrayList<ChessMove> allMoves) {
//        if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
//                    if (board.getPiece(pos) == null) {
//                        System.out.println(row + ", " + col);
//                        ChessMove move = new ChessMove(myPosition, pos, null);
//                        allMoves.add(move);
//                    } else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
//                        run = false;
//                        System.out.println(row + ", " + col);
//                        ChessMove move = new ChessMove(myPosition, pos, null);
//                        allMoves.add(move);
//                        //remove the enemy piece right here???
//                    }
//                    else{
//                        run = false;
//                    }
//                }
}

class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean run = true;

            while(1<=row && row <=7 && 1<=col && col<=7 && run){
                row = row+direction[0];
                col = col+direction[1];
                ChessPosition pos = new ChessPosition(row, col);
                //return canMove(pos, board, myPosition,row, col, allMoves);
                if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                    if (board.getPiece(pos) == null) {
                        System.out.println(row + ", " + col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                    } else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        run = false;
                        System.out.println(row + ", " + col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                        //remove the enemy piece right here???
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

class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean run = true;

            System.out.println("row: " + row + ", col: " + col);
            while(1<=row && row <=7 && 1<=col && col<=7 && run){
                row = row+direction[0];
                col = col+direction[1];
                ChessPosition pos = new ChessPosition(row, col);
                //System.out.println(row + ", " + col);
                //return canMove(pos, board, myPosition,row, col, allMoves);
                if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                    if (board.getPiece(pos) == null) {
                        System.out.println(row + ", " + col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                    }
                    else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        System.out.println(row + ", " + col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        allMoves.add(move);
                        //remove the enemy piece right here???
                        run = false;
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

class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        int[][] directions = {{1,0}, {1,1}, {0,1}, {1,-1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        System.out.println("King pos: " + row + ", " + col);

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
}

class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        System.out.println("All possible pawn moves:");
        return new ArrayList<>();
    }
}

class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        System.out.println("All possible knight moves:");
        return new ArrayList<>();
    }
}

class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        System.out.println("All possible queen moves:");
        return new ArrayList<>();
    }
}
