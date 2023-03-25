package core.utils;

import core.gestores.GestorControles;

import java.awt.*;

public class Constants {
    public static final GestorControles KEYBOARD = new GestorControles();
    public static final String USER = System.getProperty("user.home");

    public static final int WIDTH = 720;
    public static final int HEIGHT = 560;
    public static final int CENTRO_VENTANA_X = WIDTH / 2;
    public static final int CENTRO_VENTANA_Y = HEIGHT / 2;
    public static final int FRAMES = 60;
    public static final int MAX_LVLS = 3;

    //COLORS CODE TODO DEMASIADOS OBJETOS, CALCULAR EL RGB Y METERLO EN UN INT ?
    public static final int WHITE = 255;
    public static final Color COLOR_WHITE = new Color(255, 255, 255);
    public static final Color COLOR_DMG = Color.decode("#D64C16");
    public static final Color COLOR_PUNTO_DEBIL = new Color(251, 165, 146);
    public static final Color COLOR_RAGE = Color.decode("#CB1010");
    public static final Color COLOR_INMORTAL = Color.decode("#ECAAFF");
    public static final Color COLOR_ORANGE = new Color(230, 100, 50);


    //RADIO ENEMIGOS
    public static final float ENEMY_SHIP = 20f;
    public static final float ENEMY_SHOOTER_RAD = 20f;
    public static final float METEORITO_RAD = 80f;
    public static final float ENEMY_SHIP_WIFI_RAD = 40f;
    public static final float BOMBA_RAD = 80f;
    public static final float SHIP_V2_RAD = 30f;
    public static final float BOSS_V3_RAD = 50F;

    //FILES
    //public static final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data_dev.json";
    public static final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data.json";
    public static final String ROUTE_DIRECTORY_CONFIG = USER + "/Documents/Spaceship/configuration";
    //public static final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config_dev.properties";
    public static final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config.properties";

    public static final boolean DEBUG = true;
}
