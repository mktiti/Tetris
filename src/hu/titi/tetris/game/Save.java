package hu.titi.tetris.game;

import java.io.Serializable;

public class Save implements Serializable {

    private final Field field;
    private final GameObject object;
    private final GameObject next;
    private final long score;

    public Save(Field field, GameObject object, GameObject next, long score) {
        this.field = field;
        this.object = object;
        this.next = next;
        this.score = score;
    }

    /**
     * A tárolt Field.
     * @return a tárolt Field
     */
    public Field getField() {
        return field;
    }

    /**
     * A tárolt elem.
     * @return a tárolt elem
     */
    public GameObject getObject() {
        return object;
    }

    /**
     * A tárolt következő elem.
     * @return a tárolt következő elem
     */
    public GameObject getNext() {
        return next;
    }

    /**
     * A tárolt pontszám.
     * @return a tárolt pontszám
     */
    public long getScore() {
        return score;
    }
}
