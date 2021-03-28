import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JPanel implements Runnable, MouseListener {
    final int SIZE = 400;

    boolean isRunning;
    Thread thread;

    BufferedImage view, tiles;
    int w = 32;
    int[][] grid;
    int[][] showGrid;
    MouseEvent mouse;
    int mouseX, mouseY;

    public Game() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        addMouseListener(this);
    }

    public static void main(String[] args) {
        JFrame w = new JFrame("Minesweeper");
        w.setResizable(false);
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.add(new Game());
        w.pack();
        w.setLocationRelativeTo(null);
        w.setVisible(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }

    public void start() {
        try {
            view = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
            tiles = ImageIO.read(getClass().getResource("/tiles.jpg"));
            grid = new int[12][12];
            showGrid = new int[12][12];

            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    showGrid[i][j] = 10;
                    if (new Random().nextInt(5) % 5 == 0) {
                        grid[i][j] = 9;
                    } else {
                        grid[i][j] = 0;
                    }
                }
            }

            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    int n = 0;
                    if (grid[i][j] == 9) {
                        continue;
                    }
                    if (grid[i + 1][j] == 9) {
                        n++;
                    }
                    if (grid[i][j + 1] == 9) {
                        n++;
                    }
                    if (grid[i - 1][j] == 9) {
                        n++;
                    }
                    if (grid[i][j - 1] == 9) {
                        n++;
                    }
                    if (grid[i + 1][j + 1] == 9) {
                        n++;
                    }
                    if (grid[i - 1][j - 1] == 9) {
                        n++;
                    }
                    if (grid[i - 1][j + 1] == 9) {
                        n++;
                    }
                    if (grid[i + 1][j - 1] == 9) {
                        n++;
                    }
                    grid[i][j] = n;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (mouse != null && mouse.getID() == MouseEvent.MOUSE_PRESSED) {
            mouseX = mouse.getX() / w;
            mouseY = mouse.getY() / w;
            if (mouse.getButton() == MouseEvent.BUTTON1) {
                showGrid[mouseX][mouseY] = grid[mouseX][mouseY];
            } else if (mouse.getButton() == MouseEvent.BUTTON3) {
                showGrid[mouseX][mouseY] = 11;
            }
            mouse = null;
        }
    }

    public void draw() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, SIZE, SIZE);

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (showGrid[mouseX][mouseY] == 9) {
                    showGrid[i][j] = grid[i][j];
                }
                BufferedImage subImage = tiles.getSubimage(showGrid[i][j] * w, 0, w, w);
                g2.drawImage(subImage, i * w, j * w, w, w, null);
            }
        }

        Graphics g = getGraphics();
        g.drawImage(view, 0, 0, SIZE, SIZE, null);
        g.dispose();
    }

    @Override
    public void run() {
        try {
            start();
            while (isRunning) {
                update();
                draw();
                Thread.sleep(1000 / 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouse = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}