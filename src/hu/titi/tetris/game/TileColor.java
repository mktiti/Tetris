package hu.titi.tetris.game;

import java.awt.*;
import java.io.Serializable;

public enum TileColor implements Serializable {

    RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW), PURPLE(new Color(109, 23, 200)), CYAN(Color.CYAN), ORANGE(Color.ORANGE);

    public final Color color;

    TileColor(Color color) {
        this.color = color;
    }

}
