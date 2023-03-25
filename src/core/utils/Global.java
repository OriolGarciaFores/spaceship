package core.utils;

import core.gestores.GestorNiveles;
import core.gestores.GestorSaveData;
import core.gestores.GestorSonido;
import core.gestores.Logger;

public class Global {
    public static Boolean isFull = false;
    public static Boolean inGame = false;
    public static Boolean over = false;
    public static Boolean outGameOver = false;
    public static boolean isSelection = false;
    public static boolean isLvlComplete = false;
    public static boolean isExit = false;
    public static GestorSaveData gestorSaveData;
    public static GestorNiveles gestorNiveles;
    public static GestorSonido gestorSonido;
    public static Logger logger;

    public static int finalScore = 0;
    public static int finalLvl = 0;
}
