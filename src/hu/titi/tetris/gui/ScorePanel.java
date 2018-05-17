package hu.titi.tetris.gui;

import hu.titi.tetris.game.Engine;

import javax.swing.*;
import java.awt.*;

import static hu.titi.tetris.gui.MainWindow.getLabel;

class ScorePanel extends JPanel {

    private final Engine engine;
    private final JLabel scoreLabel;
    private final JLabel highScoreLabel;

    /**
     * Panel inicializálás.
     * @param engine a játékmotor
     */
    ScorePanel(Engine engine) {
        this.engine = engine;
        scoreLabel = getLabel(Long.toString(engine.getScore()));
        highScoreLabel = getLabel(Long.toString(engine.getHighScore()));

        setBackground(MainWindow.BACKGROUND_COLOR);

        setLayout(new GridBagLayout());
        add(getLabel("Score:  "), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
        add(scoreLabel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(getLabel("Highscore:  "), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
        add(highScoreLabel, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * Pontszámváltozás kezelése.
     */
    void scoreChange() {
        scoreLabel.setText(Long.toString(engine.getScore()));
        highScoreLabel.setText(Long.toString(engine.getHighScore()));
    }

}
