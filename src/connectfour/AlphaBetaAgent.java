/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectfour;

/**
 *
 * @author shath
 */
public class AlphaBetaAgent {

    private final int maxDepth;
    private final int aiPlayer;
    private final int opponentPlayer;
    private long nodesExpanded;

    public AlphaBetaAgent(int maxDepth, int aiPlayer) {
        this.maxDepth = maxDepth;
        this.aiPlayer = aiPlayer;
        this.opponentPlayer = (aiPlayer == GameEngine.YELLOW) ? GameEngine.RED : GameEngine.YELLOW;
        this.nodesExpanded = 0;
    }

    private void UndoMove(int[][] board, int col) {
        for (int row = 0; row < GameEngine.ROWS; row++) {
            if (board[row][col] != GameEngine.EMPTY) {
                board[row][col] = GameEngine.EMPTY;
                return;
            }
        }
    }

    public long getNodesExpanded() {
        return nodesExpanded;
    }

    public void resetNodesExpanded() {
        nodesExpanded = 0;
    }

    public int evaluateBoard(int[][] board) {
        int score = 0;
        int centerCol = GameEngine.COLS / 2;
        int centerCount = 0;
        for (int row = 0; row < GameEngine.ROWS; row++) {
            if (board[row][centerCol] == aiPlayer) {
                centerCount++;
            }
        }
        score += centerCount * 6;

        for (int row = 0; row < GameEngine.ROWS; row++) {
            for (int col = 0; col <= GameEngine.COLS - 4; col++) {
                int[] window = {
                    board[row][col],
                    board[row][col + 1],
                    board[row][col + 2],
                    board[row][col + 3]
                };
                score += evaluateWindow(window);
            }
        }

        for (int col = 0; col < GameEngine.COLS; col++) {
            for (int row = 0; row <= GameEngine.ROWS - 4; row++) {
                int[] window = {board[row][col],
                    board[row + 1][col],
                    board[row + 2][col],
                    board[row + 3][col]};
                score += evaluateWindow(window);
            }
        }

        for (int row = 0; row <= GameEngine.ROWS - 4; row++) {
            for (int col = 0; col <= GameEngine.COLS - 4; col++) {
                int[] window = {board[row][col],
                    board[row + 1][col + 1],
                    board[row + 2][col + 2],
                    board[row + 3][col + 3]
                };
                score += evaluateWindow(window);
            }
        }

        for (int row = 0; row <= GameEngine.ROWS - 4; row++) {
            for (int col = 3; col < GameEngine.COLS; col++) {
                int[] window = {board[row][col],
                    board[row + 1][col - 1],
                    board[row + 2][col - 2],
                    board[row + 3][col - 3]
                };
                score += evaluateWindow(window);
            }
        }
        return score;
    }

    private int evaluateWindow(int[] window) {
        int aiCount = 0;
        int oppCount = 0;
        int emptyCount = 0;

        for (int cell : window) {
            if (cell == aiPlayer) {
                aiCount++;
            } else if (cell == opponentPlayer) {
                oppCount++;
            } else {
                emptyCount++;
            }
        }
        if (aiCount == 3 && emptyCount == 1) {
            return 100;
        } else if (aiCount == 2 && emptyCount == 2) {
            return 10;
        } else if (oppCount == 3 && emptyCount == 1) {
            return -80;
        }
        return 0;

    }

    public int alphaBeta(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        nodesExpanded++;

        boolean aiWon = GameEngine.checkWin(board, aiPlayer);
        boolean oppWon = GameEngine.checkWin(board, opponentPlayer);
        boolean isDraw = GameEngine.isDraw(board);

        if (aiWon) {
            return 1000000 + depth;
        }
        if (oppWon) {
            return -1000000 - depth;
        }
        if (isDraw) {
            return 0;
        }
        if (depth == 0) {
            return evaluateBoard(board);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < GameEngine.COLS; col++) {
                if (GameEngine.isValidMove(board, col)) {
                    if (GameEngine.dropPiece(board, col, aiPlayer)) {
                        int eval = alphaBeta(board, depth - 1, alpha, beta, false);
                        UndoMove(board, col);
                        if (eval > maxEval) {
                            maxEval = eval;
                        }
                        if (maxEval > alpha) {
                            alpha = maxEval;
                        }
                        if (alpha >= beta) {
                            break;
                        }

                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < GameEngine.COLS; col++) {
                if (GameEngine.isValidMove(board, col)) {
                    if (GameEngine.dropPiece(board, col, opponentPlayer)) {
                        int eval = alphaBeta(board, depth - 1, alpha, beta, true);
                        UndoMove(board, col);

                        if (eval < minEval) {
                            minEval = eval;
                        }
                        if (minEval < beta) {
                            beta = minEval;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
            return minEval;
        }
    }

    public int getBestMove(int[][] board) {
        return getBestMove(board, this.maxDepth);
    }
    public int getBestMove(int[][] board , int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        for (int col = 0; col < GameEngine.COLS; col++) {
            if (GameEngine.isValidMove(board, col)) {
                if (GameEngine.dropPiece(board, col, aiPlayer)) {
                    int score = alphaBeta(board,depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    UndoMove(board, col);
                    if (bestCol == -1 || score > bestScore) {
                        bestScore = score;
                        bestCol = col;
                    }
                }
            }
        }
        return bestCol;

    }
}
