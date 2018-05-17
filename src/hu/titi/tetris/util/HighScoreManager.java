package hu.titi.tetris.util;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {

    private static final int HIGHSCORE_COUNT = 10;
    private static final String DEFAULT_FILEPATH = "highscores.txt";

    public static class Record {
        private final long score;
        private final String name;
        private final LocalDate date;

        private Record(long score, String name, LocalDate date) {
            this.score = score;
            this.name = name;
            this.date = date;
        }

        private Record(long score, String name) {
            this(score, name, LocalDate.now());
        }

        /**
         * Rekord keletkezése.
         * @return rekord keletkezése
         */
        public LocalDate getDate() {
            return date;
        }

        /**
         * Rekord elérője.
         * @return rekord elérője
         */
        public String getName() {
            return name;
        }

        /**
         * Rekord pontszáma.
         * @return rekord pontszáma
         */
        public long getScore() {
            return score;
        }

        /**
         * Rekord táblázat sorként reprezentálva
         * @return sor reprezentáció
         */
        public String[] asRow() {
            return new String[] {name, Long.toString(score), date.toString()};
        }

        /**
         * Rekord szövegként reprezentálva.
         * @return szöveg reprezentáció
         */
        @Override
        public String toString() {
            return name + ";" + score + ";" + date.getYear() + ";" + date.getMonthValue() + ";" + date.getDayOfMonth();
        }
    }

    private final List<Record> highScores = new ArrayList<>(HIGHSCORE_COUNT);
    private final String filepath;

    public HighScoreManager() {
        this(DEFAULT_FILEPATH);
    }

    /**
     * Használja a megadott helyet a tárolásra.
     * @param filepath a tárolás helye
     */
    public HighScoreManager(String filepath) {
        this.filepath = filepath;
        loadHighScores();
    }

    /**
     * Pontszámrekord beszúrása a megfelelő helyre. (Ha van ilyen.)
     * @param name a játékos neve
     * @param score a pontszám
     */
    public void addHighScore(String name, long score) {
        boolean added = false;
        for (int i = 0; i < highScores.size(); i++) {
            if (highScores.get(i).getScore() < score) {
                highScores.add(i, new Record(score, name));
                while (highScores.size() > HIGHSCORE_COUNT) {
                    highScores.remove(HIGHSCORE_COUNT);
                }
                writeHighScores();
                added = true;
                break;
            }

        }
        if (!added && highScores.size() < HIGHSCORE_COUNT) {
            highScores.add(new Record(score, name));
            writeHighScores();
        }
    }

    /**
     * Megadja a ranglista elemeit. (Sorrendben)
     * @return a ranglista
     */
    public List<Record> getHighScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Megadja az i. rekordot.
     * @param i a rekord sorszáma
     * @return az i. rekord
     */
    public Record getScore(int i) {
        return highScores.get(i);
    }

    /**
     * Megadja a highscoret.
     * @return a highscore
     */
    public long getHighScore() {
        return highScores.size() > 0 ? highScores.get(0).getScore() : 0L;
    }

    /**
     * Megadja hogy adott pontszámmal fel lehet-e kerülni a ranglistára.
     * @param score a pontszám
     * @return fel lehet-e a pontszámmal kerülni
     */
    public boolean shouldAdd(long score) {
        return highScores.size() < HIGHSCORE_COUNT || highScores.stream().anyMatch(r -> r.getScore() < score);
    }

    /**
     * Betölti a ranglistát a fájlból.
     */
    private void loadHighScores() {
        if (new File(filepath).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                String line;
                for (int i = 0; i < HIGHSCORE_COUNT; i++) {
                    if ((line = br.readLine()) == null) {
                        break;
                    }

                    String[] data = line.trim().split(";");
                    if (data.length != 5) {
                        break;
                    }

                    String name = data[0];
                    int score = Integer.parseInt(data[1]);
                    int year = Integer.parseInt(data[2]);
                    int month = Integer.parseInt(data[3]);
                    int day = Integer.parseInt(data[4]);

                    highScores.add(i, new Record(score, name, LocalDate.of(year, month, day)));
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiírja a ranglistát a fájlba.
     */
    private void writeHighScores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Record r : highScores) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
