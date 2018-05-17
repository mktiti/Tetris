package hu.titi.tetris.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Coordinate implements Serializable {

    final int x;
    final int y;

    private final int hash;

    public enum Direction {
        Left(-1, 0), Right(1, 0), Down(0, 1);

        final int x;
        final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final int COMMONS_BORD = 2;
    private static final List<Coordinate> COMMONS = new ArrayList<>(25);

    /*
      létrehozza a gyakori (relatív) koordináta-párokat
     */
    static {
        for (int x = -COMMONS_BORD; x <= COMMONS_BORD; x++) {
            for (int y = -COMMONS_BORD; y <= COMMONS_BORD; y++) {
                COMMONS.add(new Coordinate(x, y));
            }
        }
    }

    /**
     * Visszadja az x, y-hoz tartozó koordinátát
     * @param x a koordináta x váétozója
     * @param y a korrdináta y változója
     * @return a koordináta
     */
    public static Coordinate of(int x, int y) {
        if (x <= COMMONS_BORD && x >= -COMMONS_BORD && y <= COMMONS_BORD && y >= -COMMONS_BORD) {
            return COMMONS.get((x + COMMONS_BORD) * (2 * COMMONS_BORD + 1) + (y + COMMONS_BORD));
        }
        return new Coordinate(x, y);
    }

    /**
     * Új koordináta-pár, csak az osztály használhatja
     * @param x x koordináta
     * @param y y koordináta
     */
    private Coordinate(int x, int y) {
        this.x = x;
        this.y = y;

        int temp = 17;
        temp = temp * 31 + x;
        hash = temp * 31 + y;
    }

    /**
     * Koordináta-pár eltolása adott irányba
     * @param direction az eltolás iránya
     * @return az új koordináta
     */
    Coordinate move(Direction direction) {
        return of(x + direction.x, y + direction.y);
    }

    /**
     * Koordináta-pár eltolása
     * @param c az eltolás
     * @return az új koordináta
     */
    Coordinate add(Coordinate c) {
        return of(x + c.x, y + c.y);
    }

    /**
     * hash kód
     * @return a koordináta-pár hash kódja
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * szöveg reprezentáció
     * @return a koordináta-pár szöveg reprezentációja
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * A koordináta-párok egyenlőek-e
     * @param o objektum
     * @return o koorináta-pár-e, és ha igen akkor koordinátái megegyeznek-e ezen páréval
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Coordinate && ((Coordinate)o).x == x && ((Coordinate)o).y == y;
    }

    /**
     * Y koordináta
     * @return y koordináta
     */
    int getY() {
        return y;
    }

    /**
     * X koordináta
     * @return x koordináta
     */
    int getX() {
        return x;
    }
}
