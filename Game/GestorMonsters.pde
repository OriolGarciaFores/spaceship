class GestorMonsters {

  ArrayList <Monster_easy> monsterEasy = new ArrayList<Monster_easy>();
  int monsterEasyBornTimer;
  int monsterEasyBornDist = int(0.5*FRAMES);//Ratio de aparicion por frame
  float monsterEasyRad = 20f;//CODIGO REPETIDO

  ArrayList <Monster_shooter> monsterShooter = new ArrayList<Monster_shooter>();
  int monsterShotBornTimer;
  int monsterShotBornDist = int(3*FRAMES);
  float monsterShotRad = 20f;//CODIGO REPETIDO

  ArrayList<Meteorito> meteoritos = new ArrayList<Meteorito>();
  int meteoBornTimer;
  final int meteoBornDist = 45;
  float meteoRad = 30f;//CODIGO REPETIDO

  ArrayList <MonsterWifi> monsterWifi = new ArrayList<MonsterWifi>();
  int monsterWifiBornTimer;
  final int monsterWifiBornDist = int(0.5*FRAMES);
  float monsterWifiRad = 40f;//CODIGO REPETIDO

  private int bornShipsInBoss = 0;

  private MonsterBoss mb;
  private MonsterBossV2 mb2;
  private Player player;
  private GestorNiveles gn;
  private ArrayList<ParticleSystem> particlesSystems;

  public GestorMonsters(Player player, GestorNiveles gn) {
    this.player = player;
    this.monsterEasyBornTimer = 0;
    this.mb = new MonsterBoss(this.player, new PVector(WIDTH+20, 0));
    this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
    this.gn = gn;
    this.particlesSystems = new ArrayList<ParticleSystem>();
  }

  void update(ArrayList<Bala> balas) {

    updateMeteoritos(balas);
    updateMonsterEasy(balas);
    mechanicalBoss(balas);
    updateMonsterShooter(balas);
    updateMonsterWifi(balas);

    if (this.player.score < this.gn.getMaxScore()) {
      timer();
    }

    updateParticles();
    //PANTALLA DE RESULTADOS Y RESETEAR EL SCORE
    updateResults(balas);
  }

  private void mechanicalBoss(ArrayList<Bala> balas) {
    switch(this.gn.getLevel()) {
    case 1:
      if (this.player.score >= this.gn.getMaxScore() && (monsterEasy.isEmpty() && monsterShooter.isEmpty() || mb.getIsStarted()) && !mb.isDie) {
        mb.updateBoss(balas);
        if (!mb.getIsStarted()) {
          this.player.setAutoMove(true);
          mb.update();
        } else {
          this.player.setAutoMove(false);
          timerBoss(1);
        }
        mb.paint();
      }
      break;
    case 2:
      //BOSS 2n
      if (this.player.score >= this.gn.getMaxScore() && (monsterEasy.isEmpty() && monsterWifi.isEmpty() || mb2.getIsStarted()) && !mb2.isDie) {
        mb2.updateBoss(balas);
        if (!mb2.getIsStarted()) {
          this.player.setTargetAutoX(CENTRO_VENTANA_X-200);
          this.player.setAutoMove(true);
          mb2.update();
        } else {
          this.player.setAutoMove(false);
          timerBoss(2);
        }
        mb2.paint();
      }
      break;
    }
  }

  private void timer() {
    monsterEasyBornTimer++;
    if (monsterEasyBornTimer >= monsterEasyBornDist) {
      addMonsterEasy(1);
      monsterEasyBornTimer = 0;
    }
    monsterShotBornTimer++;
    if (monsterShotBornTimer >= monsterShotBornDist) {
      addMonsterShooter(1);
      monsterShotBornTimer = 0;
    }
    meteoBornTimer++;
    if (meteoBornTimer >= meteoBornDist) {
      addMeteo(1);
      meteoBornTimer = 0;
    }
    monsterWifiBornTimer++;
    if (monsterWifiBornTimer >= monsterWifiBornDist) {
      addMonsterWifi(1);
      monsterWifiBornTimer = 0;
    }
  }

  private void timerBoss(int idBoss) {
    switch(idBoss) {
      //1r BOSS
    case 1:
      //FASES
      switch(mb.getFase()) {
      case 1:
        if (bornShipsInBoss < 50) {
          monsterEasyBornTimer++;
          if (monsterEasyBornTimer >= monsterEasyBornDist) {
            bornShipsInBoss++;
            addMonsterEasyBoss(5);
            monsterEasyBornTimer = 0;
          }
        } else if (monsterEasy.isEmpty()) {
          mb.setFase(2);
        }
        if (!mb.loadShooters) {
          mb.loadShooters = true;
          addMonsterShooterBoss();
        }
        break;
      case 2:
        if (mb.shield <= 0) {
          mb.setFase(3);
        }
        break;
      case 3:
        if (monsterShooter.isEmpty()) {
          mb.setFase(4);
          mb.setShield(40);
        }
        break;
      case 4:
        //DAÑO A LA NAVE
        if (mb.shield <= 0) {
          this.player.setScore(mb.score);
          mb.isDie = true;
        }
        break;
      }
      break;
      //2n BOSS
    case 2:
      if (mb2.needShips && mb2.getShieldActive()) {
        addMonsterWifi(5);
        mb2.needShips = false;
      } else if (monsterWifi.isEmpty()) {
        mb2.setShieldActive(false);
        mb2.timerShield();
      }  
      break;
    }
  }

  private void addMonsterEasy(int i) {
    if (monsterEasy.size() < this.gn.getMaxMonsterEasy()) {
      for (int c = 0; c < i; c++) {
        monsterEasy.add(new Monster_easy(this.player, respawn(monsterEasyRad, false)));
      }
    }
  }

  private void addMonsterEasyBoss(int i) {
    if (monsterEasy.size() < 100) {
      for (int c = 0; c < i; c++) {
        monsterEasy.add(new Monster_easy(this.player, respawnInBoss(monsterEasyRad)));
      }
    }
  }

  private void addMonsterShooter(int i) {
    if (monsterShooter.size() < this.gn.getMaxMonsterShooter()) {
      for (int c = 0; c < i; c++) {
        monsterShooter.add(new Monster_shooter(this.player, respawn(monsterShotRad, true)));
      }
    }
  }

  private void addMonsterWifi(int i) {
    if (monsterWifi.size() < this.gn.getMaxMonsterWifi()) {
      for (int c = 0; c < i; c++) {
        monsterWifi.add(new MonsterWifi(this.player, respawn(monsterWifiRad, false)));
      }
    }
  }  

  private void addMonsterShooterBoss() {
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50, 50), 20));
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT/2), 20));
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT/3), 20));
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50, (HEIGHT/2)+100), 20));
    monsterShooter.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT-50), 20));
  }

  private void addMeteo(int i) {
    if (meteoritos.size() < this.gn.getMaxMeteoritos()) {
      for (int c = 0; c < i; c++) {
        if (this.player.score >= (this.gn.getMaxScore()/2)) {
          float y = random(20, HEIGHT-20);
          meteoritos.add(new Meteorito(this.player, new PVector(WIDTH+200, y), new PVector(-200, y), 'L'));
        } else {
          float x = random(20, WIDTH-20);
          meteoritos.add(new Meteorito(this.player, new PVector(x, -100), new PVector(x, HEIGHT+200), 'D'));
        }
      }
    }
  }

  //SPAWN EN LOS MARGENES
  private PVector respawn(float rad, boolean isMargin) {    
    float rx = 0f;
    float ry = 0f;
    float angle = 0f;
    float disting = 0f;
    int h = 0;
    int w = 0;

    if (isMargin) {
      h = 200;
      w = 200;
    }

    do {

      angle = random(-PI, PI); //ANGULO RANDOM
      disting = random(HEIGHT-h, WIDTH-w);
      rx = disting*cos(angle)+this.player.pos.x;
      ry = disting*sin(angle)+this.player.pos.y;
      rx = range(rx, rad, WIDTH-rad);
      ry = range(ry, rad, HEIGHT-rad);
    } while (PVector.dist(new PVector(rx, ry), this.player.pos)<=this.player.r*5/2+rad/2);

    return new PVector(rx, ry);
  }

  private PVector respawnInBoss(float rad) {
    //CENTRO DE LA NAVE NODRIZA
    float rx = random(WIDTH, WIDTH-rad-50);
    float ry = random(HEIGHT/4, HEIGHT-(HEIGHT/4));

    return new PVector(rx, ry);
  }

  private void updateParticles() {    
    for (int i = 0; i < particlesSystems.size(); i++) {
      ParticleSystem ps = particlesSystems.get(i);
      ps.update();
      if (ps.isEmpty()) {
        particlesSystems.remove(i);
      }
    }
  }

  private void updateMeteoritos(ArrayList<Bala> balas) {
    for (int i = 0; i < meteoritos.size(); i++) {
      Meteorito met = meteoritos.get(i);
      met.updateMet(balas);
      met.update();
      if (met.isDie) {
        this.player.setScore(met.score);
        meteoritos.remove(i);
      }
      met.paint();
    }
  }

  private void updateMonsterEasy(ArrayList<Bala> balas) {
    for (int i = 0; i < monsterEasy.size(); i++) {
      Monster_easy mEasy = monsterEasy.get(i);
      mEasy.updateEasy(balas);
      mEasy.update();
      if (mEasy.isDie) {
        ParticleSystem ps = new ParticleSystem(mEasy.pos);
        ps.addParticle(20, mEasy.c);
        this.particlesSystems.add(ps);
        this.player.setScore(mEasy.score);
        monsterEasy.remove(i);
      }
      mEasy.paint();
    }
  }

  private void updateMonsterShooter(ArrayList<Bala> balas) {
    for (int i = 0; i < monsterShooter.size(); i++) {
      Monster_shooter mShot = monsterShooter.get(i);
      mShot.updateShot(balas);
      mShot.update();
      if (mShot.isDie) {
        ParticleSystem ps = new ParticleSystem(mShot.pos);
        ps.addParticle(20, mShot.c);
        this.particlesSystems.add(ps);
        this.player.setScore(mShot.score);
        monsterShooter.remove(i);
      }
      mShot.paint();
    }
  }

  private void updateMonsterWifi(ArrayList<Bala> balas) {
    for (int i = 0; i < monsterWifi.size(); i++) {
      MonsterWifi mWifi = monsterWifi.get(i);
      mWifi.updateEasy(balas);
      mWifi.update();
      if (mWifi.isDie) {
        ParticleSystem ps = new ParticleSystem(mWifi.pos);
        ps.addParticle(20, mWifi.c);
        this.particlesSystems.add(ps);
        this.player.setScore(mWifi.score);
        monsterWifi.remove(i);
      }
      mWifi.paint();
    }
  }

  private void updateResults(ArrayList<Bala> balas) {
    switch(this.gn.getLevel()) {
    case 1:
      if (mb.isDie) {
        if (mb.animationDead) {
          animationDestroyBossRandom(30, (WIDTH-(WIDTH/4)), (WIDTH-20), 50, (HEIGHT-30));
          mb.animationDead = false;
        }
      }
      if (mb.isDie && this.particlesSystems.isEmpty()) {
        //procesing results -> finallvl, score, array balas, nextLvl, idBoss
        procesingResults(1, this.player.score, balas, 2, 1);
      }
      break;
    case 2:
      if (mb2.isDie && mb2.animationDead) {
        animationDestroyBoss(10, mb2.pos);
        mb2.animationDead = false;
      }
      if (mb2.isDie && this.particlesSystems.isEmpty()) {
        //procesing results -> finallvl, score, array balas, nextLvl, idBoss
        this.player.setScore(mb2.score);// ?¿?¿?¿?¿??¿
        procesingResults(2, this.player.score, balas, 3, 2);
      }
      break;
    }
  }

  private void animationDestroyBoss(int quantity, PVector pos) {
    for (int i = 0; i < quantity; i++) {
      ParticleSystem ps = new ParticleSystem(pos);
      ps.addParticle(300, color(255, 152, 15));
      this.particlesSystems.add(ps);
    }
  }

  private void animationDestroyBossRandom(int quantity, int rxInit, int rxFinal, int ryInit, int ryFinal) {
    for (int i = 0; i < quantity; i++) {
      ParticleSystem ps = new ParticleSystem(new PVector(random(rxInit, rxFinal), random(ryInit, ryFinal)));
      ps.addParticle(300, color(255, 152, 15));
      this.particlesSystems.add(ps);
    }
  }

  private void procesingResults(final int lvl, final int score, ArrayList<Bala> balas, final int nextLvl, final int idBoss) {
    finalLvl = lvl;
    finalScore = score;
    this.player.reset();
    balas.clear();
    this.gn.setLevel(nextLvl);
    resetBoss(idBoss);
    isLvlComplete = true;
  }

  private void resetBoss(final int idBoss) {
    switch(idBoss) {
    case 1:
      this.mb = new MonsterBoss(this.player, new PVector(WIDTH+20, 0));
      break;
    case 2:
      this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
      break;
    }
  }
}
