class Monster_easy extends Monster{
  
  public Monster_easy(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 3;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(#3399cc);
  }
  
  public void updateEasy(ArrayList<Bala> balas){
   pos = new PVector(pos.x, pos.y);
   colision(balas);
  }
  
  public void paint(){
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
      finalScore = this.player.getScore();
      over = true;
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
  
}
