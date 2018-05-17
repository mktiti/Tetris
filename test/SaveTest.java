import hu.titi.tetris.game.*;
import hu.titi.tetris.util.SaveManager;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Random;

public class SaveTest {

    private static final Random rand = new Random();

    private static <T> T randelem(T[] array) {
        return array[rand.nextInt(array.length)];
    }

    /**
     * Teszteli hogy a SaveManager getDefaultDir-je tényleg a legutóbb használt könyvtárat adja-e vissza
     */
    @Test
    public void testDefaultDir() {
        GameObject go = new GameObject(randelem(TileColor.values()), randelem(Shape.values()), Coordinate.of(rand.nextInt(10), rand.nextInt(10)));
        GameObject next = new GameObject(randelem(TileColor.values()), randelem(Shape.values()), Coordinate.of(rand.nextInt(10), rand.nextInt(10)));
        Save save = new Save(new Field(10, 10), go, next, rand.nextLong());

        String home = System.getProperty("user.home");
        SaveManager.save(new File(home, "testfile"), save);
        assertEquals(home, SaveManager.getDefaultDir().getPath());
    }

    /**
     * Létrehoz egy random állapotot, elmenti és visszatölti, majd ellenőrzi az egyezést az eredetivel.
     */
    @Test
    public void testSave() {
        Field f = new Field(10, 10);
        GameObject go = new GameObject(randelem(TileColor.values()), randelem(Shape.values()), Coordinate.of(rand.nextInt(10), rand.nextInt(10)));
        GameObject next = new GameObject(randelem(TileColor.values()), randelem(Shape.values()), Coordinate.of(rand.nextInt(10), rand.nextInt(10)));
        long score = rand.nextLong();
        Save save = new Save(f, go, next, score);

        SaveManager.save(new File("testfile"), save);

        Save s = SaveManager.load(new File("testfile.tet"));
        assertEquals(score, s.getScore());
        assertEquals(go, s.getObject());
        assertEquals(next, s.getNext());
        assertEquals(f.getWidth(), s.getField().getWidth());
        assertEquals(f.getHeight(), s.getField().getHeight());
    }

    @After
    public void deleteTemp() {
        File f = new File("testfile.tet");
        if (f.exists()) {
            f.delete();
        }


        f = new File(System.getProperty("user.home"), "testfile.tet");
        if (f.exists()) {
            f.delete();
        }
    }

}
