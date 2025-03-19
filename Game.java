import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

public class Game extends JFrame implements Runnable {
    private int highestScore = 0; // Track the highest score
    private int score = 0; // Track the current score
    private int currentLevel = 1; // Start at level 1
    public int mapWidth = 15;
    public int mapHeight = 15;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public ArrayList<Texture> textures;
    public Player Player;
    public Processor screen;

    public int getHighestScore() {
        return highestScore;
    }

    private void setupMenuBar() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLightWeightPopupEnabled(false);
        popupMenu.setBackground(new Color(40, 40, 40));
        popupMenu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        popupMenu.setPreferredSize(new Dimension(150, 90));

        JMenuItem instructionsItem = new JMenuItem("Instructions");
        instructionsItem.setBackground(new Color(50, 50, 50));
        instructionsItem.setForeground(Color.WHITE);
        instructionsItem.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionsItem.setOpaque(true);
        instructionsItem.setBorderPainted(false);
        instructionsItem.addActionListener(e -> showInstructions());

        JMenuItem restartItem = new JMenuItem("Restart Game");
        restartItem.setBackground(new Color(50, 50, 50));
        restartItem.setForeground(Color.WHITE);
        restartItem.setFont(new Font("Arial", Font.PLAIN, 12));
        restartItem.setOpaque(true);
        restartItem.setBorderPainted(false);
        restartItem.addActionListener(e -> restartGame());

        JMenuItem quitItem = new JMenuItem("Exit Game");
        quitItem.setBackground(new Color(50, 50, 50));
        quitItem.setForeground(Color.WHITE);
        quitItem.setFont(new Font("Arial", Font.PLAIN, 12));
        quitItem.setOpaque(true);
        quitItem.setBorderPainted(false);
        quitItem.addActionListener(e -> System.exit(0));

