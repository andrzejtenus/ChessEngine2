package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class SilnikSzachowy {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();

        Table.get().show();
    }
}