package connectfour;

/**
 *
 * @author fulwah alyahya
 */
import java.util.ArrayList;

public class GameEngine {

    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int EMPTY = 0;
    public static final int YELLOW = 1;
    public static final int RED = 2;

    public static int[][] initializeBoard() {
        return new int[ROWS][COLS];
    }

    public static void displayBoard(int[][] board) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static boolean isValidMove(int[][] board, int col) {
        if (col < 0 || col >= COLS) {
            return false;
        }

        return board[0][col] == EMPTY;
    }

    public static boolean dropPiece(int[][] board, int col, int player) {
        if (player != YELLOW && player != RED) {
            return false;
        }

        if (!isValidMove(board, col)) {
            return false;
        }

        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Integer> getLegalMoves(int[][] board) {
        ArrayList<Integer> legalMoves = new ArrayList<>();

        for (int col = 0; col < COLS; col++) {
            if (isValidMove(board, col)) {
                legalMoves.add(col);
            }
        }

        return legalMoves;
    }

    public static boolean checkWin(int[][] board, int player) {
    if (player != YELLOW && player != RED) {
        return false;
    }

    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {

            // Skip cells that do not belong to this player.
            if (board[row][col] != player) {
                continue;
            }

            // Horizontal: to the right.
            if (col + 3 < COLS
                    && board[row][col + 1] == player
                    && board[row][col + 2] == player
                    && board[row][col + 3] == player) {
                return true;
            }

            // Vertical: downward.
            if (row + 3 < ROWS
                    && board[row + 1][col] == player
                    && board[row + 2][col] == player
                    && board[row + 3][col] == player) {
                return true;
            }

            // Diagonal: downward and to the right.
            if (row + 3 < ROWS && col + 3 < COLS
                    && board[row + 1][col + 1] == player
                    && board[row + 2][col + 2] == player
                    && board[row + 3][col + 3] == player) {
                return true;
            }

            // Diagonal: downward and to the left.
            if (row + 3 < ROWS && col - 3 >= 0
                    && board[row + 1][col - 1] == player
                    && board[row + 2][col - 2] == player
                    && board[row + 3][col - 3] == player) {
                return true;
            }
        }
    }

    return false;
}
    public static boolean isDraw(int[][] board) {
    if (checkWin(board, YELLOW) || checkWin(board, RED)) {
        return false;
    }

    return getLegalMoves(board).isEmpty();
}
    
    
    ////=== main method  
    public static void main(String[] args) {
         int[][] board = {
        {1, 1, 2, 2, 1, 1, 2},
        {2, 2, 1, 1, 2, 2, 1},
        {1, 1, 2, 2, 1, 1, 2},
        {2, 2, 1, 1, 2, 2, 1},
        {1, 1, 2, 2, 1, 1, 2},
        {2, 2, 1, 1, 2, 2, 1}
    };

    displayBoard(board);

    System.out.println("Yellow wins: " + checkWin(board, YELLOW));
    System.out.println("Red wins: " + checkWin(board, RED));
    System.out.println("Draw: " + isDraw(board));
    }
    }


