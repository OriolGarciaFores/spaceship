package core.gestores;

import core.beans.entidades.*;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

import static processing.core.PApplet.println;

public class GestorEnemies {

    private final int SHIP_BASIC_BORN_DIST = (int) (0.5 * Constants.FRAMES);
    private final int SHIP_SHOT_BORN_DIST = 3 * Constants.FRAMES;
    private final int meteoBornDist = (3 * Constants.FRAMES);
    private final int monsterWifiBornDist = (int) (0.5 * Constants.FRAMES);
    private final int bombBornDist = (3 * Constants.FRAMES);
    private final int shooterV2BornDist = (5 * Constants.FRAMES);

    //ARRAY INT[] -> IDENTIFICADOR DEL BICHO ES EL INDICE -> VALOR ACUMULADOR DE BICHOS VIVOS DE EL TIPO EN CONCRETO
    private final int[] monstersAlive = new int[2];

    //TODO CADA ENEMY DEBERIA INDICARLO SU CLASE.
    private int shipBasicBornTimer;
    private int monsterShotBornTimer;
    private int meteoBornTimer;
    private int monsterWifiBornTimer;
    private int bombBornTimer;
    private int shooterV2Timer;

    private PApplet parent;
    private GestorDisparos gestorDisparos;

    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    private int bornShipsInBoss = 0;

    //private MonsterBoss mb;
    //private MonsterBossV2 mb2;
    //private MonsterBossV3 mb3;
    private Player player;
    private ArrayList<GestorParticulas> gestorParticulas;

    public GestorEnemies(Player player, PApplet pApplet, GestorDisparos gestorDisparos) {
        this.monstersAlive[0] = 0;
        this.monstersAlive[1] = 0;
        //this.monstersAlive[2] = 0;
        //this.monstersAlive[3] = 0;
        //this.monstersAlive[4] = 0;
        //this.monstersAlive[5] = 0;

        this.player = player;
        this.gestorDisparos = gestorDisparos;
        //this.mb = new MonsterBoss(this.player, new PVector(WIDTH + 20, 0));
        //this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
        //this.mb3 = new MonsterBossV3(this.player, new PVector(WIDTH - BOSS_V3_RAD, CENTRO_VENTANA_Y));
        this.gestorParticulas = new ArrayList<GestorParticulas>();

        this.parent = pApplet;
    }

    public void update() {
        //mechanicalBoss(balas);
        updateEnemies();

        if (this.player.getScore() < Global.gestorNiveles.getMaxScore()) {
            invocarEnemies();
        }

        updateParticles();
        //PANTALLA DE RESULTADOS Y RESETEAR EL SCORE
        //if (this.player.getScore() >= Global.gestorNiveles.getMaxScore() && this.enemies.isEmpty()) updateResults(balas);
    }

    public void paint(PGraphics graphics) {
        paintMonsters(graphics);
        paintParticles(graphics);
    }

    private void paintMonsters(PGraphics graphics) {
        for (Enemy enemy : enemies) {
            enemy.paint(graphics);
        }
    }

    private void paintParticles(PGraphics graphics) {
        for (GestorParticulas gestorParticula : gestorParticulas) {
            gestorParticula.paint(graphics);
        }
    }

    //OPTIMIZAR VALIDACION DE MONSTER ALIVE -> La idea es que no haya monstruos al empezar el boss.

    /*private void mechanicalBoss(ArrayList<Bala> balas) {
        switch (gestorNiveles.getLevel()) {
            case 1:
                if (this.player.score >= gestorNiveles.getMaxScore() && (this.monstersAlive[0] == 0 && this.monstersAlive[1] == 0 || mb.getIsStarted()) && !mb.isDie) {
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
                if (this.player.score >= gestorNiveles.getMaxScore() && (this.monstersAlive[0] == 0 && this.monstersAlive[2] == 0 || mb2.getIsStarted()) && !mb2.isDie) {
                    mb2.updateBoss(balas);
                    if (!mb2.getIsStarted()) {
                        this.player.setTargetAutoX(CENTRO_VENTANA_X - 200);
                        this.player.setAutoMove(true);
                        mb2.update();
                    } else {
                        this.player.setAutoMove(false);
                        timerBoss(2);
                    }
                    mb2.paint();
                }
                break;
            case 4:
                if (!mb3.isDie) {
                    mb3.updateBoss(balas);
                    mb3.update();
                    timerBoss(3);
                    mb3.paint();
                }
                break;
        }
    }*/

