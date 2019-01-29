class MonsterWifi extends Monster{
  private int timerColor = 0;
  private final float timerColorFrame = (0.2*FRAMES);
  private int timerAnimation = 0;
  private int timerAnimationFrame = 3;
  private int cont = 0;
  
  public MonsterWifi(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 8;
    this.rad = 40f;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(#3BF200);
  }
  
  public void updateEasy(ArrayList<Bala> balas){
   pos = new PVector(pos.x, pos.y);
   if(!this.inmortal){
     colision(balas);
   }
  }
  
  public void paint(){
    if(this.inmortal){
      timerColor();
    }else{
      this.c = color(#3BF200);
    }
    
    noStroke();
    fill(c);
    pushMatrix();
    translate(pos.x,pos.y);
    ellipse(0,0,this.rad-20f,this.rad-20f);
    //debugArea(rad);
    timer();
    popMatrix();
  }
  
  void timer(){
    noFill();
    stroke(c);
    strokeWeight(1);
    timerAnimation++;
    if(timerAnimation >= timerAnimationFrame){
      cont++;
      if(cont%2==0){
        ellipse(0,0,30,30);
        cont = 0;
      }else{
        ellipse(0,0,30,30);
        ellipse(0,0,40,40);
        
      }
      
      timerAnimation = 0;
    }
    
  }
  
  public void colision(ArrayList<Bala> balas){
    //INTERACCIONA CON EL PLAYER
    if(PVector.dist(this.pos,this.player.pos)<=this.player.r/2+rad/2){
      //finalScore = this.player.getScore();
      //over = true;
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
     if(this.c == color(#3BF200)){
       this.c = COLOR_INMORTAL;
     }else{
       this.c = color(#3BF200);
     }
     this.timerColor = 0;
   }
 }
}
