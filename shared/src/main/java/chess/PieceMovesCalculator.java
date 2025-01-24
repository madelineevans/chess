package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface PieceMovesCalculator {
    Collection<ChessMove> findAllMoves(ChessBoard board, ChessPosition myPosition);

//    default ChessMove move(ChessBoard board, ChessPosition myPosition, ChessPosition pos, int row, int col) {
//        ChessMove move = null;
//        if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
//            if (board.getPiece(pos) == null) {
//                System.out.println(row + ", " + col);
//                return move = new ChessMove(myPosition, pos, null);
//            }
//            else if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
//                System.out.println(row + ", " + col);
//                return move = new ChessMove(myPosition, pos, null);
//                //remove the enemy piece right here???
//            }
//        }
//        return move;
//    }

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

            while(1<=row && row <=7 && 1<=col && col<=7 && run){
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
        //ArrayList<Integer> allDir = new ArrayList<>();      //directions to move piece
        ArrayList<int[]> allDir = new ArrayList<>();       //directions to check if take another piece
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece.PieceType pp = null;
        boolean blocked = false;

        System.out.println("pawn at "+row+","+col);

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
            System.out.println("dir "+ di[0]+","+di[1]);
            ChessPosition pos = new ChessPosition(row+di[0], col+di[1]);
            System.out.println("new pos "+ pos.getRow()+","+pos.getColumn());
            if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                System.out.println("blocked is "+blocked);
                System.out.println("Piece at position: " + board.getPiece(pos));
                System.out.println("i="+i);
                if (i<2){
                    if (board.getPiece(pos) != null ) {
                        if (board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //can I take a piece
                            ChessMove move = new ChessMove(myPosition, pos, null);
                            System.out.println("[" + pos.getRow() + "," + pos.getColumn() + "]");
                            allMoves.add(move);
                        }
                    }
                }
                else if (!blocked && board.getPiece(pos) == null) { //if no one's blocking it's move
                    System.out.println("in here");
                    if (pos.getRow() == 8 | pos.getRow() == 1) {
                        pp = ChessPiece.PieceType.QUEEN; //but could also be a rook,Knight, or bishop
                    }
                    ChessMove move = new ChessMove(myPosition, pos, pp);
                    System.out.println("[" + pos.getRow() + "," + pos.getColumn() + "]");
                    allMoves.add(move);
                }
                else {
                    blocked = true;
                }
            }
            i++;
        }
        return allMoves;
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
