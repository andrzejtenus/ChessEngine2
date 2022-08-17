package com.chess.engine.player.algorithm;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class TransmissionUpdate implements MoveStrategy {
    String oldBoard;

    public TransmissionUpdate(String oldBoard) {
        this.oldBoard = oldBoard;
    }

    Piece movedPiece = null;
    int destCoordinate = -1;

    @Override
    public Move execute(Board board) {
        return detectChanges(board);
    }

    private Move detectChanges(Board board) {
        String newBoard = "";
        while (destCoordinate == -1 || movedPiece == null) {
            newBoard = this.getNewBoard();
            if (newBoard.length() == 64) {
                for (int i = 0; i < 64; i++) {
                    if (this.oldBoard.charAt(i) != newBoard.charAt(i)) {
                        //zabranie bierki
                        if (this.oldBoard.charAt(i) < newBoard.charAt(i)) {
                            if (checkPieces(board, board.getTile(i).getPiece())) {
                                movedPiece = board.getTile(i).getPiece();
                            } else {
                                destCoordinate = i;
                            }
                        }
                        //polozenie bierki
                        else {
                            destCoordinate = i;
                        }
                    }
                }
            }
        }
        for (Move move : movedPiece.calculateLegalMoves(board)) {
            if (move.getDestinationCoordinate() == destCoordinate) {
                destCoordinate = -1;
                movedPiece = null;
                Table.get().setOldBoard(newBoard);
                return move;
            }
        }
        return null;
    }

    String getNewBoard() {
        String str = "";
        try {
            URL url = new URL("http://192.168.1.52/");
            Scanner s = new Scanner(url.openStream());

            while (s.hasNext()) {
                str += s.nextLine();
                System.out.println(s.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    boolean checkPieces(Board board, Piece piece) {
        boolean v = false;

        for (Piece p : board.currentPlayer().getActivePieces()) {
            if (p.equals(piece)) {
                return true;
            }
        }
        return false;
    }
}
