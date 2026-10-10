import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeFrame extends JFrame
{
    private static final int ROW = 3;
    private static final int COL = 3;

    private TicTacToeTile[][] tiles = new TicTacToeTile[ROW][COL];
    private String[][] board = new String[ROW][COL];

    private JPanel mainPnl;
    private JPanel boardPnl;
    private JPanel controlPnl;

    private JLabel statusLbl;
    private JButton quitBtn;

    private String player = "X";
    private int moveCount = 0;

    private ActionListener moveListener;

    /**
     * Creates the Tic Tac Toe game window.
     */
    public TicTacToeFrame()
    {
        super("Tic Tac Toe");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout(10, 10));
        add(mainPnl);

        statusLbl = new JLabel("Player X's turn", JLabel.CENTER);
        statusLbl.setFont(new Font("Arial", Font.BOLD, 22));
        mainPnl.add(statusLbl, BorderLayout.NORTH);

        createBoardPanel();
        mainPnl.add(boardPnl, BorderLayout.CENTER);

        createControlPanel();
        mainPnl.add(controlPnl, BorderLayout.SOUTH);

        clearBoard();

        setVisible(true);
    }

    /**
     * Creates the 3 by 3 game board and its buttons.
     */
    private void createBoardPanel()
    {
        boardPnl = new JPanel();
        boardPnl.setLayout(new GridLayout(3, 3, 5, 5));

        moveListener = (ActionEvent ae) ->
        {
            TicTacToeTile clicked = (TicTacToeTile) ae.getSource();

            int row = clicked.getRow();
            int col = clicked.getCol();

            if (!board[row][col].equals(" "))
            {
                JOptionPane.showMessageDialog(
                        this,
                        "That square is already occupied. Choose another square.",
                        "Invalid Move",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            else
            {
                board[row][col] = player;
                clicked.setText(player);

                moveCount++;

                if (moveCount >= 5 && isWin(player))
                {
                    JOptionPane.showMessageDialog(
                            this,
                            "Player " + player + " wins!",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    askPlayAgain();
                }
                else if (moveCount >= 7 && isTie())
                {
                    JOptionPane.showMessageDialog(
                            this,
                            "The game is a tie!",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    askPlayAgain();
                }
                else
                {
                    if (player.equals("X"))
                    {
                        player = "O";
                    }
                    else
                    {
                        player = "X";
                    }

                    statusLbl.setText("Player " + player + "'s turn");
                }
            }
        };

        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                tiles[row][col] = new TicTacToeTile(row, col);
                tiles[row][col].setFont(
                        new Font("Arial", Font.BOLD, 50)
                );

                tiles[row][col].addActionListener(moveListener);

                boardPnl.add(tiles[row][col]);
            }
        }
    }

    /**
     * Creates the control panel containing the Quit button.
     */
    private void createControlPanel()
    {
        controlPnl = new JPanel();

        quitBtn = new JButton("Quit");

        quitBtn.addActionListener(ae ->
        {
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit?",
                    "Quit Game",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION)
            {
                dispose();
            }
        });

        controlPnl.add(quitBtn);
    }

    /**
     * Clears the board and starts a new game with player X.
     */
    private void clearBoard()
    {
        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                board[row][col] = " ";
                tiles[row][col].setText("");
            }
        }

        player = "X";
        moveCount = 0;

        statusLbl.setText("Player X's turn");
    }

    /**
     * Checks whether the specified player has won.
     *
     * @param player the player to check
     * @return true if the player has won
     */
    private boolean isWin(String player)
    {
        return isRowWin(player)
                || isColWin(player)
                || isDiagonalWin(player);
    }

    /**
     * Checks all rows for a win.
     *
     * @param player the player to check
     * @return true if a row contains three of the player's symbols
     */
    private boolean isRowWin(String player)
    {
        for (int row = 0; row < ROW; row++)
        {
            if (board[row][0].equals(player)
                    && board[row][1].equals(player)
                    && board[row][2].equals(player))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks all columns for a win.
     *
     * @param player the player to check
     * @return true if a column contains three of the player's symbols
     */
    private boolean isColWin(String player)
    {
        for (int col = 0; col < COL; col++)
        {
            if (board[0][col].equals(player)
                    && board[1][col].equals(player)
                    && board[2][col].equals(player))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks both diagonals for a win.
     *
     * @param player the player to check
     * @return true if a diagonal contains three of the player's symbols
     */
    private boolean isDiagonalWin(String player)
    {
        if (board[0][0].equals(player)
                && board[1][1].equals(player)
                && board[2][2].equals(player))
        {
            return true;
        }

        if (board[0][2].equals(player)
                && board[1][1].equals(player)
                && board[2][0].equals(player))
        {
            return true;
        }

        return false;
    }

    /**
     * Checks whether neither player can still complete a winning line.
     * This allows a tie to be detected before the board is full.
     *
     * @return true if no winning line remains possible
     */
    private boolean isTie()
    {
        // Check rows
        for (int row = 0; row < ROW; row++)
        {
            if (lineCanWin(
                    board[row][0],
                    board[row][1],
                    board[row][2]))
            {
                return false;
            }
        }

        // Check columns
        for (int col = 0; col < COL; col++)
        {
            if (lineCanWin(
                    board[0][col],
                    board[1][col],
                    board[2][col]))
            {
                return false;
            }
        }

        // Check diagonal from top-left to bottom-right
        if (lineCanWin(board[0][0], board[1][1], board[2][2]))
        {
            return false;
        }

        // Check diagonal from top-right to bottom-left
        if (lineCanWin(board[0][2], board[1][1], board[2][0]))
        {
            return false;
        }

        return true;
    }

    /**
     * Determines whether a line can still be completed by one player.
     *
     * @param first the first square
     * @param second the second square
     * @param third the third square
     * @return true if the line does not contain both X and O
     */
    private boolean lineCanWin(String first, String second, String third)
    {
        return !(first.equals("X") || second.equals("X") || third.equals("X"))
                || !(first.equals("O") || second.equals("O") || third.equals("O"));
    }

    /**
     * Asks whether the players want another game.
     */
    private void askPlayAgain()
    {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Would you like to play another game?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION)
        {
            clearBoard();
        }
        else
        {
            dispose();
        }
    }
}