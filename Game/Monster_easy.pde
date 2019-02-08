class Monster_easy extends Monster{
  
  private int timerColor = 0;
  private final float timerColorFrame = (0.2*FRAMES);
  
  public Monster_easy(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 3;
    this.id = 0;
    this.rad = MONSTER_EASY_RAD;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(#3399cc);
  }
  
  @Override
  public void updatePaint(ArrayList<Bala> balas){
   pos = new PVector(pos.x, pos.y);
   if(!this.inmortal){
     colision(balas);
   }
   paint();
  }
  
  public void paint(){
    if(this.inmortal){
      timerColor();
    }else{
      this.c = color(#3399cc);
    }
    noFill();
    strokeWeight(4);
    stroke(c);
    
    pushMatrix();
    translate(pos.x,pos.y);
    rect(-7,-7,15,15);
    //debugArea(rad);
    popMatrix();
  }
  
  public void colision(ArrayList<Bala> balas){
    //INTERACCIONA CON EL PLAYER
    if(PVector.dist(this.pos,this.player.pos)<=this.player.r/2+rad/2){
      this.player.decreaseLife();
    }
    int i = 0;
    while(!this.isDie && i < balas.size()){
      //INTERSECCION ENTRE BALA Y BICHO
      if(PVector.dist(this.pos,balas.get(i).pos)<=balas.get(i).rad/2+rad/2){
        balas.get(i).isDie = true;
        this.isDie = true;
      }
      i++;
    }
  }
  
 private void timerColor(){
   this.timerColor++;
   if(this.timerColor > this.timerColorFrame){
     if(this.c == color(#3399cc)){
       this.c = COLOR_INMORTAL;
     }else{
       this.c = color(#3399cc);
     }
     this.timerColor = 0;
   }
 }
  
}
