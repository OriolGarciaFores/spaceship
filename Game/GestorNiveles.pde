/*
* CONTROL DE NIVELES DE PANTALLA/DIFICULTAD DEL JUEGO
* DESDE AQUI, CARGAR/LEER FICHERO NIVEL MAX DISPONIBLE EL JUGADOR DEFAULT 1.
*/

class GestorNiveles{
  
  private int level;
  private int maxMonsterEasy;
  private int maxMonsterShooter;
  private int maxScore;
  private int maxMeteoritos;
  private int maxMonsterWifi;
  private int maxLevel;
  
  private Configuration config;
  
  public GestorNiveles(){
    println("GESTOR NIVELES INICIALIZADO");
    this.maxMonsterEasy = 0;
    this.maxMonsterShooter = 0;
    this.maxScore = 0;
    this.maxMeteoritos = 0;
    this.maxMonsterWifi = 0;
    this.level = 1;
    this.config = new Configuration();
    this.maxLevel = Integer.parseInt(this.config.getInfo("maxLevel"));
    updateLevel();
  }
  
  void update(){
    updateLevel();
  }
  
  private void updateLevel(){
    switch(this.level){
      case 1:
        this.maxMonsterEasy = 20;
        this.maxMonsterShooter = 5;
        this.maxScore = 200;
      break;
      case 2:
        setMaxLevel(this.level);
        //CAMBIOS DE DIFICULTAD
        this.maxMonsterEasy = 5;
        this.maxMonsterShooter = 0;
        this.maxScore = 0;//500
        this.maxMeteoritos = 5;
        this.maxMonsterWifi = 8;
      break;
      case 3:
        over = true;
        endGame = true;
      break;
      default:
        this.level = 1;
        updateLevel();
      break;
    }
  
  }
  
  public void setLevel(int lv){
    this.level = lv;
  }
  
  public int getLevel(){
    return this.level;
  }
  
  public int getMaxMonsterEasy(){
    return this.maxMonsterEasy;
  }
  
  public int getMaxMonsterShooter(){
    return this.maxMonsterShooter;
  }
  
  public int getMaxScore(){
    return this.maxScore;
  }
  
  public int getMaxMeteoritos(){
    return this.maxMeteoritos;
  }
  
  public int getMaxLevel(){
    return this.maxLevel;
  }
  
  public void setMaxLevel(int maxLvl){
    if(this.maxLevel < maxLvl){
      this.maxLevel = maxLvl;
      this.config.update("maxLevel",""+this.maxLevel);
    }
  }
  
  public int getMaxMonsterWifi(){
    return this.maxMonsterWifi;
  }
  
}
