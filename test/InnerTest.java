import com.sun.org.apache.regexp.internal.RE;
import hu.titi.tetris.game.*;
import org.junit.After;
import org.junit.Test;

import javax.swing.*;
import java.util.Random;

import static org.junit.Assert.*;

public class InnerTest {

    private static final Random rand = new Random();

    /**
     * Teszteli az Engine állapotváltoztatását (megállítás/újrakezdés)
     */
    @Test
    public void stopTest() {
        Engine e = new Engine(new JFrame(), () -> {;}, () -> {;}, () -> {;}, () -> {;});
        e.run();
        e.pause();
        assertEquals(Engine.State.PAUSED, e.getState());
        e.pause();
        assertEquals(Engine.State.RUNNING, e.getState());
    }

    /**
     * Teszteli hogy egy négyzet pontosan 15 helyen lehet egy 6*4es pályán
     */
    @Test
    public void testValidPositions() {
        Field f = new Field(6, 4);
        int sum = 0;
        GameObject go = new GameObject(TileColor.RED, Shape.O, Coordinate.of(0, 0));
        for (int x = -2; x <= 8; x++) {
            for (int y = 0; y <= 6; y++) {
                if (go.setPosition(Coordinate.of(x, y)).isValid(f)) {
                    sum++;
                }
            }
        }
        assertEquals(15, sum);
    }

    /**
     * Teszteli hogy egy 20 magas pálya pontosan 10 négyzettől telik-e meg
     */
    @Test
    public void tenToFill() {
        Field f = new Field(5, 20);
        for (int i = 0; i < 10; i++) {
            GameObject block = new GameObject(TileColor.RED, Shape.O, Coordinate.of(0, 0));
            assertEquals(true, block.isValid(f));
            block.drop(f);
            assertEquals(0, f.save(block));
        }
        GameObject block = new GameObject(TileColor.RED, Shape.O, Coordinate.of(0, 0));
        block.drop(f);
        assertEquals(false, block.isValid(f));
    }

    /**
     * Teszteli hogy egy 2 széles táblára dobva egy négyzet egyből eeltűnik-e, és ha igen 2 sor tölődését jelni a tábla.
     */
    @Test
    public void singleBlockRows() {
        Field f = new Field(2, 10);
        GameObject block = new GameObject(TileColor.RED, Shape.O, Coordinate.of(0, 0));
        block.drop(f);
        assertEquals(2, f.save(block));
    }

    /**
     * Teszteli hogy mindegyik alakzat körbeforgatva önmaga lesz.
     */
    @Test
    public void rotateTest() {
        for (Shape s : Shape.values()) {
            Tiles t = s.baseTiles();
            for (int i = 0; i < 4; i++) {
                t = t.rotate();
            }
            assertEquals(s.baseTiles(), t);
        }
    }

}
