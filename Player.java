import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
    public double x, y, xd, yd, xa, za;
    public boolean left, right, forward, back;
    public boolean changeMap2 = false;
    public boolean changeMap3 = false;
    public boolean changeMap4 = false;
    public boolean changeMap5 = false;

    public Player(double x, double y, double xd, double yd, double xa, double za) {
        this.x = x;
        this.y = y;
        this.xd = xd;
        this.yd = yd;
        this.xa = xa;
        this.za = za;
    }

    public void update(int[][] map) {
        double rotSpeed = 0.03;
        double moveSpeed = 0.05;
        double newX = x;
        double newY = y;

        if (right) {
            double oldxd = xd;
            xd = xd * Math.cos(-rotSpeed) - yd * Math.sin(-rotSpeed);
            yd = oldxd * Math.sin(-rotSpeed) + yd * Math.cos(-rotSpeed);
            double oldxa = xa;
            xa = xa * Math.cos(-rotSpeed) - za * Math.sin(-rotSpeed);
            za = oldxa * Math.sin(-rotSpeed) + za * Math.cos(-rotSpeed);
        }
        if (left) {
            double oldxd = xd;
            xd = xd * Math.cos(rotSpeed) - yd * Math.sin(rotSpeed);
            yd = oldxd * Math.sin(rotSpeed) + yd * Math.cos(rotSpeed);
            double oldxa = xa;
            xa = xa * Math.cos(rotSpeed) - za * Math.sin(rotSpeed);
            za = oldxa * Math.sin(rotSpeed) + za * Math.cos(rotSpeed);
        }
        if (forward) {
            newX = x + xd * moveSpeed;
            newY = y + yd * moveSpeed;
        }
        if (back) {
            newX = x - xd * moveSpeed;
            newY = y - yd * moveSpeed;
        }

        int xBlock = (int)(newX);
        int yBlock = (int)(newY);

        if (map[xBlock][yBlock] == 0) {
            x = newX;
            y = newY;
        }

        // Check for goal
        if (map[xBlock][yBlock] == 6) {
            if (!changeMap2 && !changeMap3 && !changeMap4 && !changeMap5) {
                changeMap2 = true;
                x = 1.5;
                y = 14;
            } else if (changeMap2 && !changeMap3 && !changeMap4 && !changeMap5) {
                changeMap3 = true;
                x = 1.5;
                y = 14;
            } else if (changeMap2 && changeMap3 && !changeMap4 && !changeMap5) {
                changeMap4 = true;
                x = 1.5;
                y = 14;
            } else if (changeMap2 && changeMap3 && changeMap4 && !changeMap5) {
                changeMap5 = true;
                x = 1.5;
                y = 14;
            }
        }
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

    public void keyTyped(KeyEvent key) {
    }
} 