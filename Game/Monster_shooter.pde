class Monster_shooter extends Monster{

  /*
  * MONSTRUO NO MOVIL, QUE DISPARARA EN UNA UNICA DIRECCION QUE SE DETERMINARA POR UN RANDOM DE 8.
  */
  
  GestorDisparosEnemigos gde;
  private float timer;
  private final float timerFrame = (0.5*FRAMES);
  
  public Monster_shooter(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 5;
    this.health = 1;
    this.gde = new GestorDisparosEnemigos(this);
  }
  
  public Monster_shooter(Player player, PVector pos, int health){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 0;
    this.health = health;
    this.gde = new GestorDisparosEnemigos(this);
    this.rad = 35f;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(#FAD91C);
  }
  
  public void updateShot(ArrayList<Bala> balas){
   //pos = new PVector(pos.x, pos.y);
   gde.update();
   colision(balas, gde.getBalas());
  }
  
  public void paint(){
    timerColor();
    fill(c);
    strokeWeight(4);
    stroke(c);
    
    pushMatrix();
    translate(pos.x,pos.y);
    ellipse(0,0,rad,rad);
    //debugArea(rad);
    popMatrix();
  }
  
  public void timerColor(){
    timer++;
    if(this.c == color(#D66D0B) && timer >= timerFrame){
      this.c = color(#FAD91C);
      timer = 0;
    }
  }
  
  public void colision(ArrayList<Bala> balas, ArrayList<Bala> balasE){
    //INTERACCIONA CON EL PLAYER
    if(PVector.dist(this.pos,this.player.pos)<=this.player.r/2+rad/2){
      finalScore = this.player.getScore();
      over = true;
    }
    
    int ind = 0;
    while(!over && ind < balasE.size()){
      //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
      if(PVector.dist(this.player.pos,balasE.get(ind).pos)<=balasE.get(ind).rad/2+rad/2){
       balasE.get(ind).isDie = true;
       finalScore = this.player.getScore();
       over = true;
      }
      ind++;
    }
    
    int i = 0;
    while(!this.isDie && i < balas.size()){
      //INTERSECCION ENTRE BALA Y BICHO
      if(PVector.dist(this.pos,balas.get(i).pos)<=balas.get(i).rad/2+rad/2){
        balas.get(i).isDie = true;
        this.health--;
        if(this.health <= 0){
          this.isDie = true;
        }
        this.c = color(#D66D0B);
      }
      i++;
    }

  }
  
  public void setTarget(String target){
    this.gde.setTarget(target);
  }

}