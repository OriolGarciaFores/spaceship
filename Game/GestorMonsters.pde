class GestorMonsters{
  
  ArrayList <Monster_easy> monsterEasy = new ArrayList<Monster_easy>();
  int monsterEasyBornTimer;
  int monsterEasyBornDist = int(0.5*FRAMES);//Ratio de aparicion por frame
  float monsterEasyRad = 20f;
  
  ArrayList <Monster_shooter> monsterShooter = new ArrayList<Monster_shooter>();
  int monsterShotBornTimer;
  int monsterShotBornDist = int(3*FRAMES);
  float monsterShotRad = 20f;
  
  ArrayList<Meteorito> meteoritos = new ArrayList<Meteorito>();
  int meteoBornTimer;
  int meteoBornDist = int(10*FRAMES);
  float meteoRad = 30f;
  
  private int bornShipsInBoss = 0;
  private MonsterBoss mb;
  
  private Player player;
  
  private GestorNiveles gn;
  
  public GestorMonsters(Player player, GestorNiveles gn){
    this.player = player;
    this.monsterEasyBornTimer = 0;
    this.mb = new MonsterBoss(this.player,new PVector(WIDTH+20,0));
    this.gn = gn;
  }
  
  void update(ArrayList<Bala> balas){
    
    for(int i = 0; i < meteoritos.size();i++){
      Meteorito met = meteoritos.get(i);
      met.updateMet(balas);
      met.update();
      if(met.isDie){
        meteoritos.remove(i);
      }
      met.paint();
    }
    
    for(int i = 0; i < monsterEasy.size(); i++){
      Monster_easy mEasy = monsterEasy.get(i);
      mEasy.updateEasy(balas);
      mEasy.update();
      if(mEasy.isDie){
        this.player.setScore(mEasy.score);
        monsterEasy.remove(i);
      }
      mEasy.paint();
    }
    
    //CUANDO ESTEN TODO
    mechanicalBoss(balas);
    
    
    for(int i = 0; i < monsterShooter.size(); i++){
      Monster_shooter mShot = monsterShooter.get(i);
      mShot.updateShot(balas);
      //mShot.update();
      if(mShot.isDie){
        this.player.setScore(mShot.score);
        monsterShooter.remove(i);
      }
      mShot.paint();
    }

    if(this.player.score < this.gn.getMaxScore()){
          timer();
    }
    
    
    if(mb.isDie){
      switch(this.gn.getLevel()){
        case 1:
        //PANTALLA DE RESULTADOS Y RESETEAR EL SCORE
          //this.player.score = 0;
          this.gn.setLevel(2);
        break;
        case 2:
        break;
      }
    }

  }
  
  private void mechanicalBoss(ArrayList<Bala> balas){
   switch(this.gn.getLevel()){
     case 1:
       if(this.player.score >= this.gn.getMaxScore() && (monsterEasy.isEmpty() && monsterShooter.isEmpty() || mb.getIsStarted()) && !mb.isDie){
         mb.updateBoss(balas);
        if(!mb.getIsStarted()){
          this.player.setAutoMove(true);
          mb.update();
        }else{
          this.player.setAutoMove(false);
          timerBoss();
        }
        mb.paint();
      }
     break;
     case 2:
       //BOSS 2n
     break;
   }  
  }
  
  private void timer(){
    monsterEasyBornTimer++;
    if(monsterEasyBornTimer >= monsterEasyBornDist){
      addMonsterEasy(1);
      monsterEasyBornTimer = 0;
    }
    monsterShotBornTimer++;
    if(monsterShotBornTimer >= monsterShotBornDist){
      addMonsterShooter(1);
      monsterShotBornTimer = 0;
    }
    meteoBornTimer++;
    if(meteoBornTimer >= meteoBornDist){
      addMeteo(1);
      meteoBornTimer = 0;
    }
  }
  
  private void timerBoss(){
    
    switch(mb.getFase()){
      case 1:
         if(bornShipsInBoss < 50){
            monsterEasyBornTimer++;
            if(monsterEasyBornTimer >= monsterEasyBornDist){
              bornShipsInBoss++;
              addMonsterEasyBoss(5);
              monsterEasyBornTimer = 0;
            }
          }else if(monsterEasy.isEmpty()){
            mb.setFase(2);
          }
         if(!mb.loadShooters){
          mb.loadShooters = true;
          addMonsterShooterBoss();
        }
        break;
      case 2:
      //APARICION DE BICHOS MUY DE VEZ EN CUANDO???
      // random numero de naves de aparicion instantaneo 1-5
        //ESCUDO + CAÑONES
        //POSICIONAR CAÑONES
        if(mb.shield <= 0){
          mb.setFase(3);
        }
        break;
      case 3:
        if(monsterShooter.isEmpty()){
          mb.setFase(4);
          mb.setShield(40);
        }
        break;
      case 4:
        //DAÑO A LA NAVE
        if(mb.shield <= 0){
          this.player.setScore(mb.score);
          finalScore = this.player.getScore();
          mb.isDie = true;
        }
       break;
    
    }
  }
  
  private void addMonsterEasy(int i){
    if(monsterEasy.size() < this.gn.getMaxMonsterEasy()){
      for(int c = 0; c < i; c++){
        monsterEasy.add(new Monster_easy(this.player,respawn(monsterEasyRad, false)));
      }
    }
  }
  
  private void addMonsterEasyBoss(int i){
     if(monsterEasy.size() < 100){
      for(int c = 0; c < i; c++){
        monsterEasy.add(new Monster_easy(this.player,respawnInBoss(monsterEasyRad)));
      }
    }
  }
  
  private void addMonsterShooter(int i){
    if(monsterShooter.size() < this.gn.getMaxMonsterShooter()){
      for(int c = 0; c < i; c++){
        monsterShooter.add(new Monster_shooter(this.player,respawn(monsterShotRad, true)));
      }
    }
  }
  
  private void addMonsterShooterBoss(){
    monsterShooter.add(new Monster_shooter(this.player,new PVector(WIDTH-50,50),20));
    monsterShooter.add(new Monster_shooter(this.player,new PVector(WIDTH-50,HEIGHT/2),20));
    monsterShooter.add(new Monster_shooter(this.player,new PVector(WIDTH-50,HEIGHT/3),20));
    monsterShooter.add(new Monster_shooter(this.player,new PVector(WIDTH-50,(HEIGHT/2)+100),20));
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50,HEIGHT-50),20));
  }
  
  private void addMeteo(int i){
    if(meteoritos.size() < this.gn.getMaxMeteoritos()){
      for(int c = 0; c < i; c++){
        meteoritos.add(new Meteorito(this.player,new PVector(0,0)));
      }
    }
  }
  
  //SPAWN EN LOS MARGENES
  private PVector respawn(float rad, boolean isMargin){    
    float rx = 0f;
    float ry = 0f;
    float angle = 0f;
    float disting = 0f;
    int h = 0;
    int w = 0;
    
    if(isMargin){
      h = 200;
      w = 200;
    }
    
    do{
      
      angle = random(-PI,PI); //ANGULO RANDOM
      disting = random(HEIGHT-h, WIDTH-w);
      rx = disting*cos(angle)+this.player.pos.x;
      ry = disting*sin(angle)+this.player.pos.y;
      rx = range(rx, rad, WIDTH-rad);
      ry = range(ry, rad, HEIGHT-rad);
    
    }while(PVector.dist(new PVector(rx, ry),this.player.pos)<=this.player.r*5/2+rad/2);
   
    return new PVector(rx,ry);
  }
  
  private PVector respawnInBoss(float rad){
    //CENTRO DE LA NAVE NODRIZA
    float rx = random(WIDTH,WIDTH-rad-50);
    float ry = random(HEIGHT/4,HEIGHT-(HEIGHT/4));
    
    return new PVector(rx, ry); 
  }

}
