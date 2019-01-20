class Player{
  private PVector pos, speed, acc;
  private float maxSpeed;
  private int score;
  private boolean isAuto;
  
  float r = 30f;
  
  public Player(int x, int y){
    pos = new PVector(x, y);
    this.speed = new PVector(0,0);
    this.acc = new PVector(0,0);
    this.maxSpeed = 5;
    this.score = 0;
    this.isAuto = false;
  }

  void update(){
    if(!this.isAuto){
      direction();
      move();
    }else{
      autoMove();
    }
    paint();
  }
  
  void paint(){
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    //debugArea(r*5); //AREA no respawn enemigos.
    //debuArea(r);
    popMatrix();
    if(isShowPositions){
      showPositions();
    }
    showScore();
  }
  
  private void body(){
    noStroke();
    fill(230,100,50);;
    rect(-10, -10, 20, 20);
  }
  
  private void move(){
   if(this.acc.x == 0 && this.acc.y == 0){
      speed.mult(0.9);
   }else{ 
      speed.add(this.acc);
      speed.limit(maxSpeed);
   }      
      pos.add(speed); 
      collision();
  }
  
  private void direction(){
     PVector direction = new PVector(0,0);
     
     if(KEYBOARD.up){
      direction.add(new PVector(0,-1));
     }
     if(KEYBOARD.left){
      direction.add(new PVector(-1,0));
     }
     if(KEYBOARD.down){
      direction.add(new PVector(0,1));
     }
     if(KEYBOARD.right){
      direction.add(new PVector(1,0));
     } 
        
     this.acc = direction;
  }
  
  public void autoMove(){
   PVector findTarget = new PVector(CENTRO_VENTANA_X-this.pos.x+2,CENTRO_VENTANA_Y-this.pos.y+2);
   
   if(abs(int(findTarget.x)) >= 0 && abs(int(findTarget.x)) <= 4 && abs(int(findTarget.y)) <= 4 && abs(int(findTarget.y)) >= 0 ) {return;}
   
   this.acc.add(findTarget);

   this.speed.add(this.acc);
   this.speed.limit(this.maxSpeed);

   this.pos.add(this.speed);
   
   this.acc = new PVector();
    
  }
  
  private void collision(){
    float x = range(this.pos.x, r/2, WIDTH-r/2);
    float y = range(this.pos.y, r/2, HEIGHT-r/2);
    
    this.pos = new PVector(x,y);
  }
  
  public void showPositions(){
    fill(155);
    textSize(18);
    text("POS X: " + int(this.pos.x), 20,40);
    text("POS Y: " + int(this.pos.y),20,60);
  }
  
  public void setScore(int score){
    this.score += score;
  }
  
  public int getScore(){
    return this.score;
  }
  
  public void showScore(){
    fill(255);
    textSize(18);
    text("SCORE: " + this.score,CENTRO_VENTANA_X,20);
  }
  
  public void setAutoMove(boolean isAuto){
    this.isAuto = isAuto;
  }
  
  public boolean getIsAutoMove(){
    return this.isAuto;
  }

}
