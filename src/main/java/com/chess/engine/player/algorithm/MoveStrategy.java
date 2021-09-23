package com.chess.engine.player.algorithm;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board);
}
