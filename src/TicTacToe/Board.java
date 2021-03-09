package TicTacToe;

import java.util.HashSet;

public class Board {

    static final int BOARD_WIDTH = 3;
    // Each piece will either be blank an X or O
    public enum State {Blank, X, O}
    // The board will be a 3x3 board with a width of 3 established earlier
    private State[][] board;
    // To determine which players turn it will be 
    private State playersTurn;
    // To determine a winner
    private State winner;
    private HashSet<Integer> movesAvailable;

    private int moveCount;
    private boolean gameOver;

    Board() {
        board = new State[BOARD_WIDTH][BOARD_WIDTH];
        movesAvailable = new HashSet<>();
        reset();
    }

    /**
     ** Initialize the cells to be blank and all moves available for the beginning of the game
     **/
    private void initialize () {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }
        movesAvailable.clear();
        for (int i = 0; i < BOARD_WIDTH*BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        }
    }

    // Reset all the board properties to the base values, moveCount=0, gameOver=false, etc.
    void reset () {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }

    // Puts an X or O on the specified index dependent on player turn (Ex. (0,1) would be index 4)
    public boolean move (int index) {
        return move(index% BOARD_WIDTH, index/ BOARD_WIDTH);
    }

    // Put an X or O on the X,Y location and return true if the move has not been player prior
    private boolean move (int x, int y) {

        if (gameOver) {
            throw new IllegalStateException("Game over, no more moves can be played.");
        }
        if (board[y][x] == State.Blank) {
            board[y][x] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(y * BOARD_WIDTH + x);

        // THE GAME IS A DRAW IF THIS IF EVALUATES TO TRUE
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // CHECK THE BOARD TO DETERMINE A WINNER
        checkRow(y);
        checkColumn(x);
        checkDiagonalFromTopLeft(x, y);
        checkDiagonalFromTopRight(x, y);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    public boolean isGameOver () {
        return gameOver;
    }
    
    State[][] toArray () {
        return board.clone();
    }

    public State getTurn () {
        return playersTurn;
    }

    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    public HashSet<Integer> getAvailableMoves () {
        return movesAvailable;
    }

    private void checkRow (int row) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (board[row][i] != board[row][i-1]) {
                break;
            }
            if (i == BOARD_WIDTH -1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    private void checkColumn (int column) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (board[i][column] != board[i-1][column]) {
                break;
            }
            if (i == BOARD_WIDTH -1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    private void checkDiagonalFromTopLeft (int x, int y) {
        if (x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (board[i][i] != board[i-1][i-1]) {
                    break;
                }
                if (i == BOARD_WIDTH -1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    private void checkDiagonalFromTopRight (int x, int y) {
        if (BOARD_WIDTH -1-x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (board[BOARD_WIDTH -1-i][i] != board[BOARD_WIDTH -i][i-1]) {
                    break;
                }
                if (i == BOARD_WIDTH -1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    public Board getDeepCopy () {
        Board board             = new Board();

        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }

        board.playersTurn       = this.playersTurn;
        board.winner            = this.winner;
        board.movesAvailable    = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount         = this.moveCount;
        board.gameOver          = this.gameOver;
        return board;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {

                if (board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != BOARD_WIDTH -1) {
                sb.append("\n");
            }
        }

        return new String(sb);
    }

}
