package hu.titi.tetris.gui;

import hu.titi.tetris.game.Save;
import hu.titi.tetris.util.SaveManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MenuFrame extends JFrame {

    private MenuFrame() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JButton gameButton = new JButton("New game");
        JButton loadButton = new JButton("Load game");
        JButton scoreboardButton = new JButton("Show highscores");
        JButton exitButton = new JButton("Exit");

        gameButton.addActionListener(e -> {setVisible(false); new MainWindow(this);});
        loadButton.addActionListener(e -> loadGame());
        scoreboardButton.addActionListener(e -> {setVisible(false); new HighScoreFrame(this);});
        exitButton.addActionListener(e -> System.exit(0));

        setLayout(new GridBagLayout());
        add(gameButton, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        add(loadButton, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        add(scoreboardButton, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        add(exitButton, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setTitle("Tetris");
        setResizable(false);

        setVisible(true);
    }

    /**
     * Játék betöltése és indítása. (Mentés megnyitás ablak jelenik meg.)
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Tetris saves", "tet");
        fileChooser.setFileFilter(filter);
        fileChooser.setApproveButtonText("Save");
        fileChooser.setDialogTitle("Save game");
        fileChooser.setCurrentDirectory(SaveManager.getDefaultDir());

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (f.exists()) {
                Save save = SaveManager.load(f);
                if (save != null) {
                    setVisible(false);
                    new MainWindow(this, save);
                }
            }
        }
    }

    /**
     * A  program indítsa. (Főmenü megjelenítése.)
     * @param args parancssori argumentumok
     */
    public static void main(String[] args) {
        new MenuFrame();
    }

}
