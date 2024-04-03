import java.awt.Image;

import javax.swing.ImageIcon;

public class Player {
    Game game;
    double x, y, height, width, speedY, flapSpeed, collisionX, collisionY, collisionRadious, energy, maxEnergy,
            minEnergy, barSize;
    int spriteWidth, spriteHeight, frameY, buttomGap;
    boolean collided, charging;
    Image image;

    Player(Game game) {
        this.game = game;
        spriteWidth = 200;
        spriteHeight = 200;
        width = spriteWidth * game.ratio / 2;
        height = spriteHeight * game.ratio / 2;
        x = 0;
        y = game.height * 0.5 - height * 0.5;
        speedY = -5;
        flapSpeed = 5 * 0.65;
        collisionRadious = 40 * game.ratio;
        collisionX = x + width * 0.5;
        collisionY = y + height * 0.5;
        collided = false;
        barSize = Math.floor(5 * game.ratio);
        frameY = 0;
        charging = false;
        energy = 30;
        maxEnergy = energy * 2;
        minEnergy = 15;
        buttomGap = 35;
        image = new ImageIcon(getClass().getResource("./assets/images/player_fish.png")).getImage();
    }

    void draw() {
        game.ctx.drawImage(
                image, 0, (int) y, (int) width, (int) height + (int) y, 0, frameY * spriteHeight, spriteWidth,
                spriteHeight + frameY
                        * spriteHeight,
                null);
        // game.ctx.drawOval((int) collisionX, (int) collisionY, (int) collisionRadious,
        // (int) collisionRadious);
    }

    void update() {
        handleEnergy();
        y -= speedY;
        this.collisionY = y + height * 0.30;
        if (this.speedY <= 0)
            this.wingsUp();
        if (!isTouchingBottom()) {
            speedY -= game.gravity;

        } else {
            speedY = 0;
        }
        if (this.isTouchingBottom()) {
            this.y = game.height - this.height - buttomGap;
            wingsIdle();
        }
    }

    void flap() {
        if (!isTouchingTop()) {
            speedY = flapSpeed;
            wingsDown();
        }
    }

    boolean isTouchingBottom() {
        return y >= game.height - height - buttomGap;
    }

    boolean isTouchingTop() {
        return y <= 0;
    }

    void wingsIdle() {
        this.frameY = 0;
    }

    void wingsDown() {
        if (!this.charging)
            this.frameY = 1;
    }

    void wingsUp() {
        if (!this.charging)
            this.frameY = 2;
    }

    void wingsCharge() {
        this.frameY = 3;
    }

    void startCharging() {
        charging = true;
        game.speed = game.maxSpeed;
        wingsCharge();
    }

    void stopCharging() {
        charging = false;
        game.speed = game.minSpeed;
        wingsIdle();
    }

    void handleEnergy() {
        if (energy < maxEnergy) {
            energy += 0.25;
        }
        if (charging) {
            energy -= 2;
            if (energy <= 0) {
                energy = 0;
                stopCharging();
            }
        }
    }

}
