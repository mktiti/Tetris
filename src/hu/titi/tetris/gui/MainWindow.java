package hu.titi.tetris.gui;

import hu.titi.tetris.game.Engine;
import hu.titi.tetris.game.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class MainWindow extends JFrame {

    static final Color BACKGROUND_COLOR = new Color(43, 43, 43);

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 900;

    private final InfoPanel infoPanel;
    private final GamePanel gamePanel;

    private final MenuFrame menu;
    private final Engine engine;

    MainWindow(MenuFrame menu) {
        this(menu, null);
    }

    /**
     * Korábbi mentés folytatása.
     * @param menu főmenü ablak
     * @param save a mentés
     */
    MainWindow(MenuFrame menu, Save save) {
        this.menu = menu;

        if (save == null) {
            engine = new Engine(this, this::redrawGame, this::redrawNext, this::scoreChange, this::stateChange);
        } else {
            engine = new Engine(this, this::redrawGame, this::redrawNext, this::scoreChange, this::stateChange, save);
        }

        infoPanel = new InfoPanel(engine);
        gamePanel = new GamePanel(engine);

        setTitle(".:Tetris:.");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        add(gamePanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(infoPanel, new GridBagConstraints(1, 0, 1, 1, 0.33, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        setBackground(BACKGROUND_COLOR);
        pack();
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                close();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        setVisible(true);
        engine.run();
    }

    /**
     * Következő elem kijelző frissítés kérés.
     */
    private void redrawNext() {
        infoPanel.repaint();
    }

    /**
     * Játéktábla felület frissítés kérés.
     */
    private void redrawGame() {
        gamePanel.repaint();
    }

    /**
     * Pontszám változás bejelentés.
     */
    private void scoreChange() {
        infoPanel.scoreChange();
    }

    /**
     * Játékállapot változás bejelentés.
     */
    private void stateChange() {
        infoPanel.stateChange();
    }

    /**
     * Standard beállítású (szín, betűszín, szövegméret) mező készítése.
     * @param value a mező szövege
     * @return az elkészült mező
     */
    static JLabel getLabel(String value) {
        JLabel label = new JLabel(value);
        label.setForeground(new Color(200, 200, 200));
        label.setFont(label.getFont().deriveFont(20f));
        return label;
    }

    /**
     * Kilépés kérése.
     */
    private void close() {
        if (engine.exit()) {
            dispose();
        }
    }

    /**
     * Ablak bezárása, főmenü megjelenítése.
     */
    @Override
    public void dispose() {
        menu.setVisible(true);
        super.dispose();
    }
}
