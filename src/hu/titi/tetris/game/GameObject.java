package hu.titi.tetris.game;

import java.io.Serializable;
import java.util.List;

public class GameObject implements Serializable {

    private final TileColor color;
    private final Shape shape;
    private Tiles form;
    private Coordinate position;

    /**
     * Lemásol egy másik alakzatot.
     * @param object a másolandó alakzat
     */
    GameObject(GameObject object) {
        color = object.color;
        shape = object.shape;
        form = new Tiles(object.form);
        position = object.position;
    }

    public GameObject(TileColor color, Shape shape, Coordinate position) {
        this.color = color;
        this.shape = shape;

        form = shape.baseTiles();
        this.position = position;
    }

    /**
     * Lemozgatja az objektumot hogy az ne lógjon ki
     */
    void moveDownToFit() {
        position = position.add(Coordinate.of(0, form.lowerToFit()));
    }

    /**
     * Megadja, hogy adott táblán van-e hely az alakzatnak.
     * @param field a tábla
     * @return van-e hely
     */
    public boolean isValid(Field field) {
        return form.isFreeValidAt(field, position);
    }

    /**
     * Mozgatja az alakzatot a megadott irányba.
     * @param field a tábla
     * @param direction a mozgatás iránya
     * @return sikerült-e a mozgatás (volt-e hely)
     */
    public boolean move(Field field, Coordinate.Direction direction) {
        Coordinate newPos = position.move(direction);
        boolean canMove = form.isFreeValidAt(field, newPos);

        if (canMove) {
            position = newPos;
        }
        return canMove;
//        return canMove && !(direction == Coordinate.Direction.Down && !form.isValidAt(field, position.move(Coordinate.Direction.Down)));
    }

    /**
     * Leejti az alakzatot. (Önmagát adja vissza)
     * @param field a tábla
     * @return önmaga
     */
    public GameObject drop(Field field) {
        while (move(field, Coordinate.Direction.Down)) ;

        return this;
    }

    /**
     * Elforgatja az alakzatot 90 fokkal.
     * @param field a tábla
     */
    public void rotate(Field field) {
        if (shape != Shape.O) {
            Tiles newForm = form.rotate();
            if (newForm.isFreeValidAt(field, position)) {
                form = newForm;
            }
        }
    }

    /**
     * Megadja az alakzat abszolút koordinátáit.
     * @return a koordináták
     */
    List<Coordinate> coords() {
        return form.coords(position);
    }

    /**
     * Megadja az alakzat színét.
     * @return az alakzat színe
     */
    TileColor getColor() {
        return color;
    }

    /**
     * Beállítja az alakzat pozícióját.
     * @param position az új pozíció
     * @return az alakzat
     */
    public GameObject setPosition(Coordinate position) {
        this.position = position;
        return this;
    }

    /**
     * Visszatér az objektum egy leginkább sarokban lévő másával. (A „következő elem” kijelzéséhez.)
     * @return a mozgatott objektum
     */
    GameObject smallImage() {
        List<Coordinate> coords = coords();
        int minX = coords.stream().mapToInt(Coordinate::getX).min().orElse(0);
        int minY = coords.stream().mapToInt(Coordinate::getY).min().orElse(0);

        return new GameObject(this).setPosition(position.add(Coordinate.of(-minX, -minY)));
    }

    /**
     * Megadja az alakzat szélességét.
     * @return szélesség
     */
    int getWidth() {
        List<Coordinate> coords = coords();
        int minX = coords.stream().mapToInt(Coordinate::getX).min().orElse(0);
        int maxX = coords.stream().mapToInt(Coordinate::getX).max().orElse(0);

        return maxX - minX + 1;
    }

    /**
     * Megadja az alakzat magasságát.
     * @return magasság
     */
    int getHeight() {
        List<Coordinate> coords = coords();
        int minY = coords.stream().mapToInt(Coordinate::getY).min().orElse(0);
        int maxY = coords.stream().mapToInt(Coordinate::getY).max().orElse(0);

        return maxY - minY + 1;
    }

    /**
     * Azalakzat hash kódja
     * @return a hash kód
     */
    @Override
    public int hashCode() {
        int temp = 17;
        temp = temp * 31 + color.hashCode();
        temp = temp * 31 + shape.hashCode();
        temp =  temp * 31 + form.hashCode();
        return temp * 31 + position.hashCode();

    }

    /**
     * Az alakzatok egyenlőek-e
     * @param o összehasonlítandó
     * @return egyenlőek-e
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameObject)) {
            return false;
        }

        GameObject go = (GameObject)o;
        return go.color == color && go.shape == shape && go.form.equals(form) && go.position.equals(position);
    }
}
