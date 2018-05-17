package hu.titi.tetris.gui;

import hu.titi.tetris.game.Engine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static hu.titi.tetris.gui.MainWindow.getLabel;

class InfoPanel extends JPanel {

    private final Engine engine;
    private final ScorePanel scorePanel;
    private final JButton pauseButton;

    /**
     * Panel inicializálása.
     * @param engine a játékmotor
     */
    InfoPanel(Engine engine) {
        this.engine = engine;

        scorePanel = new ScorePanel(engine);
        setBackground(MainWindow.BACKGROUND_COLOR);
        NextPanel nextObject = new NextPanel(engine);

        setLayout(new GridBagLayout());

        JLabel nextLabel = getLabel("Next element:");
        nextLabel.setAlignmentX(CENTER_ALIGNMENT);
        Border margin = new EmptyBorder(15, 0, 0, 0);
        nextLabel.setBorder(new CompoundBorder(nextLabel.getBorder(), margin));

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(actionEvent -> engine.pause());
        pauseButton.setFocusable(false);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(actionEvent -> engine.save());
        saveButton.setFocusable(false);

        JPanel pausePanel = new JPanel();
        pausePanel.setBackground(MainWindow.BACKGROUND_COLOR);
        pausePanel.setLayout(new FlowLayout());
        pausePanel.add(pauseButton, BorderLayout.NORTH);
        pausePanel.add(saveButton, BorderLayout.CENTER);

        add(nextLabel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
        add(nextObject, new GridBagConstraints(0, 1, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(scorePanel, new GridBagConstraints(0, 2, 1, 1, 1, 0.8, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(pausePanel, new GridBagConstraints(0, 3, 1, 1, 1, 0.8, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * Pontszám kijelző frissítése.
     */
    void scoreChange() {
        scorePanel.scoreChange();
    }

    /**
     * Megállító gomb frissítése.
     */
    void stateChange() {
        switch (engine.getState()) {
            case RUNNING:
                pauseButton.setText("Pause");
                break;
            case PAUSED:
                pauseButton.setText("Resume");
                break;
            case FINISHED:
                pauseButton.setText("Restart");
                break;
            default: break;
        }
    }

    /**
     * Kért méret megadása.
     * @return a kért méret
     */
    @Override
    public Dimension getPreferredSize() {
        Container c = getParent();
        if (c == null) {
            return new Dimension(100, 100);
        }

        return new Dimension(c.getWidth(), c.getWidth());
    }

}
