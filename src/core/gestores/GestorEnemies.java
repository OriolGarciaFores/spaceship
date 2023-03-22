package core.gestores;

import core.beans.entidades.*;
import core.beans.entidades.bosses.Boss;
import core.beans.entidades.bosses.DeathStar;
import core.beans.entidades.bosses.Destroyer;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PApplet.println;

public class GestorEnemies {

    private final int SHIP_BASIC_BORN_DIST = (int) (0.5 * Constants.FRAMES);
    private final int SHIP_SHOT_BORN_DIST = 3 * Constants.FRAMES;
    private int meteoBornDist = (3 * Constants.FRAMES);
    private final int monsterWifiBornDist = (int) (0.5 * Constants.FRAMES);
    private final int bombBornDist = (3 * Constants.FRAMES);
    private final int shooterV2BornDist = (5 * Constants.FRAMES);

    //ARRAY INT[] -> IDENTIFICADOR DEL BICHO ES EL INDICE -> VALOR ACUMULADOR DE BICHOS VIVOS DE EL TIPO EN CONCRETO
    private final int[] monstersAlive = new int[6];

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
    private int totalMaxShipsInBoss = 0;
    private int totalShipsInBoss = 0;

    private boolean shipsInvocados = false;

    //private Destroyer destroyer;
    //private MonsterBossV2 mb2;
    //private MonsterBossV3 mb3;
    private Player player;
    private ArrayList<GestorParticulas> gestorParticulas;
    private Map<Integer, Boss> bosses = new HashMap<>();

    public GestorEnemies(Player player, PApplet pApplet, GestorDisparos gestorDisparos) {
        this.monstersAlive[0] = 0;
        this.monstersAlive[1] = 0;
        this.monstersAlive[2] = 0;
        this.monstersAlive[3] = 0;
        this.monstersAlive[4] = 0;
        this.monstersAlive[5] = 0;

        this.player = player;
        this.gestorDisparos = gestorDisparos;
        this.parent = pApplet;

        Destroyer destroyer = new Destroyer(this.player, new PVector(Constants.WIDTH + 20, 0));
        DeathStar deathStar = new DeathStar(this.player, new PVector(Constants.CENTRO_VENTANA_X, -100), this.parent, this.gestorDisparos);
        //this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
        //this.mb3 = new MonsterBossV3(this.player, new PVector(WIDTH - BOSS_V3_RAD, CENTRO_VENTANA_Y));
        bosses.put(1, destroyer);
        bosses.put(2, deathStar);

        this.gestorParticulas = new ArrayList<GestorParticulas>();
        this.shipsInvocados = false;
    }

    public void update() {
        updateBoss();
        updateEnemies();

        if (this.player.getScore() < Global.gestorNiveles.getMaxScore()) {
            invocarEnemies();
        }

        updateParticles();
        //PANTALLA DE RESULTADOS Y RESETEAR EL SCORE
        if (this.player.getScore() >= Global.gestorNiveles.getMaxScore() && this.enemies.isEmpty()) updateResults();
    }

