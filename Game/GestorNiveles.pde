/* //<>// //<>//
* CONTROL DE NIVELES DE PANTALLA/DIFICULTAD DEL JUEGO
 * DESDE AQUI, CARGAR/LEER FICHERO NIVEL MAX DISPONIBLE EL JUGADOR DEFAULT 1.
 */

class GestorNiveles {

  private int level;
  private int maxMonsterEasy;
  private int maxMonsterShooter;
  private int maxScore;
  private int maxMeteoritos;
  private int maxMonsterWifi;
  private int maxLevel;

  private Configuration config;

  public GestorNiveles() {
    println("GESTOR NIVELES INICIALIZADO");
    init_monsters(0, 0, 0, 0);
    this.level = 1;
    this.config = new Configuration();
    this.maxLevel = Integer.parseInt(this.config.getInfo("maxLevel"));
  }

  void update() {
    updateLevel();
  }

  private void updateLevel() {
    switch(this.level) {
    case 1:
      //MONSTEREASY, SHOOTER, METEORITOS, WIFI.
      init_monsters(20, 5, 0, 0);
      this.maxScore = 200;
      systemSound.beforeStop();
      systemSound.play(1);
      break;
    case 2:
      setMaxLevel(this.level);
      //MONSTEREASY, SHOOTER, METEORITOS, WIFI.
      init_monsters(5, 0, 5, 8);
      this.maxScore = 500;
      systemSound.beforeStop();
      systemSound.play(2);
      break;
    case 3:
      //NUNCA ENTRA
      //RESET DE ANTERIORES NIVELES
      setMaxLevel(this.level);
      init_monsters(0, 0, 0, 0);
      break;
    default:
      this.level = 1;
      updateLevel();
      break;
    }
  }

  public void setLevel(int lv) {
    this.level = lv;
  }

  public int getLevel() {
    return this.level;
  }

  public int getMaxMonsterEasy() {
    return this.maxMonsterEasy;
  }

  public int getMaxMonsterShooter() {
    return this.maxMonsterShooter;
  }

  public int getMaxScore() {
    return this.maxScore;
  }

  public int getMaxMeteoritos() {
    return this.maxMeteoritos;
  }

  public int getMaxLevel() {
    return this.maxLevel;
  }

  public void setMaxLevel(int maxLvl) {
    if (this.maxLevel < maxLvl) {
      this.maxLevel = maxLvl;
      this.config.update("maxLevel", ""+this.maxLevel);
    }
  }

  public int getMaxMonsterWifi() {
    return this.maxMonsterWifi;
  }

  private void init_monsters(int me, int ms, int mm, int mw) {
    this.maxMonsterEasy = me;
    this.maxMonsterShooter = ms;
    this.maxMeteoritos = mm;
    this.maxMonsterWifi = mw;
  }
}
