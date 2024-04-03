
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Background extends JPanel {
    Image background;
    Game game;
    int width, height;
    double scaleWidth, scaleHeight, x;

    Background(Game game) {
        this.game = game;
        this.width = 2400;
        this.height = 720;
        this.background = new ImageIcon(getClass().getResource("./assets/images/background_single.png")).getImage();
        this.scaleWidth = (double) this.width * game.ratio;
        this.scaleHeight = (double) this.height * game.ratio;
    }

    void update() {
        this.x += game.speed;
        if (x >= scaleWidth)
            x = 0.0;
    }

    public void draw() {
        game.ctx.drawImage(this.background, 0, 0, game.width, game.height, (int) x, 0, (int) scaleWidth + (int) x,
                height, null);
        game.ctx.drawImage(background, 0, 0, game.width, game.height, (int) x - (int) scaleWidth, 0, (int) x, height,
                null);
    }
}
