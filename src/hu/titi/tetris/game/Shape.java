package hu.titi.tetris.game;

import java.io.Serializable;

import static hu.titi.tetris.game.Coordinate.of;

public enum Shape implements Serializable {

    O {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(1, 0), of(0, 1), of(1, 1));
        }
    }, I {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(0, -1), of(0, 1), of(0, 2));
        }
    }, L {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(0, -1), of(0, 1), of(1, 1));
        }
    }, J {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(0, -1), of(0, 1), of(-1, 1));
        }
    }, S {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(1, 0), of(0, 1), of(-1, 1));
        }
    }, Z {
        @Override
        public Tiles baseTiles() { return new Tiles(of(-1, -1), of(1, 0), of(-1, 0)); }
    }, T {
        @Override
        public Tiles baseTiles() {
            return new Tiles(of(0, -1), of(1, 0), of(-1, 0));
        }
    };

    /**
     * Megadja egy adott forma leírását (elforgatás nélkül)
     * @return a forma leírása
     */
    public abstract Tiles baseTiles();

}
