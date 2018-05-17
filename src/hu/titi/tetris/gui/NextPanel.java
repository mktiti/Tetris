package hu.titi.tetris.gui;

import hu.titi.tetris.game.Engine;

import javax.swing.*;
import java.awt.*;

class NextPanel extends JPanel {

    private final Engine engine;

    /**
     * Panel inicializálás.
     * @param engine a játékmotor
     */
    NextPanel(Engine engine) {
        this.engine = engine;
        setBackground(MainWindow.BACKGROUND_COLOR);
    }

    /**
     * Panel újrarajzolás.
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        engine.drawNextShape(graphics, getWidth(), getHeight());
    }
}
