package ui;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static ui.DrawBoard.drawChessBoard;
import static ui.DrawBoard.drawChessBoardUpsidedown;

class DrawBoardTest {

    @Test
    void normalBoard(){
        //ChessBoard normBoard = new ChessBoard();
        ChessGame game = new ChessGame();
        //DrawBoard drawBoard = new DrawBoard();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawChessBoard(out, game);
    }

    @Test
    void normalUpsidownBoard(){
        //ChessBoard normBoard = new ChessBoard();
        ChessGame game = new ChessGame();
        //DrawBoard drawBoard = new DrawBoard();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawChessBoardUpsidedown(out, game);
    }

}