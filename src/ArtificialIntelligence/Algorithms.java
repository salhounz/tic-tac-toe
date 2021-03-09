package ArtificialIntelligence;

import TicTacToe.Board;

/**
 ** Setup to include multiple algorithms as the class goes on as well as keep everything 
 ** in the same package to help compartmentalize the components that make up the program
 **/
public class Algorithms {
    private Algorithms() {}
    
    public static void alphaBetaPruning (Board board) {
        AlphaBetaPruning.run(board.getTurn(), board, Double.POSITIVE_INFINITY);
    }
    // Use the alphaBeta pruning algo while also keeping a depth limit (ply)
    public static void alphaBetaPruning (Board board, int ply) {
        AlphaBetaPruning.run(board.getTurn(), board, ply);
    }

    // Use the alphaBeta pruning algo while keeping a depth limit (ply) and 
    // include the depth in the evaluation 
    public static void alphaBetav2 (Board board) {
        AlphaBetaAdvanced.run(board.getTurn(), board, Double.POSITIVE_INFINITY);
    }

    // Use the alphaBeta algo while keeping the depth limit (ply) to use in the 
    // evaluation and also a depth limit 
    public static void alphaBetav2 (Board board, int ply) {
        AlphaBetaAdvanced.run(board.getTurn(), board, ply);
    }

}
