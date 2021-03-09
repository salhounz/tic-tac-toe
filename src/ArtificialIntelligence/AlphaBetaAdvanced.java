package ArtificialIntelligence;

import TicTacToe.Board;

class AlphaBetaAdvanced {

    private static double maxPly;

    private AlphaBetaAdvanced() {}

    static void run (Board.State player, Board board, double maxPly) {

        if (maxPly < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }

        AlphaBetaAdvanced.maxPly = maxPly;
        alphaBetaPruning(player, board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    private static int alphaBetaPruning (Board.State player, Board board, double alpha, double beta, int currentPly) {
        if (currentPly++ == maxPly || board.isGameOver()) {
            return score(player, board, currentPly);
        }

        if (board.getTurn() == player) {
            return getMax(player, board, alpha, beta, currentPly);
        } else {
            return getMin(player, board, alpha, beta, currentPly);
        }
    }

    // Play the move w/ the highest score
    private static int getMax (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;

        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);

            if (score > alpha) {
                alpha = score;
                indexOfBestMove = theMove;
            }
            // Where the pruning occurs
            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }
        return (int)alpha;
    }

    // Play the move w/ the lowest score
    private static int getMin (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;

        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);

            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);

            if (score < beta) {
                beta = score;
                indexOfBestMove = theMove;
            }
            // Where the pruning occurs
            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }
        return (int)beta;
    }

    // Get the score of the board while also taking depth into account
    private static int score (Board.State player, Board board, int currentPly) {

        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player must be X or O.");
        }

        Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

        if (board.isGameOver() && board.getWinner() == player) {
            return 10 - currentPly;
        } else if (board.isGameOver() && board.getWinner() == opponent) {
            return -10 + currentPly;
        } else {
            return 0;
        }
    }

}
