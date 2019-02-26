final int FRAMES = 60;
final int MAX_LVLS = 2;

final GestorControles KEYBOARD = new GestorControles();
final String USER = System.getProperty ("user.home");

//COLORS CODE
final int WHITE = 255;
final color COLOR_DMG = #D64C16;
final color COLOR_RAGE = #CB1010;
final color COLOR_INMORTAL = #ECAAFF;
final color COLOR_ORANGE = color(230, 100, 50);


//RADIO ENEMIGOS
final float MONSTER_EASY_RAD = 20f;
final float MONSTER_SHOT_RAD = 20f;
final float METEORITO_RAD = 80f;
final float MONSTER_WIFI_RAD = 40f;
final float MONSTER_BOMB_RAD = 80f;
final float SHOOTER_V2_RAD = 30f;

//FILES
final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data_dev.json";
//final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data.json";
final String ROUTE_DIRECTORY_CONFIG = USER + "/Documents/Spaceship/configuration";
final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config_dev.properties";
//final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config.properties";
