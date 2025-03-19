import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Texture {
    public int[] pixels;
    private String loc;
    public final int SIZE;

    public static Texture city = new Texture("Texture/c.png", 64);
    public static Texture violet = new Texture("Texture/b2.png", 64);
    public static Texture brick1 = new Texture("Texture/brick1.png", 64);
    public static Texture brick2 = new Texture("Texture/brick2.png", 64);
    public static Texture brick3 = new Texture("Texture/brick3.png", 64);
    public static Texture goal = new Texture("Texture/go.png", 64);
    public static Texture diamond = new Texture("Texture/diamond.png", 64);
    public static Texture iron = new Texture("Texture/iron.png", 64);
    public static Texture dirt = new Texture("Texture/dirt.png", 64);
    public static Texture trump = new Texture("Texture/trump.png", 64);
    public static Texture win1 = new Texture("Texture/win1.png", 64);
    public static Texture win2 = new Texture("Texture/win2.png", 64);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 