class MonsterBossV2 extends Monster{
  /*
  * BOSS MOVIL. Cada x tiempo -> Se ira al centro y dispara en circulo
  * Boss mantendra parte superior, e ira disparando hacia el jugador. (La direccion se determina 1 vez por bala).
  */
  
  boolean isStarted;
  private PVector hitBox = new PVector(WIDTH,HEIGHT/2);
  private int shield = 40;
  private int fase = 1;
  private float timer;
  private final float timerFrame = (3*FRAMES);
  private ArrayList<Ball> balls;
  
  public MonsterBossV2(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 3;
    init_monster(player);
    this.score = 50;
    this.isStarted = false;
    this.rad = 80f;
    this.balls = new ArrayList<Ball>();
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(255);
  }
  
  public void updateBoss(ArrayList<Bala> balas){
   if(pos.y <= 100 && !this.isStarted){
     pos = new PVector(pos.x, pos.y);
   }else{
     this.isStarted = true;
     //pos = new PVector(pos.x,100);
     pos = new PVector(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
     //DISPAROS EN EXPANSIVO SOLO CENTRO DE PANTALLA
     timerShot();

     //MECANICAS BOSS
     //colision(balas);
   }
//UPDATEAR BALAS
  updateBalls();
  //println(balls.size());
  }
  
  private void updateBalls(){
    for(int i = 0; i < balls.size(); i++){
      Ball ball = balls.get(i);
      ball.update();
      if(ball.isDie){
        balls.remove(i);
      }
    }
  }
  
  public void paint(){
   // timerColor();
    noStroke();
    
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);//POSICIONAMIENTO
    //rect(-7,-7,this.rad-5,this.rad-5);
    ellipse(0,0,this.rad,this.rad);
    //debugAreaPoint(this.rad-80f,WIDTH/4,HEIGHT/2);
    //debugAreaPoint(this.rad,WIDTH/4,HEIGHT/2);
    popMatrix();
    
    
  }
  
  public void timerShot(){
    stroke(255);
    strokeWeight(4);
    timer++;
    if(timer >= timerFrame){
     //this.gde.addBalaEnemy(new PVector(pos.x,pos.y-10), "L",1);
     //this.balls.add(new Ball(this.pos,this.player.pos,1));
     float points = 20;//numero randoms?
     float pointAngle = 360/points;
     
     for(float angle = 0; angle < 360; angle = angle+pointAngle){
       float x = cos(radians(angle)) * CENTRO_VENTANA_X;
       float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
      // println(x);
       //println(y);
       this.balls.add(new Ball(this.pos,new PVector(x+CENTRO_VENTANA_X,y+CENTRO_VENTANA_Y),3));
       
      // line(x+CENTRO_VENTANA_X,y+CENTRO_VENTANA_Y,CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
     }
     
      timer = 0;
    }
  }
  
  public void timerColor(){
    timer++;
  }
  
  public void colision(ArrayList<Bala> balas){
    if(this.fase != 3){
        //INTERACCIONA CON EL PLAYER
      if(PVector.dist(hitBox,this.player.pos)<=this.player.r/2+rad/2){
        finalScore = this.player.getScore();
        over = true;
      }
    }else{
      //INTERACCIONA CON EL PLAYER
      if(PVector.dist(hitBox,this.player.pos)<=this.player.r/2+(rad-80f)/2){
        finalScore = this.player.getScore();
        over = true;
      }
    }
    
    int i = 0;
    while(!this.isDie && i < balas.size()){
      //INTERSECCION ENTRE BALA Y BICHO
      if(this.fase < 3){
        if(PVector.dist(hitBox,balas.get(i).pos)<=balas.get(i).rad/2+this.rad/2){
          if(shield > 0 && this.fase == 2){
            shield--;
          }
          balas.get(i).isDie = true;
        }
      }else if(this.fase == 4){
        if(PVector.dist(hitBox,balas.get(i).pos)<=balas.get(i).rad/2+(this.rad-220f)/2){
          if(shield > 0){
            shield--;
          }
          balas.get(i).isDie = true;
        }
      }

      i++;
    }
  }
  
  public boolean getIsStarted(){
    return this.isStarted;
  }
  
  public void setHitBox(int w, int h){
    this.hitBox = new PVector(w,h);
  }
  
  public int getFase(){
    return this.fase;
  }
  
  public void setFase(int fase){
    this.fase = fase;
  }
  
  public void setShield(int shield){
    this.shield = shield;
  }
}
