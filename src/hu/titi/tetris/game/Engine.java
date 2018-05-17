package hu.titi.tetris.game;

import hu.titi.tetris.util.HighScoreManager;
import hu.titi.tetris.util.SaveManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Engine implements KeyListener {

    public enum State { RUNNING, PAUSED, FINISHED, EXIT }

    @FunctionalInterface
    public interface Redrawable { void requestRepaint(); }

    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 20;

    private static final int BORDER = 10;
    private static final int MAX_NEXT_SIZE = 170;

    private static final int PROJECTION_ALPHA = 64;
    private static final int TILE_BORDER = 3;

    private Field field;

    private GameObject currentObject;
    private GameObject nextObject;

    private final Random random = new Random();

    private final Redrawable panel;
    private final Redrawable nextShapePanel;
    private final Redrawable scorePanel;
    private final Redrawable statePanel;

    private volatile State state = State.PAUSED;

    private final AtomicLong score = new AtomicLong(0);
    private volatile double speed = 1D;

    private final HighScoreManager scoreManager = new HighScoreManager();
    private final JFrame frame;

    private File saveFile = null;

    private final Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            synchronized (Thread.currentThread()) {
                state = Engine.State.RUNNING;
                statePanel.requestRepaint();
                while (state != State.EXIT) {
                    if (state == Engine.State.PAUSED) {
                        panel.requestRepaint();
                        try {
                            Thread.currentThread().wait();
                        } catch (InterruptedException ie) {}
                        panel.requestRepaint();
                    } else if (state == Engine.State.RUNNING) {
                        try {
                            Thread.currentThread().wait((long) (1_000 / speed));
                            if (state == Engine.State.RUNNING) {
                                move(Coordinate.Direction.Down);
                            }
                        } catch (InterruptedException ie) {}
                    } else if (state == Engine.State.FINISHED) {
                        panel.requestRepaint();
                        try {
                            Thread.currentThread().wait();
                        } catch (InterruptedException ie) {}
                        panel.requestRepaint();
                    }
                }
            }
        }
    });

    private Engine(JFrame frame, Redrawable panel, Redrawable nextShapePanel, Redrawable scorePanel, Redrawable statePanel, int width, int height) {
        this.frame = frame;
        this.statePanel = statePanel;
        this.panel = panel;
        this.nextShapePanel = nextShapePanel;
        this.scorePanel = scorePanel;
        field = new Field(width, height);
    }

    public Engine(JFrame frame, Redrawable panel, Redrawable nextShapePanel, Redrawable scorePanel, Redrawable statePanel) {
        this(frame, panel, nextShapePanel, scorePanel, statePanel, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Engine(JFrame frame, Redrawable panel, Redrawable nextShapePanel, Redrawable scorePanel, Redrawable statePanel, Save save) {
        this.frame = frame;
        this.statePanel = statePanel;
        this.panel = panel;
        this.nextShapePanel = nextShapePanel;
        this.scorePanel = scorePanel;
        score.set(save.getScore());
        field = save.getField();
        currentObject = save.getObject();
        nextObject = save.getNext();
    }

    /**
     * Alakzat ejtése
     */
    private synchronized void drop() {
        currentObject.drop(field);
        addToField();
        panel.requestRepaint();
        next();
    }

    /**
     * Alakzat mozgatása
     * @param direction a mozgatás iránya
     */
    private synchronized void move(Coordinate.Direction direction) {

        if (!currentObject.move(field, direction) && direction == Coordinate.Direction.Down) {
            addToField();
            next();
        }
        panel.requestRepaint();
    }

    /**
     * Alakzat hozzárakása a táblához. (Ha leért)
     * @return volt-e sortörlés
     */
    private boolean addToField() {
        int deletedRows = field.save(currentObject);

        score.addAndGet(deletedRows * 100);
        scorePanel.requestRepaint();

        return deletedRows > 0;
    }

    /**
     * Alakzat forgatása.
     */
    private void rotate() {
        currentObject.rotate(field);
        panel.requestRepaint();
    }

    /**
     * Motor elindítása.
     */
    public void run() {
        scorePanel.requestRepaint();
        if (currentObject == null) {
            next();
        }
        gameThread.start();
    }

    /**
     * Következő alakzatra lépés.
     * @return folytatódhat-e a játék
     */
    private boolean next() {
        if (nextObject == null) {
            nextObject = newObject();
        } else {
            speed += 0.01;
        }

        currentObject = nextObject;
        nextObject = newObject();

        nextShapePanel.requestRepaint();

        if (!currentObject.isValid(field)) {

            state = State.FINISHED;

            statePanel.requestRepaint();
            panel.requestRepaint();

            if (scoreManager.shouldAdd(score.get())) {
                String name = JOptionPane.showInputDialog(frame, "Congrats! What's your name?", "Top 10 Score", JOptionPane.QUESTION_MESSAGE);
                if (name == null || name.length() == 0) {
                    name = System.getProperty("user.name");
                    if (name == null || name.trim().length() == 0) {
                        name = "player";
                    }
                }
                scoreManager.addHighScore(name, score.get());
            }

        }

        return currentObject.isValid(field);
    }

    /**
     * Új véletlenszerű alakzat generálása.
     * @return a generát alakzat
     */
    private GameObject newObject() {
        TileColor color = TileColor.values()[random.nextInt(TileColor.values().length)];
        Shape shape = Shape.values()[random.nextInt(Shape.values().length)];

        GameObject newObject = new GameObject(color, shape, field.startPos());;
        newObject.moveDownToFit();

        return newObject;
    }

    /**
     * Játék megállítás/folytatás.
     */
    public void pause() {
        switch (state) {
            case RUNNING:
                state = State.PAUSED;
                statePanel.requestRepaint();
                break;
            case PAUSED:
                state = State.RUNNING;
                statePanel.requestRepaint();
                break;
            case FINISHED:
                field = new Field(field.getWidth(), field.getHeight());

                nextObject = null;
                next();

                synchronized (gameThread) {
                    gameThread.notify();
                }
                panel.requestRepaint();
                score.set(0);
                scorePanel.requestRepaint();
                state = State.RUNNING;
                statePanel.requestRepaint();
                break;
            default: break;
        }

        synchronized (gameThread) {
            gameThread.notify();
        }
    }

    /**
     * Megadja a játék állapotát.
     * @return a játék állapota
     */
    public State getState() {
        return state;
    }

    /**
     * Megrajzolja a következő alakzatot az átvett Graphicsre.
     * @param g ahova rajzolja
     * @param width a felület szélessége
     * @param height a felület magassága
     */
    public void drawNextShape(Graphics g, int width, int height) {
        if (nextObject == null) return;

        GameObject normalized = nextObject.smallImage();
        int ow = normalized.getWidth();
        int oh = normalized.getHeight();

        int tileSize = Math.min((Math.min(width, MAX_NEXT_SIZE) - 2 * BORDER) / ow, (Math.min(height, MAX_NEXT_SIZE) - 2 * BORDER) / oh);
        int fillSize = tileSize - 2 * TILE_BORDER;

        int startX = ((width - 2 * BORDER) - tileSize * ow) / 2 + BORDER + TILE_BORDER;
        int startY = ((height - 2 * BORDER) - tileSize * oh) / 2 + BORDER + TILE_BORDER;

        g.setColor(nextObject.getColor().color);
        normalized.coords().stream().forEach(c -> g.fillRect(startX + (c.x * tileSize), startY + (c.y * tileSize), fillSize, fillSize));
    }

    /**
     * Megrajzolja a táblát az átvett Graphicsre.
     * @param g ahova rajzolja
     * @param width a felület szélessége
     * @param height a felület magassága
     */
    public void draw(Graphics g, int width, int height) {
        if (currentObject == null) return;

        int tileSize = Math.min((width - 2 * BORDER) / field.getWidth(), (height - 2 * BORDER) / field.getHeight());
        int fillSize = tileSize - 2 * TILE_BORDER;

        int startX = ((width - 2 * BORDER) - tileSize * field.getWidth()) / 2 + BORDER;
        int startY = ((height - 2 * BORDER) - tileSize * field.getHeight()) / 2 + BORDER;

        g.setColor(Color.BLACK);
        g.fillRect(startX, startY, field.getWidth() * tileSize, field.getHeight() * tileSize);

        int sx = startX + TILE_BORDER;
        int sy = startY + TILE_BORDER;

        List<Coordinate> currentCoords = currentObject.coords();
        Color col = currentObject.getColor().color;
        g.setColor(col);
        currentCoords.stream().filter(c -> c.y >= 0)
                              .forEach(c -> g.fillRect(sx + (c.x * tileSize), sy + (c.y * tileSize), fillSize, fillSize));

        List<Coordinate> projectedCoords = new GameObject(currentObject).drop(field).coords();
        g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), PROJECTION_ALPHA));
        projectedCoords.stream().filter(c -> !currentCoords.contains(c))
                                .forEach(c -> g.fillRect(sx + (c.x * tileSize), sy + (c.y * tileSize), fillSize, fillSize));

        for (int y = 0, locY = startY; y < field.getHeight(); y++, locY += tileSize) {
            for (int x = 0, locX = startX; x < field.getWidth(); x++, locX += tileSize) {
                Optional<TileColor> color = field.getColor(x, y);
                if (color.isPresent()) {
                    g.setColor(color.get().color);
                    g.fillRect(sx + (x * tileSize), sy + (y * tileSize), fillSize, fillSize);
                }
            }
        }

    }

    /**
     * Megadja a pontszmot.
     * @return a pontszám
     */
    public long getScore() {
        return score.get();
    }

    /**
     * Megadja a highscoret
     * @return a highscore
     */
    public long getHighScore() {
        return scoreManager.getHighScore();
    }

    /**
     * Elmenti a játékot
     */
    public void save() {
        if (state == State.RUNNING || state == State.PAUSED) {
            State oldState = state;
            state = State.PAUSED;
            synchronized (gameThread) {
                gameThread.notify();
            }

            if (saveFile == null) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Tetris saves", "tet");
                fileChooser.setFileFilter(filter);
                fileChooser.setApproveButtonText("Save");
                fileChooser.setDialogTitle("Save game");
                fileChooser.setCurrentDirectory(SaveManager.getDefaultDir());

                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    saveFile = fileChooser.getSelectedFile();
                    SaveManager.save(fileChooser.getSelectedFile(), new Save(field, currentObject, nextObject, score.get()));
                }
            } else {
                SaveManager.save(saveFile, new Save(field, currentObject, nextObject, score.get()));
            }

            state = oldState;
            synchronized (gameThread) {
                gameThread.notify();
            }
        }
    }

    /**
     * Kilép a játékból. (A felhasználó beleszólhat.)
     * @return a felhasználó beleegyezett-e a kilépésbe
     */
    public boolean exit() {
        boolean ret = true;

        if (state == State.RUNNING || state == State.PAUSED) {
            state = State.PAUSED;
            synchronized (gameThread) {
                gameThread.notify();
            }

            int option = JOptionPane.showOptionDialog(frame, "Do you want to save the game?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            switch (option) {
                case JOptionPane.YES_OPTION:
                    save();
                    break;
                case JOptionPane.CANCEL_OPTION:
                    ret = false;
                    break;
                default:
                    break;
            }
        }
        state = ret ? State.EXIT : State.RUNNING;
        synchronized (gameThread) {
            gameThread.notify();
        }

        return ret;
    }

    /**
     * Bemenet kezelés.
     * @param keyEvent bemeneti esemény
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    /**
     * Bemenet kezelés.
     * @param keyEvent bemeneti esemény
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (state == State.RUNNING) {
                    move(Coordinate.Direction.Left);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (state == State.RUNNING) {
                    move(Coordinate.Direction.Right);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (state == State.RUNNING) {
                    synchronized (gameThread) {
                        gameThread.notify();
                        move(Coordinate.Direction.Down);
                    }
                }
                break;
            case KeyEvent.VK_SPACE:
                if (state == State.RUNNING) {
                    synchronized (gameThread) {
                        gameThread.notify();
                        drop();
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (state == State.RUNNING) {
                    rotate();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_P:
                pause();
                break;
            default:
                break;
        }

    }

    /**
     * Bemenet kezelés.
     * @param keyEvent bemeneti esemény
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {}

}