    public void paint(PGraphics graphics) {
        paintBoss(graphics);
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

    private void updateBoss(){
        if(this.player.getScore() >= Global.gestorNiveles.getMaxScore()){
            Boss boss = bosses.get(Global.gestorNiveles.getLevel());

            if (boss != null && (isDeadAllEnemies() || boss.isStarted()) && !boss.isDie) {
                boss.updateBoss();

                if(!boss.isStarted()){
                    if(boss.getTarget().equals(player.getTargetAuto())) {
                        this.player.setTargetAutoX(Constants.CENTRO_VENTANA_X - 200);
                    }

                    this.player.setAutoMove(true);
                    boss.update();
                } else {
                    this.player.setAutoMove(false);
                    invocacionesInBoss(boss);
                    controlesEspecificos(boss);
                }
            }
        }
    }

    private boolean isDeadAllEnemies(){
        for (int i = 0; i < monstersAlive.length; i++) {
            if(monstersAlive[i] > 0) return false;
        }
        return true;
    }

    private void mechanicalBoss(ArrayList<Bala> balas) {
        /*switch (Global.gestorNiveles.getLevel()) {
            case 1:
                if (this.player.getScore() >= Global.gestorNiveles.getMaxScore() && (this.monstersAlive[0] == 0 && this.monstersAlive[1] == 0
                        || destroyer.getIsStarted()) && !destroyer.isDie) {
                    destroyer.updateBoss(balas);
                    if (!destroyer.getIsStarted()) {
                        this.player.setAutoMove(true);
                        destroyer.update();
                    } else {
                        this.player.setAutoMove(false);
                        timerBoss(1);
                    }
                }
                break;
            /*case 2:
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
                break;*/
       // }
    }

    private void paintBoss(PGraphics graphics) {
        if(this.player.getScore() >= Global.gestorNiveles.getMaxScore()){
            Boss boss = bosses.get(Global.gestorNiveles.getLevel());

            if (boss != null && (isDeadAllEnemies() || boss.isStarted()) && !boss.isDie) {
                boss.paint(graphics);
            }
        }
    }

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
                addMeteorito(1, false);
                meteoBornTimer = 0;
            }
        }

        if (Global.gestorNiveles.getMaxMonsterWifi() != 0) {
            monsterWifiBornTimer++;
            if (monsterWifiBornTimer >= monsterWifiBornDist) {
                addShipWifi(1, false);
                monsterWifiBornTimer = 0;
            }
        }

        if (Global.gestorNiveles.getMaxMonsterBomb() != 0) {
            bombBornTimer++;
            if (bombBornTimer >= bombBornDist) {
                addBomba(1, false);
                bombBornTimer = 0;
            }
        }

        if (Global.gestorNiveles.getMaxShooterV2() != 0) {
            shooterV2Timer++;
            if (shooterV2Timer >= shooterV2BornDist) {
                addShipV2(1, false);
                shooterV2Timer = 0;
            }
        }
    }

    private void invocacionesInBoss(Boss boss){
        if(boss.isModoInvocacion()){
            Map<Integer, Integer> tiposInvocacion = boss.getTiposShipsInvocacion();

            //Esto se deberia mejorar, podria ser parcialmente con timer y no todos.
            if(boss.isInvocacionTimer()){
                totalMaxShipsInBoss = 0;

                for (Integer tipo : tiposInvocacion.keySet()){
                    totalMaxShipsInBoss = totalMaxShipsInBoss + tiposInvocacion.get(tipo);
                    switch(tipo) {
                        case 0:
                            if (bornShipsInBoss < tiposInvocacion.get(tipo)) {
                                shipBasicBornTimer++;
                                if (shipBasicBornTimer >= SHIP_BASIC_BORN_DIST) {
                                    bornShipsInBoss++;
                                    totalShipsInBoss++;
                                    addShipBasicBoss(5);//Cantidad simultanea?
                                    shipBasicBornTimer = 0;
                                }
                            }
                            break;
                    }
                }

                if(totalMaxShipsInBoss == totalShipsInBoss && isDeadAllEnemiesInBoss(tiposInvocacion)){
                    boss.siguienteFase();
                }

            } else {
                if(!shipsInvocados) {
                    for (Integer tipo : tiposInvocacion.keySet()){
                        shipsInvocados = true;

                        switch(tipo) {
                            case 0:
                                addShipBasic(tiposInvocacion.get(tipo), true);
                                break;
                            case 2:
                                addShipWifi(tiposInvocacion.get(tipo), true);
                                break;
                        }
                    }
                }

                if(isDeadAllEnemiesInBoss(tiposInvocacion)){
                    boss.siguienteFase();
                    shipsInvocados = false;
                }
            }
        } else {
            bornShipsInBoss = 0;
            shipsInvocados = false;
        }
    }

    private void controlesEspecificos(Boss boss){
        //Excepciones de casos especificos
        if(boss instanceof Destroyer){
            Destroyer destroyer = (Destroyer) boss;
            if(!destroyer.isLoadShooters()) {
                destroyer.setLoadShooters(true);
                addShooterBoss();
            }

            if (this.monstersAlive[1] == 0) {
                boss.siguienteFase();
            }
        }
    }

    private boolean isDeadAllEnemiesInBoss(Map<Integer, Integer> tiposInvocacion){
        for (Integer tipo : tiposInvocacion.keySet()){
            if(this.monstersAlive[tipo] != 0){
                return false;
            }
        }
        return true;
    }

