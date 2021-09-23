package com.chess.engine.player.algorithm;

import com.chess.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);
}
