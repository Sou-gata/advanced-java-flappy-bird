import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Obstacle {
    Game game;
    int spriteWidth, spriteHeight, x, frameX;
    double scaleWidth, scaleHeight, y, collisionX, collisionY, collisionRadious, speedY, angle, rotationSpeed;
    boolean markedForDeletion;
    Image image;

    Obstacle(Game game, int x) {
        this.game = game;
        this.spriteWidth = 120;
        spriteHeight = 120;
        scaleWidth = spriteWidth * game.ratio * 0.6;
        scaleHeight = spriteHeight * game.ratio * 0.6;
        this.x = x;
        y = Math.random() * (game.height - scaleHeight - 75);
        collisionX = this.x + scaleWidth * 0.5;
        collisionY = y + scaleHeight * 0.5;
        collisionRadious = scaleWidth * 0.35;
        speedY = (Math.random() < 0.5 ? -1 : 1) * game.ratio * (Math.random() + 1);
        markedForDeletion = false;
        frameX = (int) Math.floor(Math.random() * 4);
        angle = 0;
        rotationSpeed = (Math.random() * 0.5 + 0.5) * game.ratio * (Math.random() < 0.5 ? -1 : 1);
        image = new ImageIcon(getClass().getResource("./assets/images/smallGears.png")).getImage();
    }

    void update() {
        angle += rotationSpeed;
        x -= game.speed;
        y += speedY * 2;
        collisionX = x + scaleWidth * 0.5;
        collisionY = y + scaleHeight * 0.5;
        if (!game.gameOver) {
            if (y <= 0 || y >= (game.height - scaleHeight) - 60) {
                speedY *= -1;
            }
        } else {
            speedY += 0.1;
        }
        if (isOffScreenHorizontally() || isOffScreenVertically()) {
            markedForDeletion = true;
            game.obstacles.removeIf(obs -> obs.markedForDeletion);
            if (isOffScreenHorizontally()) {
                game.score += 10;
            }
        }
        if (game.obstacles.size() == 0) {
            game.gameOver = true;
        }
        if (game.checkCollision(game.player, this)) {
            game.gameOver = true;
            game.player.collided = true;
            game.player.stopCharging();
        }
    }

    void draw() {
        Graphics2D g2d = (Graphics2D) game.ctx.create();
        g2d.rotate(angle * 0.1, x + scaleWidth / 2, y + scaleHeight / 2);
        g2d.drawImage(image,
                (int) x,
                (int) y,
                (int) x + (int) scaleWidth,
                (int) y + (int) scaleHeight,
                frameX * spriteWidth,
                0,
                frameX * spriteWidth + spriteWidth,
                spriteHeight,
                null);
        g2d.dispose();
    }

    boolean isOffScreenHorizontally() {
        return x + scaleWidth < 0;
    }

    boolean isOffScreenVertically() {
        return y > game.height;
    }

}
