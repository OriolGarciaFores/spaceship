package core.gestores;

import core.beans.entidades.Bala;
import core.beans.entidades.Ball;
import core.beans.entidades.Enemy;
import core.beans.entidades.Player;
import core.beans.entidades.bosses.Boss;
import core.utils.Global;
import core.utils.Util;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GestorColisiones {

    private Player player;

    public GestorColisiones(Player player) {
        this.player = player;
    }

    public void validarColisionesEnemies(ArrayList<Bala> balasPlayer, ArrayList<Enemy> enemies) {

        for (Enemy enemy : enemies) {
            if (enemy.isInmortal()) continue;

            if (PVector.dist(enemy.getPos(), player.pos) <= player.r / 2 + enemy.getRad() / 2) {
                player.decreaseLife();
            }

            int i = 0;

            while (!enemy.isDie && i < balasPlayer.size()) {
                //INTERSECCION ENTRE BALA Y BICHO
                if (PVector.dist(enemy.getPos(), balasPlayer.get(i).getPos()) <= balasPlayer.get(i).getRad() / 2 + enemy.getRad() / 2) {
                    balasPlayer.get(i).setDie(true);
                    if (enemy.isDestructible()) enemy.decreaseLife();
                }
                i++;
            }

            i = 0;

            if (player.getHability(1).isEquiped) {
                ArrayList<Ball> balls = Util.ObjectsToBalls(player);

                while (!enemy.isDie && i < balls.size()) {
                    //INTERSECCION ENTRE BALA Y BICHO
                    if (PVector.dist(enemy.getPos(), balls.get(i).getPos()) <= balls.get(i).getRad() / 2 + enemy.getRad() / 2) {
                        balls.get(i).setDie(true);
                        if (enemy.isDestructible()) enemy.decreaseLife();
                    }
                    i++;
                }
            }
        }
    }

    public void validarColisionesBalasEnemies(ArrayList<Bala> balasEnemies){
        int i = 0;

        while (!Global.over && i < balasEnemies.size()) {
            //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
            if (PVector.dist(player.pos, balasEnemies.get(i).getPos()) <= balasEnemies.get(i).getRad() / 2 + player.r / 2) {
                balasEnemies.get(i).setDie(true);
                player.decreaseLife();
            }
            i++;
        }
    }

    public void validarColisionesBallsEnemies(ArrayList<Ball> ballsEnemies){
        int i = 0;

        while (!Global.over && i < ballsEnemies.size()) {
            //INTERSECCION ENTRE BALA Y BICHO
            if (PVector.dist(player.pos, ballsEnemies.get(i).getPos()) <= ballsEnemies.get(i).getRad() / 2 + player.r / 2) {
                ballsEnemies.get(i).setDie(true);
                player.decreaseLife();
            }
            i++;
        }
    }

    public void validarColisionesBosses(Map<Integer, Boss> bosses, ArrayList<Bala> balasPlayer){
        Boss boss = bosses.get(Global.gestorNiveles.getLevel());

        if (boss != null && boss.isStarted() && !boss.isDie) {

            if (PVector.dist(boss.getPos(), this.player.pos) <= this.player.r / 2 + boss.getRad() / 2) {
                player.decreaseLife();
            }

            //Puede el boss contener fases que no se desee impacto a hitbox entera.
            if(!boss.isProyectilColisionable()) return;

            float radioImpacto = 0f;
            int i = 0;
            while (i < balasPlayer.size()) {
                if(boss.isSoloPuntoDebil() && !boss.isShieldActive()) radioImpacto = boss.getRadPuntoDebil();
                else radioImpacto = boss.getRad();
                //INTERSECCION ENTRE BALA Y BICHO
                if (PVector.dist(boss.getPos(), balasPlayer.get(i).getPos()) <= balasPlayer.get(i).getRad() / 2 + radioImpacto / 2) {
                    balasPlayer.get(i).setDie(true);
                    if (boss.isDestructible()) boss.decreaseLife();
                }

                i++;
            }

            i = 0;

            if (player.getHability(1).isEquiped) {
                ArrayList<Ball> balls = Util.ObjectsToBalls(player);

                while (i < balls.size()) {
                    if(boss.isSoloPuntoDebil() && !boss.isShieldActive()) radioImpacto = boss.getRadPuntoDebil();
                    else radioImpacto = boss.getRad();
                    //INTERSECCION ENTRE BALA Y BICHO
                    if (PVector.dist(boss.getPos(), balls.get(i).getPos()) <= balls.get(i).getRad() / 2 + radioImpacto / 2) {
                        balls.get(i).setDie(true);
                        if (boss.isDestructible()) boss.decreaseLife();
                    }

                    i++;
                }
            }

        }
    }
}
