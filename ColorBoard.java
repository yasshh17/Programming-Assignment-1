import javax.swing.*;
import java.awt.*;

public class ColorBoard extends JFrame {

    public ColorBoard(char[][] board) {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(board.length, board[0].length)); // Set grid layout based on board size

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                panel.setBackground(getColorFromChar(board[i][j]));
                add(panel);
            }
        }
    }

    private Color getColorFromChar(char colorChar) {
        switch (colorChar) {
            case 'R':
                return Color.RED;
            case 'G':
                return Color.GREEN;
            case 'B':
                return Color.BLUE;
            case 'Y':
                return Color.YELLOW;
            case 'W':
                return Color.WHITE;
            case 'g':
                return Color.GRAY;
            case 'X':
                return Color.BLACK;
            default:
                return Color.WHITE;
        }
    }
}