    private void invocarEnemies() {
        if (Global.gestorNiveles.getMaxMonsterEasy() != 0) {
            shipBasicBornTimer++;
            if (shipBasicBornTimer >= SHIP_BASIC_BORN_DIST) {
                addShipBasic(1, false);
                shipBasicBornTimer = 0;
            }
        }
        if (Global.gestorNiveles.getMaxMonsterShooter() != 0) {
            monsterShotBornTimer++;
            if (monsterShotBornTimer >= SHIP_SHOT_BORN_DIST) {
                addShooter(1, false);
                monsterShotBornTimer = 0;
            }
        }
        if (Global.gestorNiveles.getMaxMeteoritos() != 0) {
            meteoBornTimer++;
            if (meteoBornTimer >= meteoBornDist) {
                //addMeteo(1, false);
                meteoBornTimer = 0;
            }
        }
        if (Global.gestorNiveles.getMaxMonsterWifi() != 0) {
            monsterWifiBornTimer++;
            if (monsterWifiBornTimer >= monsterWifiBornDist) {
                //addMonsterWifi(1, false);
                monsterWifiBornTimer = 0;
            }
        }
        if (Global.gestorNiveles.getMaxMonsterBomb() != 0) {
            bombBornTimer++;
            if (bombBornTimer >= bombBornDist) {
                //addBomb(1, false);
                bombBornTimer = 0;
            }
        }
        if (Global.gestorNiveles.getMaxShooterV2() != 0) {
            shooterV2Timer++;
            if (shooterV2Timer >= shooterV2BornDist) {
                //addShooterV2(1, false);
                shooterV2Timer = 0;
            }
        }
    }

    /*
    private void timerBoss(int idBoss) {
        switch (idBoss) {
            //1r BOSS
            case 1:
                //FASES
                switch (mb.getFase()) {
                    case 1:
                        if (bornShipsInBoss < 50) {
                            monsterEasyBornTimer++;
                            if (monsterEasyBornTimer >= monsterEasyBornDist) {
                                bornShipsInBoss++;
                                addMonsterEasyBoss(5);
                                monsterEasyBornTimer = 0;
                            }
                        } else if (this.monstersAlive[0] == 0) {
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
                        if (this.monstersAlive[1] == 0) {
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
                    addMonsterWifi(5, true);
                    mb2.needShips = false;
                } else if (this.monstersAlive[2] == 0 && !mb2.isRage) {
                    mb2.setShieldActive(false);
                    mb2.timerShield();
                }
                break;
            case 3:
                //3r BOSS
                switch (mb3.getFase()) {
                    case 1:
                        addShooterV2(2, true);
                        mb3.setFase(2);
                        break;
                    case 2:
                        if (mb3.getHealth() <= (mb3.getMaxHealth() - (mb3.getMaxHealth() / 4))) {
                            mb3.setFase(3);
                        }
                        if (this.monstersAlive[3] == 0)
                            meteoBornTimer++;
                        if (meteoBornTimer >= meteoBornDist) {
                            addMeteo(2, true);
                            meteoBornTimer = 0;
                        }
                        break;
                    case 3:
                        addShooterV2(4, true);
                        mb3.setFase(4);
                        break;
                    case 4:
                        if (mb3.getHealth() <= (mb3.getMaxHealth() / 2)) {
                            mb3.setFase(5);
                        }
                        break;
                    case 5:
                        addShooterV2(6, true);
                        mb3.setFase(6);
                        break;
                }
                //FASE 1 : Invocar 2 bichos. Boss Disparo normal. Meteoritos activados.
                //FASE 2 : Invocar 4 bichos. Boss activar bombas. Cada X tiempo rage. (Muchos disparos bombas).
                //FASE 3 : Invocar 6 bichos. Boss activar disparos en area.
                break;
        }
    }
*/
    private void addShipBasic(int i, boolean inBoss) {
        if (this.monstersAlive[0] < Global.gestorNiveles.getMaxMonsterEasy() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[0]++;
                enemies.add(new ShipBasic(this.player, respawn(Constants.ENEMY_SHIP, false)));
            }
        }
    }

    private void addShipBasicBoss(int i) {
        if (this.monstersAlive[0] < 100) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[0]++;
                enemies.add(new ShipBasic(this.player, respawnInBoss(Constants.ENEMY_SHIP)));
            }
        }
    }

    private void addShooter(int i, boolean inBoss) {
        if (this.monstersAlive[1] < Global.gestorNiveles.getMaxMonsterShooter() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[1]++;
                enemies.add(new Shooter(this.player, respawn(Constants.ENEMY_SHOOTER_RAD, true), this.gestorDisparos, this.parent));
            }
        }
    }
