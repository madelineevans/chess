package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import static ui.EscapeSequences.*;

public class DrawBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    //private static final String EMPTY = "\u2003\u2003";

    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

//        drawWhiteRow(out);
//        drawBlackRow(out);
        drawChessBoard(out);

        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { "TIC", "TAC", "TOE" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

//    private static void drawTicTacToeBoard(PrintStream out) {
//
//        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {  //change so 8 rows
//
//            drawRowOfSquares(out);
//
//            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) { //get rid of
//                // Draw horizontal row separator.
//                drawHorizontalLine(out);
//                setBlack(out);
//            }
//        }
//    }

    private static void drawChessBoard(PrintStream out){
        //drawHeaders(out);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) { //for each row call a row printer
            if(boardRow == 1){
                drawBlackRow(out, WHITE_PAWN);
            }
            else if(boardRow == 6){
                drawWhiteRow(out, BLACK_PAWN);
            }
            else if(boardRow%2==0){
                drawWhiteRow(out, EMPTY);
            }
            else{
                drawBlackRow(out, EMPTY);
            }
        }
    }

    private static void drawWhiteRow(PrintStream out, String player) {
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                if (col % 2 == 0) {
                    drawWhiteSquare(out, player);
                } else {
                    drawBlackSquare(out, player);
                }
                //out.print(" "); // Print a space to visualize the square.
            }
            out.println(); // Move to the next line after printing the full row.
        }
    }

    private static void drawBlackRow(PrintStream out, String player) {
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                //String player = EMPTY;
                if (col % 2 == 0) {
                    drawBlackSquare(out, "A");
                } else {
                    drawWhiteSquare(out, "A");
                }
                //out.print(" "); // Print a space to visualize the square.
            }

            out.println(); // Move to the next line after printing the full row.
        }
    }

    private static void drawWhiteSquare(PrintStream out, String player){
        setGrey(out);
        out.print(player);
        out.print(RESET_BG_COLOR);
    }
    private static void drawBlackSquare(PrintStream out, String player){
        setDarkGreen(out);
        out.print(player);
        out.print(RESET_BG_COLOR);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printWhitePlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
    private static void printBlackPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}