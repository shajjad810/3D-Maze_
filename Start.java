import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start {
    private static int highestScore = 0; // Shared highest score

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Game");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Maze Game");
        title.setBounds(250, 50, 200, 30);
        frame.add(title);

        JButton newGameButton = new JButton("Start Game");
        newGameButton.setBounds(240, 120, 120, 40);
        frame.add(newGameButton);

        JLabel highestScoreLabel = new JLabel("Highest Score: " + highestScore);
        highestScoreLabel.setBounds(240, 180, 200, 30); // Adjust label position
        frame.add(highestScoreLabel);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(240, 240, 120, 40); // Adjust button position and size
        frame.add(exitButton);

        frame.setVisible(true);

        // Action Listeners
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close start screen
                Game game = new Game();
                game.setVisible(true);
                highestScore = game.getHighestScore(); // Update the highest score after the game
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
