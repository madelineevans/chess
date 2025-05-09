package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;
import java.io.PrintStream;
import static ui.EscapeSequences.*;
import java.util.Collection;

public class DrawBoard {
    // Padded characters.
    private static final String EMPTY = "   ";

    public static void drawChessBoard(PrintStream out, ChessGame game) {
        ChessBoard board = game.getBoard();

        out.print("\n" +SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        //out.print("\n");

        for (int row = 8; row >= 1; row--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col <= 8; col++) {
                drawBoard1(out, board, row, col);

            }
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            out.println(RESET_BG_COLOR);
        }

        drawBoard2(out);
    }

    private static void drawBoard2(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void drawBoard1(PrintStream out, ChessBoard board, int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);

        String player = EMPTY;
        String color = SET_TEXT_COLOR_WHITE;

        if (piece != null) {
            player = setPlayer(piece);
            color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
        }

        if ((row + col) % 2 == 0) {
            drawBlackSquare(out, player, color);
        } else {
            drawWhiteSquare(out, player, color);
        }
    }

    public static void drawChessBoardUpsidedown(PrintStream out, ChessGame game){
        ChessBoard board = game.getBoard();

        out.print("\n" +SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);

        for (int row = 1; row <= 8; row++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col >= 1; col--) {
                drawBoard1(out, board, row, col);

            }
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            out.println(RESET_BG_COLOR);
        }

        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public static void drawChessBoardWithHighlights(PrintStream out, ChessGame game, Collection<ChessMove> legalMoves) {
        ChessBoard board = game.getBoard();

        out.print("\n" +SET_BG_COLOR_LIGHT_GREY +"   ");
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);

        for (int row = 8; row >= 1; row--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col <= 8; col++) {
                drawChessBoardHighlights(out, board, row, col, legalMoves);
            }
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            out.println(RESET_BG_COLOR);
        }

        drawBoard2(out);
    }

    public static void drawBlackChessBoardWithHighlights(PrintStream out, ChessGame game, Collection<ChessMove> legalMoves) {
        ChessBoard board = game.getBoard();

        out.print("\n" +SET_BG_COLOR_LIGHT_GREY +"   ");
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);

        for (int row = 1; row <= 8; row++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col >= 1; col--) {
                drawChessBoardHighlights(out, board, row, col, legalMoves);
            }
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            out.println(RESET_BG_COLOR);
        }

        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void drawChessBoardHighlights(PrintStream out, ChessBoard board, int row, int col, Collection<ChessMove> legalMoves){
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);

        String player = EMPTY;
        String color = SET_TEXT_COLOR_WHITE;

        //Collection<ChessMove> legalMoves
        boolean isLegalMove = legalMoves.stream().anyMatch(move -> move.getEndPosition().equals(pos));

        if (piece != null) {
            player = setPlayer(piece);
            color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
        }

        // Highlight legal moves with a different background color
        if (isLegalMove) {
            drawHighlightedSquare(out, player, color);
        } else if ((row + col) % 2 == 0) {
            drawBlackSquare(out, player, color);
        } else {
            drawWhiteSquare(out, player, color);
        }
    }

    private static String setPlayer(ChessPiece piece){
        if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            return " N ";
        }
        else{
            return " " + piece.getPieceType().toString().charAt(0) + " ";
        }
    }

    private static void drawHighlightedSquare(PrintStream out, String player, String textColor) {
        out.print(SET_BG_COLOR_YELLOW); // Assuming yellow is for highlights
        out.print(textColor);
        out.print(player);
        out.print(RESET_BG_COLOR);
    }

    private static void drawWhiteSquare(PrintStream out, String player, String textColor){
        out.print(SET_BG_COLOR_WHITE);
        out.print(textColor);
        out.print(player);
        out.print(RESET_BG_COLOR);
    }

    private static void drawBlackSquare(PrintStream out, String player, String textColor){
        out.print(SET_BG_COLOR_BLACK);
        out.print(textColor);
        out.print(player);
        out.print(RESET_BG_COLOR);
    }
}