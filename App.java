import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game game = new Game();
        frame.setSize(game.width, game.height);
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setLocationRelativeTo(null);
        game.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                    game.player.wingsUp();
                    game.isFlying = false;
                } else if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_SHIFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    game.isBoosted = false;
                    game.player.stopCharging();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    game.restart();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!game.isFlying) {
                        game.player.flap();
                        game.isFlying = true;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_SHIFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (!game.isBoosted) {
                        game.isBoosted = true;
                        game.player.startCharging();
                    }
                }
            }
        });
        frame.setVisible(true);
    }
}