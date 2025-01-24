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
        ArrayList<Integer> allDir = new ArrayList<>();      //directions to move piece
        ArrayList<int[]> dir = new ArrayList<>();       //directions to check if take another piece
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece.PieceType pp = null;
        boolean blocked = false;

        System.out.println("pawn at "+row+","+col);

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {       //if black
            allDir.add(-1);
            if(row == 7){
                allDir.add(-2);
            }
        }
        else{       //if white
            allDir.add(1);
            if(row== 2){
                allDir.add(2);
            }
        }

        for(Integer di : allDir){
            System.out.println("can move in direction "+di);
            if(di<0){//if black
                dir.add(new int[]{-1,-1});
                dir.add(new int[]{-1,1});
            }
            else{//if white
                dir.add(new int[]{1,-1});
                dir.add(new int[]{1,1});
            }
            ChessPosition pos = new ChessPosition(row+di, col);

            if (pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8) {
                if (!blocked && board.getPiece(pos) == null) { //if no one's blocking it's move
                    if(pos.getRow() == 8 | pos.getRow() == 1){
                        pp = ChessPiece.PieceType.QUEEN; //but could also be a rook,Knight, or bishop
                    }
                    ChessMove move = new ChessMove(myPosition, pos, pp);
                    System.out.println("["+pos.getRow()+","+pos.getColumn()+"]");
                    allMoves.add(move);
                }
                else{
                    blocked = true;
                }
                for(int[] d: dir){
                    System.out.println("direction to take is: ["+d[0]+","+d[1]+"]");
                    ChessPosition pos2 = new ChessPosition(row+d[0], col+d[1]);
                    System.out.println("checking for a piece to take at "+pos2.getRow()+","+pos2.getColumn());
                    if(board.getPiece(pos2) != null){
                        if (board.getPiece(pos2).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //can I take a piece
                            ChessMove move = new ChessMove(myPosition, pos2, null);
                            System.out.println("["+pos2.getRow()+","+pos2.getColumn()+"]");
                            allMoves.add(move);
                        }
                    }
                }
            }
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
