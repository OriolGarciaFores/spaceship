class Meteorito extends Monster{
  
  //IMAGEN HELICE
  
  private int contador = 0;
  
   public Meteorito(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 0;
    this.rad = 30f;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(#3399cc);
  }
  
  public void updateMet(ArrayList<Bala> balas){
   pos = new PVector(pos.x, pos.y);
   colision(balas);
  }
  
  public void paint(){
    noFill();
    strokeWeight(4);
    stroke(c);
    
    pushMatrix();
    translate(pos.x,pos.y);
    contador++;
    rotate(contador);

    for (int i = 0; i<4; i++) {
      rotate(HALF_PI*i);
      triangle(0, 0, 1*30f, 1*this.rad, 0, 1.41*this.rad);
    }
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
        //this.isDie = true;
      }
      i++;
    }
  }
}
