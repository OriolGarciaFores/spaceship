package core.utils;

import core.gestores.GestorNiveles;
import core.gestores.GestorSaveData;

public class Global {
    public static Boolean isFull = false;
    public static Boolean inGame = false;
    public static Boolean over = false;
    public static Boolean outGameOver = false;
    public static boolean endGame = false;
    public static boolean isSelection = false;
    public static boolean isLvlComplete = false;
    public static boolean isExit = false;
    public static GestorSaveData gestorSaveData;
    public static GestorNiveles gestorNiveles;

    public static int finalScore = 0;
    public static int finalLvl = 0;
}
