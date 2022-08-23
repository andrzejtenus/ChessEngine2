package com.chess.engine.player.algorithm;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.gui.Table;

import static com.chess.engine.pieces.Piece.PieceType.PAWN;

public class AlgUpdate implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int depth;
    public int calculatedMoves;
    private Move lastMove;

    public AlgUpdate(final int depth, Move move) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.depth = depth;
        calculatedMoves = 0;
        lastMove = move;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int currentValue;
        calculatedMoves = 0;

        for (var move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), depth - 1, alfa, beta) :
                        max(moveTransition.getTransitionBoard(), depth - 1, alfa, beta);
                if (board.currentPlayer().getAlliance().isWhite() && currentValue >= maxValue) {
                    //komputer nie widzi lepszego ruchu tylko punktowo taki sam
                    if (currentValue == maxValue) {
                        if (move.getMovedPiece().isFirstMove() && !bestMove.getMovedPiece().isFirstMove()) {
                            maxValue = currentValue;
                            bestMove = move;
                        } else if (move.getMovedPiece().getPieceType() == PAWN && bestMove.getMovedPiece().getPieceType() != PAWN) {
                            maxValue = currentValue;
                            bestMove = move;
                        } else if (move.isAttack() && !bestMove.isAttack()) {
                            maxValue = currentValue;
                            bestMove = move;
                        } else if (lastMove != null && bestMove.getMovedPiece().getPieceType() == lastMove.getMovedPiece().getPieceType() && move.getMovedPiece().getPieceType() != lastMove.getMovedPiece().getPieceType()) {
                            maxValue = currentValue;
                            bestMove = move;
                        }
                    } else {
                        maxValue = currentValue;
                        bestMove = move;
                    }
                } else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= minValue) {
                    if (currentValue == minValue) {
                        if (move.getMovedPiece().isFirstMove() && !bestMove.getMovedPiece().isFirstMove()) {
                            minValue = currentValue;
                            bestMove = move;
                        } else if (move.getMovedPiece().getPieceType() == PAWN && bestMove.getMovedPiece().getPieceType() != PAWN) {
                            minValue = currentValue;
                            bestMove = move;
                        } else if (move.isAttack() && !bestMove.isAttack()) {
                            maxValue = currentValue;
                            bestMove = move;
                        } else if (lastMove != null && bestMove.getMovedPiece().getPieceType() == lastMove.getMovedPiece().getPieceType() && move.getMovedPiece().getPieceType() != lastMove.getMovedPiece().getPieceType()) {
                            maxValue = currentValue;
                            bestMove = move;
                        }
                    } else {
                        minValue = currentValue;
                        bestMove = move;
                    }
                }
            }
        }
        var executionTime = System.currentTimeMillis() - startTime;
        System.out.println(executionTime + " calculated moves: " + calculatedMoves);
        if (board.currentPlayer().getAlliance().isWhite()) {
            Table.get().setLastWhiteMove(bestMove);
        } else {
            Table.get().setLastBlackMove(bestMove);
        }
        return bestMove;
    }

    public int min(Board board, int depth, int alfa, int beta) {
        calculatedMoves++;
        if (depth == 0 || this.isEndGameScenario(board))
            return this.boardEvaluator.evaluate(board, depth);
        int minValue = Integer.MAX_VALUE;
        for (var move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1, alfa, beta);
                if (currentValue <= minValue)
                    minValue = currentValue;
                if (beta > minValue)
                    beta = minValue;
                if (beta <= alfa)
                    break;
            }
        }
        return minValue;
    }

    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }

    public int max(Board board, int depth, int alfa, int beta) {
        calculatedMoves++;
        if (depth == 0 || this.isEndGameScenario(board))
            return this.boardEvaluator.evaluate(board, depth);
        int maxValue = Integer.MIN_VALUE;
        for (var move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1, alfa, beta);
                if (currentValue >= maxValue)
                    maxValue = currentValue;
                if (alfa < maxValue)
                    alfa = maxValue;
                if (beta <= alfa)
                    break;
            }
        }
        return maxValue;
    }
}