        popupMenu.add(instructionsItem);
        popupMenu.add(restartItem);
        popupMenu.addSeparator();
        popupMenu.add(quitItem);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int windowWidth = getWidth();
                int windowHeight = getHeight();
                Insets insets = getInsets();
                int menuButtonWidth = Math.min(80, windowWidth / 10);
                int menuButtonHeight = Math.min(30, windowHeight / 20);
                int adjustedX = 10 + insets.left;
                int adjustedY = 10 + insets.top;
                if (e.getX() >= adjustedX && e.getX() <= adjustedX + menuButtonWidth &&
                        e.getY() >= adjustedY && e.getY() <= adjustedY + menuButtonHeight) {
                    popupMenu.show(Game.this, adjustedX, adjustedY + menuButtonHeight);
                }
            }
        });
    }

    // Show instructions in a dialog
    private void showInstructions() {
        JFrame instructionsFrame = new JFrame("Game Instructions");
        instructionsFrame.setSize(400, 300);
        instructionsFrame.setLocationRelativeTo(this);
        instructionsFrame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea(
                "3D Maze Game Instructions:\n\n" +
                        "- Use arrow keys to move around the maze\n" +
                        "- Up/Down: Move forward/backward\n" +
                        "- Left/Right: Turn left/right\n" +
                        "- Find the goal to advance to the next level\n" +
                        "- Each level completed gives you 10 points\n" +
                        "- Press F5 to reset position\n" +
                        "- Press F6 to skip to next level (cheat)");
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        panel.add(textArea, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> instructionsFrame.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        instructionsFrame.add(panel);
        instructionsFrame.setVisible(true);
    }

    // Restart the game
    private void restartGame() {
        dispose(); // Close the current game window
        new Game(); // Start a new game
    }

    public static int[][] map = {
            { 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 6, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 0, 10, 10, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
    };

    public static int[][] map2 = {
            { 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
            { 6, 0, 0, 0, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 },
            { 5, 0, 9, 0, 0, 9, 0, 9, 9, 9, 9, 0, 0, 0, 0, 5 },
            { 5, 9, 0, 9, 0, 9, 0, 9, 9, 0, 0, 0, 0, 0, 0, 5 },
            { 5, 0, 0, 0, 0, 9, 0, 9, 9, 0, 9, 0, 0, 0, 0, 5 },
            { 5, 0, 9, 0, 9, 0, 0, 9, 0, 9, 9, 9, 9, 9, 0, 5 },
            { 5, 0, 9, 9, 9, 9, 9, 0, 9, 0, 9, 0, 0, 0, 0, 5 },
            { 5, 0, 0, 0, 9, 0, 0, 9, 0, 0, 0, 0, 9, 0, 0, 5 },
            { 5, 0, 0, 0, 9, 0, 0, 9, 0, 9, 9, 0, 9, 9, 0, 5 },
            { 5, 0, 9, 0, 9, 0, 0, 9, 0, 9, 9, 0, 0, 9, 0, 5 },
            { 5, 0, 9, 9, 9, 0, 0, 9, 0, 0, 0, 0, 0, 9, 0, 5 },
            { 5, 0, 0, 0, 9, 0, 0, 9, 0, 9, 0, 9, 0, 0, 0, 5 },
            { 5, 0, 9, 0, 9, 9, 9, 9, 0, 9, 0, 9, 9, 9, 0, 5 },
            { 5, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 5 },
            { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 }
    };

    public static int[][] map3 = {
            { 2, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
            { 6, 0, 8, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 2, 0, 8, 0, 8, 8, 0, 8, 0, 8, 8, 8, 0, 0, 0, 2 },
            { 2, 0, 8, 8, 8, 8, 0, 8, 0, 0, 0, 8, 0, 0, 0, 2 },
            { 2, 0, 0, 0, 8, 9, 0, 8, 0, 8, 0, 8, 8, 8, 0, 2 },
            { 2, 8, 8, 0, 8, 8, 0, 8, 0, 8, 0, 0, 0, 8, 0, 2 },
            { 2, 0, 0, 0, 8, 8, 0, 8, 0, 8, 0, 8, 0, 0, 0, 2 },
            { 2, 0, 8, 8, 8, 8, 0, 8, 8, 8, 0, 8, 0, 8, 0, 2 },
            { 2, 0, 8, 0, 0, 0, 0, 0, 0, 8, 8, 8, 0, 0, 0, 2 },
            { 2, 0, 8, 0, 8, 8, 8, 8, 0, 8, 0, 0, 0, 8, 0, 2 },
            { 2, 0, 8, 0, 0, 0, 0, 8, 0, 8, 0, 8, 8, 8, 0, 2 },
            { 2, 0, 8, 8, 8, 8, 0, 8, 0, 8, 0, 0, 0, 0, 0, 2 },
            { 2, 0, 0, 0, 0, 8, 0, 8, 0, 8, 8, 8, 8, 8, 0, 2 },
            { 2, 0, 8, 8, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }
    };

    public static int[][] map4 = {
            { 4, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 },
            { 6, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
            { 4, 0, 0, 7, 0, 7, 7, 7, 0, 7, 7, 7, 0, 0, 0, 4 },
            { 4, 0, 0, 7, 0, 0, 0, 7, 0, 0, 0, 7, 0, 0, 0, 4 },
            { 4, 7, 0, 7, 0, 7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 4 },
            { 4, 0, 0, 7, 0, 7, 0, 7, 0, 0, 0, 7, 0, 0, 0, 4 },
            { 4, 0, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 7, 7, 0, 4 },
            { 4, 0, 7, 7, 0, 7, 0, 7, 0, 0, 0, 7, 0, 0, 0, 4 },
            { 4, 0, 0, 7, 0, 7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 4 },
            { 4, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 7, 0, 0, 0, 4 },
            { 4, 7, 0, 7, 0, 0, 7, 7, 7, 7, 7, 7, 7, 7, 0, 4 },
            { 4, 0, 0, 7, 7, 0, 7, 0, 0, 0, 0, 7, 0, 0, 0, 4 },
            { 4, 0, 7, 7, 7, 0, 7, 0, 7, 7, 7, 7, 0, 7, 7, 4 },
            { 4, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 }
    };

    public static int[][] map5 = {
            { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 2 },
            { 8, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 2 },
            { 8, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 11, 0, 0, 2 },
            { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
            { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 }
    };

    public Game() {
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        textures = new ArrayList<Texture>();
        textures.add(Texture.city);
        textures.add(Texture.violet);
        textures.add(Texture.brick1);
        textures.add(Texture.brick2);
        textures.add(Texture.brick3);
        textures.add(Texture.goal);
        textures.add(Texture.diamond);
        textures.add(Texture.iron);
        textures.add(Texture.dirt);
        textures.add(Texture.trump);
        textures.add(Texture.win1);
        textures.add(Texture.win2);
        Player = new Player(1.5, 14, 1, 0, 0, -.66);
        screen = new Processor(map, mapWidth, mapHeight, textures, 640, 480);
        addKeyListener(Player);

        // Set initial frame size
        setSize(800, 600);
        setMinimumSize(new Dimension(640, 480));
        setResizable(true);
        setTitle("Mazes in Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);

        // Setup popup menu instead of menu bar
        setupMenuBar();

        // Make the frame visible first
        setVisible(true);

        // Create buffer strategy after the frame is visible
        createBufferStrategy(3);

        // Start the game
        start();
    }

    private synchronized void start() { // starts in sync
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        Graphics g = bs.getDrawGraphics();

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, windowWidth, windowHeight);

        g.drawImage(image, 0, 0, windowWidth, windowHeight, null);

        Insets insets = getInsets();
        int menuWidth = Math.min(80, windowWidth / 10);
        int menuHeight = Math.min(30, windowHeight / 20);
        int menuX = 10 + insets.left;
        int menuY = 10 + insets.top;

        int scoreBoxWidth = 120;
        int scoreBoxHeight = 75;
        int scoreBoxX = 20;
        int scoreBoxY = menuY + menuHeight + 5;

        g.setColor(new Color(40, 40, 40, 200));
        g.fillRect(scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight);
        g.setColor(Color.GRAY);
        g.drawRect(scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight);

        int fontSize = 14;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        int textX = scoreBoxX + 10;
        g.drawString("Level: " + currentLevel, textX, scoreBoxY + 20);
        g.drawString("Score: " + score, textX, scoreBoxY + 40);
        g.drawString("High: " + highestScore, textX, scoreBoxY + 60);

        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(menuX, menuY, menuWidth, menuHeight);
        g.setColor(Color.WHITE);
        int menuFontSize = Math.min(18, Math.max(12, windowWidth / 45));
        g.setFont(new Font("Arial", Font.BOLD, menuFontSize));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth("MENU");
        int textXMenu = menuX + (menuWidth - textWidth) / 2;
        int textYMenu = menuY + ((menuHeight - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString("MENU", textXMenu, textYMenu);
        g.setColor(Color.GRAY);
        g.drawRect(menuX, menuY, menuWidth, menuHeight);

        g.dispose();
        bs.show();
    }

    private void update() {
        if (Player.changeMap2 && currentLevel == 1) {
            map = map2;
            currentLevel = 2;
            score += 10; // Increment score
        }
        if (Player.changeMap3 && currentLevel == 2) {
            map = map3;
            currentLevel = 3;
            score += 10; // Increment score
        }
        if (Player.changeMap4 && currentLevel == 3) {
            map = map4;
            currentLevel = 4;
            score += 10; // Increment score
        }
        if (Player.changeMap5 && currentLevel == 4) {
            map = map5;
            currentLevel = 5;
            score += 10; // Increment score
        }
        if (score > highestScore) {
            highestScore = score; // Update highest score
        }
        screen.update(Player, pixels);
        Player.update(map);
    }

    private void control() {
        requestFocus();
    }

    public void run() {
        while (running) {
            update();
            render();
            control();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
    }
}

class Texture {
    public int[] pixels; // hold data in image
    private String loc;
    public final int SIZE; // always 64 x 64 for square

    public Texture(String location, int size) {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Texture city = new Texture("Texture//c.png", 64);
    public static Texture brick1 = new Texture("Texture//brick1.png", 64);
    public static Texture brick2 = new Texture("Texture//brick2.png", 64);
    public static Texture brick3 = new Texture("Texture//brick3.png", 64);
    public static Texture violet = new Texture("Texture//b2.png", 64);
    public static Texture goal = new Texture("Texture//go.png", 64);
    public static Texture diamond = new Texture("Texture//diamond.png", 64);
    public static Texture dirt = new Texture("Texture//dirt.png", 64);
    public static Texture ore = new Texture("Texture//dore.png", 64);
    public static Texture iron = new Texture("Texture//iron.png", 64);
    public static Texture trump = new Texture("Texture//trump.png", 64);
    public static Texture win1 = new Texture("Texture//win1.png", 64);
    public static Texture win2 = new Texture("Texture//win2.png", 64);
}

class Player implements KeyListener {
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    public boolean left, right, forward, back;
    public final double movementSpeed = .008; // Reduced from .015 to .008 for much slower movement
    public final double rotateSpeed = .004; // Reduced from .008 to .004 for much slower rotation
    public boolean changeMap2 = false;
    public boolean changeMap3 = false;
    public boolean changeMap4 = false;
    public boolean changeMap5 = false;

    // Add movement smoothing
    private double currentSpeed = 0;
    private double targetSpeed = 0;
    private final double acceleration = 0.0002; // Reduced from 0.0005 for smoother acceleration
    private final double deceleration = 0.0004; // Reduced from 0.001 for smoother deceleration

    // Add rotation smoothing
    private double currentRotation = 0;
    private double targetRotation = 0;
    private final double rotationAcceleration = 0.0001; // Reduced from 0.0003
    private final double rotationDeceleration = 0.0002; // Reduced from 0.0006

    public Player(double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }

    public void keyPressed(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;

        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;

        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;

        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;

        if ((key.getKeyCode() == KeyEvent.VK_F5)) {
            resetPos();
        }

        if ((key.getKeyCode() == KeyEvent.VK_F6)) {
            nextMap();
        }

        if ((key.getKeyCode() == KeyEvent.VK_F1)) {
            System.out.println(xPos + "-" + yPos);
        }
    }

    public void keyReleased(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }

    public void update(int[][] map) {
        if ((xPos > 1.013765758832589 && xPos < 1.724263794075187)
                && (yPos > 1.013765758832589 && yPos < 1.724263794075187)) {
            nextMap();
            resetPos();
        }

        // Smooth movement speed
        if (forward) {
            targetSpeed = movementSpeed;
        } else if (back) {
            targetSpeed = -movementSpeed;
        } else {
            targetSpeed = 0;
        }

        // Gradually adjust current speed
        if (currentSpeed < targetSpeed) {
            currentSpeed = Math.min(currentSpeed + acceleration, targetSpeed);
        } else if (currentSpeed > targetSpeed) {
            currentSpeed = Math.max(currentSpeed - deceleration, targetSpeed);
        }

        // Apply movement with current speed
        if (currentSpeed != 0) {
            double nextX = xPos + xDir * currentSpeed;
            double nextY = yPos + yDir * currentSpeed;

            // Check if next position is valid
            if (map[(int) nextX][(int) yPos] == 0) {
                xPos = nextX;
            }
            if (map[(int) xPos][(int) nextY] == 0) {
                yPos = nextY;
            }
        }

        // Smooth rotation
        if (right) {
            targetRotation = -rotateSpeed;
        } else if (left) {
            targetRotation = rotateSpeed;
        } else {
            targetRotation = 0;
        }

        // Gradually adjust current rotation
        if (currentRotation < targetRotation) {
            currentRotation = Math.min(currentRotation + rotationAcceleration, targetRotation);
        } else if (currentRotation > targetRotation) {
            currentRotation = Math.max(currentRotation - rotationDeceleration, targetRotation);
        }

        // Apply rotation with current rotation speed
        if (currentRotation != 0) {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(currentRotation) - yDir * Math.sin(currentRotation);
            yDir = oldxDir * Math.sin(currentRotation) + yDir * Math.cos(currentRotation);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(currentRotation) - yPlane * Math.sin(currentRotation);
            yPlane = oldxPlane * Math.sin(currentRotation) + yPlane * Math.cos(currentRotation);
        }
    }

    public void keyTyped(KeyEvent e) {
        // difference between keyTyped and keyPressed?
    }

    public void resetPos() {
        xPos = 1.5;
        yPos = 14;
        xDir = 1;
        yDir = 0;
        xPlane = 0;
        yPlane = -.66;
        currentSpeed = 0;
        targetSpeed = 0;
        currentRotation = 0;
        targetRotation = 0;
    }

    public void nextMap() {
        if (changeMap4)
            changeMap5 = true;
        if (changeMap3)
            changeMap4 = true;
        if (changeMap2)
            changeMap3 = true;
        changeMap2 = true;
    }
}

class Processor {
    public int[][] map;
    public int mapWidth, mapHeight, width, height;
    public ArrayList<Texture> textures;

    public Processor(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
    }

    public int[] update(Player Player, int[] pixels) {
        if (Player.changeMap2) {
            map = Game.map2;
        }
        if (Player.changeMap3) {
            map = Game.map3;
        }
        if (Player.changeMap4) {
            map = Game.map4;
        }
        if (Player.changeMap5) {
            map = Game.map5;
        }

        // Initialize background
        for (int n = 0; n < pixels.length / 2; n++) {
            if (pixels[n] != Color.DARK_GRAY.getRGB())
                pixels[n] = Color.DARK_GRAY.getRGB();
        }

        for (int i = pixels.length / 2; i < pixels.length; i++) {
            if (pixels[i] != Color.gray.getRGB())
                pixels[i] = Color.gray.getRGB();
        }

        for (int x = 0; x < width; x++) {
            // Calculate ray direction
            double PlayerX = 2 * x / (double) (width) - 1;
            double rayDirX = Player.xDir + Player.xPlane * PlayerX;
            double rayDirY = Player.yDir + Player.yPlane * PlayerX;

            // Map position
            int mapX = (int) Player.xPos;
            int mapY = (int) Player.yPos;

            // Side distance
            double sideDistX;
            double sideDistY;
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist;
            int stepX, stepY;
            boolean hit = false;
            int side = 0;

            // Calculate step and initial side distance
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (Player.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - Player.xPos) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (Player.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - Player.yPos) * deltaDistY;
            }

            // Perform DDA with bounds checking
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                // Check bounds
                if (mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight) {
                    hit = true;
                    break;
                }

                if (map[mapX][mapY] > 0) {
                    hit = true;
                }
            }

            // Calculate distance to wall
            if (side == 0) {
                perpWallDist = Math.abs((mapX - Player.xPos + (1 - stepX) / 2) / rayDirX);
            } else {
                perpWallDist = Math.abs((mapY - Player.yPos + (1 - stepY) / 2) / rayDirY);
            }

            // Calculate line height
            int lineHeight;
            if (perpWallDist > 0) {
                lineHeight = Math.abs((int) (height / perpWallDist));
            } else {
                lineHeight = height;
            }

            // Calculate drawing boundaries
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) {
                drawStart = 0;
            }

            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) {
                drawEnd = height - 1;
            }

            // Get texture with bounds checking
            int texNum = map[mapX][mapY] - 1;
            if (texNum < 0 || texNum >= textures.size()) {
                continue;
            }

            // Calculate texture coordinates
            double wallX;
            if (side == 1) {
                wallX = (Player.xPos + ((mapY - Player.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
            } else {
                wallX = (Player.yPos + ((mapX - Player.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }

            wallX -= Math.floor(wallX);
            int texX = (int) (wallX * (textures.get(texNum).SIZE));

            if (side == 0 && rayDirX > 0) {
                texX = textures.get(texNum).SIZE - texX - 1;
            }
            if (side == 1 && rayDirY < 0) {
                texX = textures.get(texNum).SIZE - texX - 1;
            }

            // Draw the vertical line with bounds checking
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
                if (texY < 0 || texY >= textures.get(texNum).SIZE) {
                    continue;
                }

                int color;
                if (side == 0) {
                    color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                } else {
                    color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711;
                }

                int pixelIndex = x + y * width;
                if (pixelIndex >= 0 && pixelIndex < pixels.length) {
                    pixels[pixelIndex] = color;
                }
            }
        }
        return pixels;
    }
}