/*
    private void addMonsterWifi(int i, boolean inBoss) {
        if (this.monstersAlive[2] < gestorNiveles.getMaxMonsterWifi() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[2]++;
                monsters.add(new MonsterWifi(this.player, respawn(MONSTER_WIFI_RAD, false)));
            }
        }
    }

    private void addBomb(int i, boolean inBoss) {
        if (this.monstersAlive[4] < gestorNiveles.getMaxMonsterBomb() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[4]++;
                monsters.add(new Bomb(this.player, respawn(MONSTER_BOMB_RAD, false)));
            }
        }
    }

    private void addMonsterShooterBoss() {
        this.monstersAlive[1] = 5;
        monsters.add(new Monster_shooter(this.player, new PVector(WIDTH - 50, 50), 20));
        monsters.add(new Monster_shooter(this.player, new PVector(WIDTH - 50, HEIGHT / 2), 20));
        monsters.add(new Monster_shooter(this.player, new PVector(WIDTH - 50, HEIGHT / 3), 20));
        monsters.add(new Monster_shooter(this.player, new PVector(WIDTH - 50, (HEIGHT / 2) + 100), 20));
        monsters.add(new Monster_shooter(this.player, new PVector(WIDTH - 50, HEIGHT - 50), 20));
    }

    //REVISAR -> SOLO PARA NIVEL 2???¿?¿
    private void addMeteo(int i, boolean inBoss) {
        if (this.monstersAlive[3] < gestorNiveles.getMaxMeteoritos() || inBoss) {
            if (meteoBornDist > 50) {
                meteoBornDist -= 15;
            }
            for (int c = 0; c < i; c++) {
                this.monstersAlive[3]++;
                if (this.player.score >= (gestorNiveles.getMaxScore() / 2)) {
                    //meteoBornDist = 35;
                    float y = random(20, HEIGHT - 20);
                    monsters.add(new Meteorito(this.player, new PVector(WIDTH + 200, y), new PVector(-200, y), 'L'));
                } else {
                    float x = random(20, WIDTH - 20);
                    monsters.add(new Meteorito(this.player, new PVector(x, -100), new PVector(x, HEIGHT + 200), 'D'));
                }
            }
        }
    }

    private void addShooterV2(int i, boolean inBoss) {
        if (this.monstersAlive[5] < gestorNiveles.getMaxShooterV2() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[5]++;
                monsters.add(new ShooterV2(this.player, respawn(SHOOTER_V2_RAD, false)));
            }
        }
    }*/

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

            angle = this.parent.random(-PApplet.PI, PApplet.PI); //ANGULO RANDOM
            disting = this.parent.random(Constants.HEIGHT - h, Constants.WIDTH - w);
            rx = disting * PApplet.cos(angle) + this.player.pos.x;
            ry = disting * PApplet.sin(angle) + this.player.pos.y;
            rx = Util.range(rx, rad, Constants.WIDTH - rad);
            ry = Util.range(ry, rad, Constants.HEIGHT - rad);
        } while (PVector.dist(new PVector(rx, ry), this.player.pos) <= this.player.r * 5 / 2 + rad / 2);

        return new PVector(rx, ry);
    }

    private PVector respawnInBoss(float rad) {
        //CENTRO DE LA NAVE NODRIZA
        float rx = this.parent.random(Constants.WIDTH, Constants.WIDTH - rad - 50);
        float ry = this.parent.random(Constants.HEIGHT / 4, Constants.HEIGHT - (Constants.HEIGHT / 4));

        return new PVector(rx, ry);
    }

    private void updateParticles() {
        for (int i = gestorParticulas.size() - 1; i >= 0; i--) {
            GestorParticulas ps = gestorParticulas.get(i);
            ps.update();
            if (ps.isEmpty()) {
                gestorParticulas.remove(i);
            }
        }
    }

    private void updateEnemies() {
        if (enemies.size() > 0) {
            for (int i = enemies.size() - 1; i >= 0; i--) {
                enemies.get(i).update();
                enemies.get(i).timerShot();
                //enemies.get(i).updateColisions(balas);
                if (enemies.get(i).isDie) {
                    if (enemies.get(i).animationDestroy) {
                        GestorParticulas ps = new GestorParticulas(enemies.get(i).getPos());
                        ps.addParticle(20, enemies.get(i).getC(), this.parent);
                        this.gestorParticulas.add(ps);
                    }
                    this.player.setScore(enemies.get(i).score);
                    this.monstersAlive[enemies.get(i).id]--;
                    enemies.remove(i);
                }
            }
        }
    }
/*
    private void updateResults(ArrayList<Bala> balas) {
        switch (Global.gestorNiveles.getLevel()) {
            case 1:
                if (mb.isDie) {
                    if (mb.animationDead) {
                        animationDestroyBossRandom(30, (WIDTH - (WIDTH / 4)), (WIDTH - 20), 50, (HEIGHT - 30));
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
            case 3:
                procesingResults(3, this.player.score, balas, 4, 0);
                break;
            case 4:
                //RESULTS LVL 4.
                procesingResults(4, this.player.score, balas, 5, 3);
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
        gestorNiveles.setLevel(nextLvl);
        resetBoss(idBoss);
        isLvlComplete = true;
    }

    private void resetBoss(final int idBoss) {
        switch (idBoss) {
            case 1:
                this.mb = new MonsterBoss(this.player, new PVector(WIDTH + 20, 0));
                break;
            case 2:
                this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
                break;
            case 3:
                this.mb3 = new MonsterBossV3(this.player, new PVector(WIDTH - BOSS_V3_RAD, CENTRO_VENTANA_Y));
                break;
        }
    }*/

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
