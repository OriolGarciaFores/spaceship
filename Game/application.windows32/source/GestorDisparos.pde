class GestorDisparos{
  private ArrayList<Bala> balas;
  private Player player;
  
  private int timerShoot = 0;
  private final float shootDist = FRAMES/6;
  
  public GestorDisparos(Player player){
    this.balas = new ArrayList<Bala>();
    this.player = player;
  }
  
  void update(){
    timer();
    move();
  }
  
  private void move(){
    if(!balas.isEmpty()){
      for (int i = 0; i < balas.size();i++){
        Bala b = balas.get(i);
        b.update();
        b.paint();
        if(b.isDie()){
          balas.remove(i);
        }
      }
    }    
  }
  
  private void timer(){
    timerShoot++;
    if(timerShoot >= shootDist){
      setShot();
      timerShoot = 0;
    }
  }
  
  public void addBala(PVector pos, String direct){
    balas.add(new Bala(new PVector(pos.x, pos.y), direct));
  }
  
  public void setShot(){
    String direct = "";
    
    if(!KEYBOARD.shotUp && !KEYBOARD.shotDown && !KEYBOARD.shotLeft && !KEYBOARD.shotRight ||
  KEYBOARD.shotUp && KEYBOARD.shotDown || KEYBOARD.shotLeft && KEYBOARD.shotRight){return;}
    
    if(KEYBOARD.shotUp){
      direct += "U";
    } 
    if(KEYBOARD.shotDown){
      direct += "D";
    } 
    if(KEYBOARD.shotLeft){
      direct += "L";
    } 
    if (KEYBOARD.shotRight){
      direct += "R";
    }
    
    addBala(this.player.pos, direct);
  }
  
  public ArrayList<Bala> getBalas(){
    return balas;
  }
  
}
