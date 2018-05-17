import hu.titi.tetris.util.HighScoreManager;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class HighScoreTest {

    private static final Random rand = new Random();

    private static String randomName(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('a' + rand.nextInt(26));
        }
        return sb.toString();
    }

    private static long[] intArray(List<Long> list) {
        long[] ret = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    /**
     * Teszteli hogy az adatok megmaradnak-e fájlbaírás-újraolvasás után
     */
    @Test
    public void testPermanency() {
        long[] scores = {4000, 500, 100, 0, -100};
        {
            HighScoreManager highScoreManager = new HighScoreManager("testfile");
            Arrays.stream(scores).forEach(l -> highScoreManager.addHighScore(randomName(10), l));
        }
        // Highscoremanager could be gc'd
        HighScoreManager highScoreManager = new HighScoreManager("testfile");
        assertArrayEquals(scores, intArray(highScoreManager.getHighScores().stream().map(HighScoreManager.Record::getScore).collect(Collectors.toList())));
    }

    /**
     * Teszteli hogy a dátum megfelelően állítódik-e be
     */
    @Test
    public void testDate() {
        HighScoreManager highScoreManager = new HighScoreManager("testfile");
        highScoreManager.addHighScore(randomName(10), 100_000);
        assertEquals(LocalDate.now(), highScoreManager.getHighScores().get(0).getDate());
    }

    /**
     * Teszteli hogy az adatok a megfeleló sorrendben kerülnek be
     */
    @Test
    public void testSorting() {
        List<Long> scores = IntStream.range(0, 10).mapToObj(i -> (long)rand.nextInt(10_000)).collect(Collectors.toList());

        HighScoreManager highScoreManager = new HighScoreManager("testfile");
        scores.stream().forEach(i -> highScoreManager.addHighScore(randomName(10), i));

        Collections.sort(scores);
        Collections.reverse(scores);

        assertArrayEquals(intArray(scores), intArray(highScoreManager.getHighScores().stream().map(HighScoreManager.Record::getScore).collect(Collectors.toList())));
    }

    @After
    public void deleteTemp() {
        File f = new File("testfile");
        if (f.exists()) {
            f.delete();
        }
    }

}
