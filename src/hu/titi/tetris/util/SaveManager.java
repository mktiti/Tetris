package hu.titi.tetris.util;

import hu.titi.tetris.game.Save;

import java.io.*;

public class SaveManager {

    private static File defaultDir = null;

    /**
     * Állapot mentése a megadott fileba.
     * @param f a mentés helye
     * @param s a menteni kívánt állapot
     */
    public static void save(File f, Save s) {
        if (!f.getName().endsWith(".tet")) {
            f = new File(f.getPath() + ".tet");
        }

        defaultDir = f.getParentFile();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(s);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Állapot visszaállítása a megadott fileból.
     * @param f a mentés helye
     * @return a beolvasott állapot
     */
    public static Save load(File f) {
        Save ret = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof Save) {
                ret = (Save)o;
                defaultDir = f.getParentFile();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Megadja a legutóbbi mentés/visszaállítás helyét.
     * @return a legutóbbi mentés/visszaállítás helye
     */
    public static File getDefaultDir() {
        return defaultDir;
    }
}
