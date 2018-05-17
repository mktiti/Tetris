package hu.titi.tetris.gui;

import hu.titi.tetris.util.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class HighScoreFrame extends JFrame {

    private final MenuFrame menu;

    HighScoreFrame(MenuFrame menu) {
        this.menu = menu;

        List<HighScoreManager.Record> records = new HighScoreManager().getHighScores();
        String[][] values = new String[records.size()][3];
        for (int i = 0; i < records.size(); i++) {
            values[i] = records.get(i).asRow();
        }

        setTitle("Highscores");
        JTable table = new JTable(values, new String[] {"Name", "Score", "Date"});
        table.setDefaultEditor(Object.class, null);
        table.setFillsViewportHeight(true);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        setMinimumSize(new Dimension(300, 200));
        //setSize(300, 450);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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
