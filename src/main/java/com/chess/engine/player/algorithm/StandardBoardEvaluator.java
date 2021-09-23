package com.chess.engine.player.algorithm;

import com.chess.engine.board.Board;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) -
                scorePlayer(board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return player.getPicesValue();
    }


}
