package core.gestores;

import core.utils.Global;

import static processing.core.PApplet.println;

public class GestorNiveles {
    //TODO Los Tipos de monstruos filtrarlos por un Map<tipo, cantidad>
    private int level;
    private int maxMonsterEasy;
    private int maxMonsterShooter;
    private int maxScore;
    private int maxMeteoritos;
    private int maxMonsterWifi;
    private int maxMonsterBomb;
    private int maxLevel;
    private int maxShooterV2;
    private int fase = 0;
    private final int MAX_FASES = 7;
    private float scoreMaxNextFase = 0;

    private GestorConfiguracion config;

    private boolean isFinalProgress;

    public GestorNiveles() {
        println("GESTOR NIVELES INICIALIZADO");
        init_monsters(0, 0, 0, 0, 0, 0);
        this.level = 1;
        this.config = new GestorConfiguracion();
        this.maxLevel = Integer.parseInt(this.config.getInfo("maxLevel"));
        this.fase = 0;
        this.isFinalProgress = false;
    }

    public void update() {
        updateLevel();
    }

    private void updateLevel() {
        switch (this.level) {
            case 1:
                //MONSTEREASY, SHOOTER, METEORITOS, WIFI, BOMB, shooter v2.
                init_monsters(20, 5, 0, 0, 0, 0);
                this.maxScore = 200;
                Global.gestorSonido.beforeStop();
                Global.gestorSonido.play(1);
                this.fase = 0;
                break;
            case 2:
                setMaxLevel(this.level);
                init_monsters(5, 0, 4, 8, 0, 0);
                this.maxScore = 400;
                Global.gestorSonido.beforeStop();
                Global.gestorSonido.play(2);
                this.fase = 0;
                break;
            case 3:
                setMaxLevel(this.level);
                init_monsters(30, 0, 0, 5, 1, 0);
                this.maxScore = 21;
                Global.gestorSonido.beforeStop();
                Global.gestorSonido.play(3);
                this.fase = 0;
                this.isFinalProgress = false;
                this.scoreMaxNextFase = this.maxScore / this.MAX_FASES;
                break;
            case 4:
                //NIVEL DE BOSS UNICO
                setMaxLevel(this.level);
                init_monsters(0, 0, 0, 0, 0, 0);
                this.maxScore = 0;
                Global.gestorSonido.beforeStop();
                Global.gestorSonido.play(3);
                this.fase = 0;
                this.isFinalProgress = false;
                break;
            default:
                this.level = 1;
                updateLevel();
                break;
        }
    }

    //A PARTIR DE LV3
    public void updateProgress(int scorePlayer) {
        if (!this.isFinalProgress && this.level >= 3 && scorePlayer >= this.scoreMaxNextFase) {
            avanzarFase();
            calcularScoreMaxNestFase();
            progresLvl();
        }
    }

    public float getScoreMaxNextFase() {
        return this.scoreMaxNextFase;
    }

    private void calcularScoreMaxNestFase() {
        this.scoreMaxNextFase = (this.maxScore / this.MAX_FASES) * (this.fase + 1);
    }

    //DAR PROGRESION DE DIFICULTAD EN LOS NIVELES 3 Y SUPERIORES
    //Switch case puede ser un metodo ilimitado. Rehacer el metodo de forma dinamica.
    private void progresLvl() {
        switch (this.level) {
            case 3:
                switch (this.fase) {
                    case 1:
                        //MONSTEREASY, SHOOTER, METEORITOS, WIFI, BOMB, shooter v2.
                        init_monsters(5, 0, 0, 3, 2, 1);
                        break;
                    case 2:
                        init_monsters(3, 0, 0, 2, 2, 1);
                        break;
                    case 3:
                        init_monsters(0, 0, 0, 8, 1, 2);
                        break;
                    case 4:
                        init_monsters(15, 0, 0, 0, 1, 2);
                        break;
                    case 5:
                        init_monsters(0, 0, 0, 15, 1, 1);
                        break;
                    case 6:
                        init_monsters(20, 0, 0, 15, 1, 1);
                        this.isFinalProgress = true;
                        break;
                }
                break;
            case 4:
                //FUTUROS LVLS
                break;
        }
    }

    private void avanzarFase() {
        this.fase++;
    }

    public void setLevel(int lv) {
        this.level = lv;
    }

    public int getLevel() {
        return this.level;
    }

    public int getMaxMonsterEasy() {
        return this.maxMonsterEasy;
    }

    public int getMaxMonsterShooter() {
        return this.maxMonsterShooter;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    public int getMaxMeteoritos() {
        return this.maxMeteoritos;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int getMaxMonsterBomb() {
        return this.maxMonsterBomb;
    }

    public int getMaxShooterV2() {
        return this.maxShooterV2;
    }

    public void setMaxLevel(int maxLvl) {
        if (this.maxLevel < maxLvl) {
            this.maxLevel = maxLvl;
            this.config.update("maxLevel", "" + this.maxLevel);
        }
    }

    public int getMaxMonsterWifi() {
        return this.maxMonsterWifi;
    }

    private void init_monsters(int me, int ms, int mm, int mw, int mb, int msv2) {
        this.maxMonsterEasy = me;
        this.maxMonsterShooter = ms;
        this.maxMeteoritos = mm;
        this.maxMonsterWifi = mw;
        this.maxMonsterBomb = mb;
        this.maxShooterV2 = msv2;
    }

    public void siguienteNivel(){
        this.level = this.level + 1;
    }
}
