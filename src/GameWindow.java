import javafx.application.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GameWindow extends JFrame {
    private static GameWindow gameWindow;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image monkey;
    private static float monkeyXLeft = 600;
    private static float monkeyYTop = -200;
    private static float monkeyV = 200;
    private static int score = 0;


    public static void main(String[] args) throws Exception {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("GameBackground.png"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("GameOver.png"));
        monkey = ImageIO.read(GameWindow.class.getResourceAsStream("Monkey.png"));
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(100, 10);
        gameWindow.setSize(1080, 648);
        gameWindow.setResizable(false);
        lastFrameTime = System.nanoTime();
        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float monkeyXRight = monkeyXLeft + monkey.getWidth(null);
                float monkeyYBottom = monkeyYTop + monkey.getHeight(null);
                boolean isClickOnDrop = x >= monkeyXLeft && x <= monkeyXRight &&
                        y >= monkeyYTop && y <= monkeyYBottom;
                if (isClickOnDrop) {
                    monkeyYTop = -200;
                    monkeyXLeft = (int) (Math.random() * (gameField.getWidth() - monkey.getWidth(null)));
                    monkeyV = monkeyV + 20;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }
            }
        });
        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }

    private static void onRepaint (Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;
        monkeyYTop = monkeyYTop + monkeyV * deltaTime;

        g.drawImage(background, 0, 0, null);
        g.drawImage(monkey, (int)monkeyXLeft, (int)monkeyYTop, null);
        if (monkeyYTop > gameWindow.getHeight()) {
            g.drawImage(gameOver, 360, 108, null);
        }
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
