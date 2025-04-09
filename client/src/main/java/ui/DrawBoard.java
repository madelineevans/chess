package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import static ui.EscapeSequences.*;

public class DrawBoard {
    // Padded characters.
    private static final String EMPTY = "   ";

    public static void drawChessBoard(PrintStream out, ChessGame game) {
        ChessBoard board = game.getBoard();

        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        //out.print("\n");

        for (int row = 8; row >= 1; row--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                String player = EMPTY;
                String color = SET_TEXT_COLOR_WHITE;

                if (piece != null) {
                    if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                        player = " N ";
                    }
                    else{
                        player = " " + piece.getPieceType().toString().charAt(0) + " ";
                    }
                    color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
                }

                if ((row + col) % 2 == 0) {
                    drawBlackSquare(out, player, color);
                } else {
                    drawWhiteSquare(out, player, color);
                }

            }
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            out.println(RESET_BG_COLOR);
            //out.println();
        }

        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public static void drawChessBoardUpsidedown(PrintStream out, ChessGame game){
        ChessBoard board = game.getBoard();

        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + c + " ");
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        out.println(RESET_BG_COLOR);

        for (int row = 1; row <= 8; row++) {
            out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col >= 1; col--) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                String player = EMPTY;
                String color = SET_TEXT_COLOR_WHITE;

                if (piece != null) {
                    if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                        player = " N ";
                    }
                    else{
                        player = " " + piece.getPieceType().toString().charAt(0) + " ";
                    }

                    color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_MAGENTA : SET_TEXT_COLOR_BLUE;
                }

                if ((row + col) % 2 == 0) {
                    drawBlackSquare(out, player, color);
                } else {
                    drawWhiteSquare(out, player, color);
                }

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
    private static void drawBorderSquare(PrintStream out, String num){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + num + " ");
        out.print(RESET_BG_COLOR);
    }
}