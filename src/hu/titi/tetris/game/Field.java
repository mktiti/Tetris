package hu.titi.tetris.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Field implements Serializable {

    private final int width;
    private final int height;

    private final ArrayList<ArrayList<TileColor>> cells;

    private final Coordinate start;

    /**
     * Üres, adott méretű tábla létrehozása
     * @param width a tábla szélessége
     * @param height a tábla magassága
     */
    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        cells = new ArrayList<>(height);
        for (int y = 0; y < height; y++) {
            ArrayList<TileColor> row = new ArrayList<>(width);
            cells.add(y, row);
            for (int x = 0; x < width; x++) {
                row.add(x, null);
            }
        }

        start = Coordinate.of(width / 2, 0);
    }

    /**
     * Megmondja hogy adott pozíció a táblán van-e, és ha igen akkor szabad-e
     * @param c pozíció
     * @return c pozíció a táblán van-e, és szabad-e
     */
    boolean freeValid(Coordinate c) {
        return valid(c) && !isFilled(c.x, c.y);
    }

    /**
     * Megadja hogy a pozíció a táblán van-e
     * @param c pozíció
     * @return c pozíció a pályán van-e
     */
    private boolean valid(Coordinate c) {
        return c.x < width && c.x >= 0 && c.y < height;
    }

    /**
     * Megadja, hogy adott pozíció foglalt-e
     * @param x x pozíció
     * @param y y pozíció
     * @return fogalalt-e a pozíció
     */
    private boolean isFilled(int x, int y) {
        return getColor(x, y).isPresent();
    }

    /**
     * A tábla színe egy adott pontban
     * @param x x pozíció
     * @param y y pozíció
     * @return a pozícióban lévő szín (opcionálisan, ha van ott elem)
     */
    Optional<TileColor> getColor(int x, int y) {
        if (y < 0) {
            return Optional.empty();
        } else {
            TileColor tc = cells.get(y).get(x);
            return tc == null ? Optional.empty() : Optional.of(tc);
        }
    }

    /**
     * Objektum másolása a táblára
     * @param object az objektum
     * @return a törölt sorok száma
     */
    public int save(GameObject object) {
        TileColor color = object.getColor();
        object.coords().stream().filter(c -> c.y >= 0).forEach((c) -> cells.get(c.y).set(c.x, color));

        return checkRows();
    }

    /**
     * Törölhető sorok törlése
     * @return a törölt sorok száma
     */
    private int checkRows() {
        int removed = 0;

        for (int y = 0; y < height; y++) {
            boolean full = true;
            ArrayList<TileColor> row = cells.get(y);
            for (int x = 0; x < width; x++) {
                if (row.get(x) == null) {
                    full = false;
                    break;
                }
            }
            if (full) {
                deleteRow(y);
                removed++;
            }
        }
        return removed;
    }

    /**
     * Sor törlése
     * @param row a törölni való sor indexe
     */
    private void deleteRow(int row) {
        cells.remove(row);
        ArrayList<TileColor> newRow = new ArrayList<>(width);
        cells.add(0, newRow);
        for (int x = 0; x < width; x++) {
            newRow.add(x, null);
        }
    }

    /**
     * A tbla kezdőkoordinátáját adja meg
     * @return a pálya koordinátája
     */
    Coordinate startPos() {
        return start;
    }

    /**
     * Magasság
     * @return a tábla magassága
     */
    public int getHeight() {
        return height;
    }

    /**
     * Szélesség
     * @return a tábla szélessége
     */
    public int getWidth() {
        return width;
    }
}
