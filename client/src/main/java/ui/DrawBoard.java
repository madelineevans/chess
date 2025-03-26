package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import static ui.EscapeSequences.*;

public class DrawBoard {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static Random rand = new Random();

    public static void drawChessBoard(PrintStream out){
        for (int boardRow = BOARD_SIZE_IN_SQUARES-1; boardRow >= 0; --boardRow) { //for each row call a row printer
            if(boardRow == 2){
                drawWhiteRow(out, " P ", SET_TEXT_COLOR_MAGENTA, Integer.toString(boardRow));
            }
            else if(boardRow == 7){
                drawBlackRow(out, " P ", SET_TEXT_COLOR_BLUE, Integer.toString(boardRow));
            }
            else if(boardRow == 0 || boardRow == 9){
                drawReverseBorder(out);
            }
            else if(boardRow == 1 || boardRow == 8){
                String text = SET_TEXT_COLOR_MAGENTA;
                if(boardRow == 8){
                    text = SET_TEXT_COLOR_BLUE;
                }
                drawQueenRow(out, text, Integer.toString(boardRow));
            }
            else if(boardRow%2==0){
                drawWhiteRow(out, EMPTY, SET_TEXT_COLOR_WHITE, Integer.toString(boardRow));
            }
            else{
                drawBlackRow(out, EMPTY, SET_TEXT_COLOR_WHITE, Integer.toString(boardRow));
            }
        }
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public static void drawChessBoardUpsidedown(PrintStream out){
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) { //for each row call a row printer
            if(boardRow == 2){
                drawBlackRow(out, " P ", SET_TEXT_COLOR_MAGENTA, Integer.toString(boardRow));
            }
            else if(boardRow == 7){
                drawWhiteRow(out, " P ", SET_TEXT_COLOR_BLUE, Integer.toString(boardRow));
            }
            else if(boardRow == 0 || boardRow == 9){
                drawBorder(out);
            }
            else if(boardRow == 1 || boardRow == 8){
                String text = SET_TEXT_COLOR_MAGENTA;
                if(boardRow == 8){
                    text = SET_TEXT_COLOR_BLUE;
                }
                drawReverseQueenRow(out, text, Integer.toString(boardRow));
            }
            else if(boardRow%2==0){
                drawBlackRow(out, EMPTY, SET_TEXT_COLOR_WHITE, Integer.toString(boardRow));
            }
            else{
                drawWhiteRow(out, EMPTY, SET_TEXT_COLOR_WHITE, Integer.toString(boardRow));
            }
        }
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void drawBorder(PrintStream out){
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                String val = setColLetter(col);
                drawBorderSquare(out, val);
            }
        }
        out.println();
    }

    private static void drawReverseBorder(PrintStream out){
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = BOARD_SIZE_IN_SQUARES-1; col >= 0; --col) {
                String val = setColLetter(col);
                drawBorderSquare(out, val);
            }
        }
        out.println();
    }

    private static String setColLetter(int col){
        return switch (col) {
            case 1 -> "h";
            case 2 -> "g";
            case 3 -> "f";
            case 4 -> "e";
            case 5 -> "d";
            case 6 -> "c";
            case 7 -> "b";
            case 8 -> "a";
            default -> " ";
        };
    }

    private static void drawBlackRow(PrintStream out, String player, String textColor, String row) {
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                if((col== 0 || col == 9) && (!Objects.equals(row, "0") || !row.equals("9"))){
                    drawBorderSquare(out, row);
                }
                else if (col % 2 == 0) {
                    drawWhiteSquare(out, player, textColor);
                }
                else {
                    drawBlackSquare(out, player, textColor);
                }
            }
            out.println();
        }
    }

    private static void drawWhiteRow(PrintStream out, String player, String textColor, String row) {
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                //String player = EMPTY;
                if((col== 0 || col == 9) && (!Objects.equals(row, "0") || !row.equals("9"))){
                    drawBorderSquare(out, row);
                }
                else if (col % 2 == 0) {
                    drawBlackSquare(out, player, textColor);
                } else {
                    drawWhiteSquare(out, player, textColor);
                }
            }

            out.println();
        }
    }

    private static void drawReverseQueenRow(PrintStream out, String textColor, String row){
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                String player = EMPTY;
                player = switch (col) {
                    case 1, 8 -> " R ";
                    case 2, 7 -> " N ";
                    case 3, 6 -> " B ";
                    case 4 -> " K ";
                    case 5 -> " Q ";
                    default -> player;
                };

                if((col== 0 || col == 9) && (!Objects.equals(row, "0") || !row.equals("9"))){
                    drawBorderSquare(out, row);
                }
                else if(Objects.equals(textColor, SET_TEXT_COLOR_MAGENTA)){
                    if (col % 2 == 0) {
                        drawBlackSquare(out, player, textColor);
                    } else {
                        drawWhiteSquare(out, player, textColor);
                    }
                }
                else{
                    if (col % 2 == 0) {
                        drawWhiteSquare(out, player, textColor);
                    } else {
                        drawBlackSquare(out, player, textColor);
                    }
                }
            }
            out.println();
        }
    }

    private static void drawQueenRow(PrintStream out, String textColor, String row){
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                String player = EMPTY;
                player = switch (col) {
                    case 1, 8 -> " R ";
                    case 2, 7 -> " N ";
                    case 3, 6 -> " B ";
                    case 4 -> " Q ";
                    case 5 -> " K ";
                    default -> player;
                };

                if((col== 0 || col == 9) && (!Objects.equals(row, "0") || !row.equals("9"))){
                    drawBorderSquare(out, row);
                }
                else if(Objects.equals(textColor, SET_TEXT_COLOR_BLUE)){
                    if (col % 2 == 0) {
                        drawBlackSquare(out, player, textColor);
                    } else {
                        drawWhiteSquare(out, player, textColor);
                    }
                }
                else{
                    if (col % 2 == 0) {
                        drawWhiteSquare(out, player, textColor);
                    } else {
                        drawBlackSquare(out, player, textColor);
                    }
                }
            }
            out.println();
        }
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