package hu.titi.tetris.gui;

import hu.titi.tetris.game.Engine;

import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel {

    private Engine engine;

    /**
     * Játékpanel inicializálás.
     * @param engine a játékmotor
     */
    GamePanel(Engine engine) {
        this.engine = engine;

        setFocusable(true);
        addKeyListener(engine);
        setBackground(MainWindow.BACKGROUND_COLOR);
        setVisible(true);
    }

    /**
     * Tábla újrarajzolás.
     * @param graphics
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        engine.draw(graphics, getWidth(), getHeight());
    }
}