//TODO REVISAR EL FASEO DEBERIA ESTAR INCLUIDO AL BOSS.
    private void timerBoss(Boss boss) {
        switch (boss.id) {
            //1r BOSS
            case 1:
                Destroyer destroyer = (Destroyer) boss;
                //FASES
                switch (destroyer.getFase()) {
                    case 1:
                        if (bornShipsInBoss < 50) {
                            shipBasicBornTimer++;
                            if (shipBasicBornTimer >= SHIP_BASIC_BORN_DIST) {
                                bornShipsInBoss++;
                                addShipBasicBoss(5);
                                shipBasicBornTimer = 0;
                            }
                        } else if (this.monstersAlive[0] == 0) {
                            destroyer.setFase(2);
                        }
                        if (!destroyer.isLoadShooters()) {
                            destroyer.setLoadShooters(true);
                            addShooterBoss();
                        }
                        break;
                    case 2:
                        if (destroyer.getShield() <= 0) {
                            destroyer.setFase(3);
                        }
                        break;
                    case 3:
                        if (this.monstersAlive[1] == 0) {
                            destroyer.setFase(4);
                            destroyer.setShield(40);
                        }
                        break;
                    case 4:
                        //DAÑO A LA NAVE
                        if (destroyer.getShield() <= 0) {
                            this.player.setScore(destroyer.score);
                            destroyer.isDie = true;
                        }
                        break;
                }
                break;
            //2n BOSS
            /*case 2:
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
                break;*/
        }
    }

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

    private void addShipWifi(int i, boolean inBoss) {
        if (this.monstersAlive[2] < Global.gestorNiveles.getMaxMonsterWifi() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[2]++;
                enemies.add(new ShipWifi(this.player, respawn(Constants.ENEMY_SHIP_WIFI_RAD, false)));
            }
        }
    }

    private void addBomba(int i, boolean inBoss) {
        if (this.monstersAlive[4] < Global.gestorNiveles.getMaxMonsterBomb() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[4]++;
                enemies.add(new Bomba(this.player, respawn(Constants.BOMBA_RAD, false), this.gestorDisparos));
            }
        }
    }

    private void addShooterBoss() {
        this.monstersAlive[1] = 5;
        enemies.add(new Shooter(this.player, new PVector(Constants.WIDTH - 50, 50), 20, this.gestorDisparos, this.parent));
        enemies.add(new Shooter(this.player, new PVector(Constants.WIDTH - 50, Constants.HEIGHT / 2), 20, this.gestorDisparos, this.parent));
        enemies.add(new Shooter(this.player, new PVector(Constants.WIDTH - 50, Constants.HEIGHT / 3), 20, this.gestorDisparos, this.parent));
        enemies.add(new Shooter(this.player, new PVector(Constants.WIDTH - 50, (Constants.HEIGHT / 2) + 100), 20, this.gestorDisparos, this.parent));
        enemies.add(new Shooter(this.player, new PVector(Constants.WIDTH - 50, Constants.HEIGHT - 50), 20, this.gestorDisparos, this.parent));
    }


    private void addMeteorito(int i, boolean inBoss) {
        if (this.monstersAlive[3] < Global.gestorNiveles.getMaxMeteoritos() || inBoss) {
            if (meteoBornDist > 50) {
                meteoBornDist -= 15;
            }
            for (int c = 0; c < i; c++) {
                this.monstersAlive[3]++;
                if (this.player.getScore() >= (Global.gestorNiveles.getMaxScore() / 2)) {
                    float y = this.parent.random(20, Constants.HEIGHT - 20);
                    enemies.add(new Meteorito(this.player, new PVector(Constants.WIDTH + 200, y), new PVector(-200, y), 'L'));
                } else {
                    float x = this.parent.random(20, Constants.WIDTH - 20);
                    enemies.add(new Meteorito(this.player, new PVector(x, -100), new PVector(x, Constants.HEIGHT + 200), 'D'));
                }
            }
        }
    }

    private void addShipV2(int i, boolean inBoss) {
        if (this.monstersAlive[5] < Global.gestorNiveles.getMaxShooterV2() || inBoss) {
            for (int c = 0; c < i; c++) {
                this.monstersAlive[5]++;
                enemies.add(new ShipV2(this.player, respawn(Constants.SHIP_V2_RAD, false), this.gestorDisparos));
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

                if (enemies.get(i).isDie) {
                    if (enemies.get(i).animationDestroy) {
                        GestorParticulas ps = new GestorParticulas(enemies.get(i).getPos());
                        ps.addParticle(20, enemies.get(i).getC(), this.parent);
                        this.gestorParticulas.add(ps);
                    }

                    enemies.get(i).afterDie();
                    this.player.setScore(enemies.get(i).score);
                    this.monstersAlive[enemies.get(i).id]--;
                    enemies.remove(i);
                }
            }
        }
    }

    private void updateResults() {
        Boss boss;

        switch (Global.gestorNiveles.getLevel()) {
            case 1:
                boss = this.bosses.get(1);

                if (boss.isDie) {
                    if (boss.isAnimationDead()) {
                        animationDestroyBossRandom(30, (Constants.WIDTH - (Constants.WIDTH / 4)), (Constants.WIDTH - 20), 50, (Constants.HEIGHT - 30));
                        boss.setAnimationDead(false);
                    }
                }

                if (boss.isDie && this.gestorParticulas.isEmpty()) {
                    procesingResults();
                }
                break;
            default:
                boss = this.bosses.get(Global.gestorNiveles.getLevel());

                if(boss != null){
                    if(boss.isDie) {
                        if(boss.isAnimationDead()){
                            animationDestroyBoss(10, boss.getPos());
                            boss.setAnimationDead(false);
                        }

                        if(this.gestorParticulas.isEmpty()){
                            procesingResults();
                        }
                    }
                } else {
                    procesingResults();
                }
                break;
        }
    }

    private void animationDestroyBoss(int quantity, PVector pos) {
        for (int i = 0; i < quantity; i++) {
            GestorParticulas ps = new GestorParticulas(pos);
            ps.addParticle(300, new Color(255, 152, 15), this.parent);
            this.gestorParticulas.add(ps);
        }
    }

    private void animationDestroyBossRandom(int quantity, int rxInit, int rxFinal, int ryInit, int ryFinal) {
        for (int i = 0; i < quantity; i++) {
            GestorParticulas ps = new GestorParticulas(new PVector(parent.random(rxInit, rxFinal), parent.random(ryInit, ryFinal)));
            ps.addParticle(300, new Color(255, 152, 15), this.parent);
            this.gestorParticulas.add(ps);
        }
    }

    private void procesingResults() {
        Global.finalLvl = Global.gestorNiveles.getLevel();
        Global.finalScore = player.getScore();
        this.player.reset();
        Global.gestorNiveles.siguienteNivel();
        //resetBoss(idBoss); Igual si quisiera repetir bosses hace falta un reset. Por ahora no. Modo survival?
        Global.isLvlComplete = true;
    }

    /*private void resetBoss(final int idBoss) {
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

    public Map<Integer, Boss> getBosses() {
        return bosses;
    }
}
