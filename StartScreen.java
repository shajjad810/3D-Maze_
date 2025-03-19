import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private JButton startButton;
    private JButton exitButton;
    
    public StartScreen() {
        setTitle("3D Maze Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(0, 0, 0);
                Color color2 = new Color(50, 50, 50);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Create title label
        titleLabel = new JLabel("3D MAZE GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 100, 800, 60);
        
        // Create welcome message
        welcomeLabel = new JLabel("Welcome to the 3D Maze Adventure!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(0, 200, 800, 30);
        
        // Create start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBounds(300, 300, 200, 50);
        startButton.setBackground(new Color(0, 150, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.addActionListener(e -> startGame());
        
        // Create exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBounds(300, 370, 200, 50);
        exitButton.setBackground(new Color(150, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        
        // Add hover effects
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(0, 180, 0));
                startButton.repaint();
            }
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(0, 150, 0));
                startButton.repaint();
            }
        });
        
        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(180, 0, 0));
                exitButton.repaint();
            }
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(150, 0, 0));
                exitButton.repaint();
            }
        });
        
        // Add components to panel
        mainPanel.add(titleLabel);
        mainPanel.add(welcomeLabel);
        mainPanel.add(startButton);
        mainPanel.add(exitButton);
        
        // Add panel to frame
        add(mainPanel);
    }
    
    private void startGame() {
        this.dispose(); // Close the start screen
        new Game().setVisible(true); // Start the game
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartScreen().setVisible(true);
        });
    }
} 