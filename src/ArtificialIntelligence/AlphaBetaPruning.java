package ArtificialIntelligence;

import TicTacToe.Board;

class AlphaBetaPruning {

    private static double maxPly;

    private AlphaBetaPruning () {}

    static void run (Board.State player, Board board, double maxPly) {
        if (maxPly < 1) {
            throw new IllegalArgumentException("Maximum depth must be > 0.");
        }
        AlphaBetaPruning.maxPly = maxPly;
        alphaBetaPruning(player, board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    /**
     ** Where the brunt of the algorithm lies, with each value represented accordingly such as 
     ** the player that the AI will identify as, the alpha and beta value, and the current
     ** 'ply' or depth and return the score of the board
     **/
    private static int alphaBetaPruning (Board.State player, Board board, double alpha, double beta, int currentPly) {
        if (currentPly++ == maxPly || board.isGameOver()) {
            return score(player, board);
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

            // WHERE THE PRUNING OCCURS
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

            // WHERE THE PRUNING OCCURS
            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }
        return (int)beta;
    }

    private static int score (Board.State player, Board board) {
        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player should be X or O only.");
        }

        Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

        if (board.isGameOver() && board.getWinner() == player) {
            return 10;
        } else if (board.isGameOver() && board.getWinner() == opponent) {
            return -10;
        } else {
            return 0;
        }
    }

}
