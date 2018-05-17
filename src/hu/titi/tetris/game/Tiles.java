package hu.titi.tetris.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Tiles implements Serializable {

    // Relatives
    private final Coordinate a;
    private final Coordinate b;
    private final Coordinate c;

    /**
     * Lemásol egy másik formát.
     * @param tiles a másolandó formula
     */
    Tiles(Tiles tiles) {
        a = tiles.a;
        b = tiles.b;
        c = tiles.c;
    }

    Tiles(Coordinate a, Coordinate b, Coordinate c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Megadja, hogy mennyivel kell lefelé mozgatni a formát hogy ne lógjon ki a pályáról (felül).
     * @return a szükséges mozgatás mértéke
     */
    int lowerToFit() {
        return Math.max(0, 0 - coords(Coordinate.of(0, 0)).stream().mapToInt((c) -> c.y).min().orElse(0));
    }

    /**
     * Megadja, hogy adott tábla adott pozícióján van-e hely a formának.
     * @param field a tábla
     * @param position a pozíció
     * @return van-e elég hely
     */
    boolean isFreeValidAt(Field field, Coordinate position) {
        return coords(position).stream().allMatch(field::freeValid);
    }

    /**
     * Elforgatja a formát 90 fokkal.
     * @return a forgatott forma
     */
    public Tiles rotate() {
        return new Tiles(rotateCoord(a), rotateCoord(b), rotateCoord(c));
    }

    /**
     * Koordináta-pár elforgatása 90 fokkal. Segédmetódus a rotatehez.
     * @param c a forgatandó koordináta-pár
     * @return az elforgatott koordináta-pár
     */
    private Coordinate rotateCoord(Coordinate c) {
        return Coordinate.of(-c.y, c.x);
    }

    /**
     * Megadja a forma abszolút koordinátáit adott helyen.
     * @param position a hely
     * @return a koordináták
     */
    List<Coordinate> coords(Coordinate position) {
        return Arrays.asList(position, position.add(a), position.add(b), position.add(c));
    }

    /**
     * A forma hash kódja
     * @return a hash kód
     */
    @Override
    public int hashCode() {
        int temp = 17;
        temp = temp * 31 + a.hashCode();
        temp = temp * 31 + b.hashCode();
        return temp * 31 + c.hashCode();
    }

    /**
     * A formák egyenlőek-e
     * @param o összehasonlítandó
     * @return egyenlőek-e
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tiles)) {
            return false;
        }

        Tiles t = (Tiles)o;
        return a.equals(t.a) && b.equals(t.b) && c.equals(t.c);
    }
}
