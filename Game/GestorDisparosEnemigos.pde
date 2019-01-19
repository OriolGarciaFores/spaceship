class GestorDisparosEnemigos{ 
  //BALAS ENEMIGOS
  private ArrayList<Bala> enemyBalas;
  private Monster_shooter shooter;
  
  private int timerShootEnemy = 0;
  private final float shootDistEnemy = FRAMES/2;
  
  private int timerChangeTarget = 0;
  private final float changeTargetFrame = FRAMES;
  
  private final String[] targetDirect = {"L", "R", "U", "D", "UL", "UR", "DL", "DR"};
  private String direct;
  
  public GestorDisparosEnemigos(Monster_shooter shooter){
    //ENEMIES
    this.enemyBalas = new ArrayList<Bala>();
    this.shooter = shooter;
    this.direct = randomTarget();
  }
  
  void update(){
    timer();
    move();
  }
  
  private void move(){
    if(!enemyBalas.isEmpty()){
      for (int i = 0; i < enemyBalas.size();i++){
        Bala eb = enemyBalas.get(i);
        eb.update();
        eb.paint();
        if(eb.isDie()){
          enemyBalas.remove(i);
        }
      }
    }
    
  }
  
  private void timer(){
    timerChangeTarget++;
    if(timerChangeTarget >= changeTargetFrame){
      this.direct = randomTarget();
    }
    
    timerShootEnemy++;
    if(timerShootEnemy >= shootDistEnemy){
      //addBalaEnemy(this.shooter.pos, randomTarget());//RANDOM LA DIRECCION
      //GENERAR ALGUN ENEMIGO CON CADA DISPARO SEA ALEATORIO?¿?? MODO HARDCORE
      addBalaEnemy(this.shooter.pos, this.direct);
      timerShootEnemy = 0;
    }
    
  }
  
  public void addBalaEnemy(PVector pos, String direct){
    enemyBalas.add(new Bala(new PVector(pos.x, pos.y), direct, color(#FAD91C)));
  }
  
  public ArrayList<Bala> getBalas(){
    return enemyBalas;
  }
  
  private String randomTarget(){
    int index = int(random(targetDirect.length));
    
    return targetDirect[index];
  }
  
  public void setTarget(String target){
    this.direct = target;
  }
  
}
