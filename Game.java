import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;

public class Game extends JPanel {
    int width, height, baseHeight, numberOfObstacles, eventInterval;
    double ratio, gravity, speed, maxSpeed, minSpeed, score, timer, eventTimer, eventIntervel;
    Background background;
    Player player;
    ArrayList<Obstacle> obstacles;
    boolean gameOver, eventUpdate, isBoosted, isFlying;
    ArrayList<String> message;
    long lastTime, timeStamp;

    int touchStartX;
    int swipeDistance;
    Graphics ctx;

    Timer gameLoop;

    Font font;

    Game() {

        this.width = 1000;
        this.height = 300;
        this.baseHeight = 300;
        this.ratio = (double) this.height / (double) this.baseHeight;
        this.background = new Background(this);
        this.player = new Player(this);
        this.obstacles = new ArrayList<Obstacle>();
        this.numberOfObstacles = 10;
        this.gravity = 0.15;
        this.speed = 6;
        this.minSpeed = this.speed;
        this.maxSpeed = 4 * this.speed;
        this.score = 0;
        this.timer = 0;
        this.eventTimer = 0;
        this.eventInterval = 150;
        this.eventUpdate = true;
        this.touchStartX = 0;
        this.swipeDistance = 50;
        this.message = new ArrayList<String>();
        this.gameOver = false;
        this.message.add("");
        this.message.add("");
        this.message.add("Press 'R' to restart");
        this.isBoosted = false;
        this.isFlying = false;
        this.timeStamp = Calendar.getInstance().getTimeInMillis();
        this.lastTime = this.timeStamp;

        createObstacles();

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("./assets/font/Bungee-Regular.ttf"));
        } catch (FileNotFoundException e) {
            font = new Font("Arial", Font.PLAIN, 20);
            e.printStackTrace();
        } catch (FontFormatException e) {
            font = new Font("Arial", Font.PLAIN, 20);
            e.printStackTrace();
        } catch (IOException e) {
            font = new Font("Arial", Font.PLAIN, 20);
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(this.width, this.height));
        setFocusable(true);

        gameLoop = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        gameLoop.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.ctx = g;
        timeStamp = Calendar.getInstance().getTimeInMillis();
        int deltaTime = (int) (timeStamp - lastTime);
        lastTime = timeStamp;
        draw(deltaTime);
    }

    void draw(int deltaTime) {
        if (!gameOver) {
            timer += deltaTime;
        }
        handlePeriodicEvents(deltaTime);
        background.draw();
        background.update();
        player.draw();
        player.update();
        drawStatusText();
        int obstaclesSize = obstacles.size();
        try {
            for (int i = 0; i < obstacles.size(); i++) {
                obstacles.get(i).update();
                if (obstaclesSize >= 1) {
                    obstacles.get(i).draw();
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

    }

    void drawStatusText() {
        ctx.setFont(font.deriveFont(20f));
        ctx.drawString("Score: " + (int) score, width - 150, 25);
        ctx.drawString("Timer: " + formatTimer(), 10, 25);
        drawBoostBar();
        ctx.setColor(Color.WHITE);
        ctx.setFont(font.deriveFont(17f));
        ctx.drawString("Energy: " + (int) player.energy, 10, height - 10);
        if (gameOver) {
            if (player.collided) {
                message.set(0, "Getting rusty? Try again!");
                message.set(1, "Collision time " + formatTimer() + " seconds");
            } else {
                message.set(0, "Nailed it!");
                message.set(1, "Can you do it faster then " + formatTimer() + " seconds ?");
            }

            ctx.setFont(font.deriveFont(35f));
            ctx.setColor(new Color(0, 0, 0));
            verticalCenterText(message.get(0), height / 2 - 30);
            ctx.setFont(font.deriveFont(20f));
            verticalCenterText(message.get(1), height / 2);
            verticalCenterText(message.get(2), height / 2 + 30);
        }
    }

    void verticalCenterText(String text, int y) {
        int x = (width - ctx.getFontMetrics().stringWidth(text)) / 2;
        ctx.drawString(text, x, y);
    }

    void drawBoostBar() {
        if (player.energy <= 20) {
            ctx.setColor(Color.RED);
        } else if (player.energy <= 40) {
            ctx.setColor(Color.ORANGE);
        } else {
            ctx.setColor(Color.GREEN);
        }
        for (int i = 0; i < player.energy; i++) {
            ctx.fillRect(120 + i * 10, height - 20, 8, 10);
        }
    }

    void handlePeriodicEvents(int deltaTime) {
        if (eventTimer < eventInterval) {
            eventTimer += deltaTime;
            eventUpdate = false;
        } else {
            eventTimer = eventTimer % eventInterval;
            eventUpdate = true;
        }
    }

    String formatTimer() {
        return String.format("%.1f", timer * 0.001);
    }

    void createObstacles() {
        obstacles.clear();
        int firstX = (int) ((3 * width / 4) * ratio);
        int obstacleSpacing = (int) (600 * ratio);
        for (int i = 0; i < numberOfObstacles; i++) {
            obstacles.add(new Obstacle(this, firstX + i * obstacleSpacing));
        }
    }

    boolean checkCollision(Player a, Obstacle b) {
        double dx = a.collisionX - b.collisionX;
        double dy = a.collisionY - b.collisionY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < a.collisionRadious + b.collisionRadious;
    }

    void restart() {
        gameOver = false;
        player = new Player(this);
        obstacles.clear();
        createObstacles();
        score = 0;
        timer = 0;
        System.gc();
    }
}