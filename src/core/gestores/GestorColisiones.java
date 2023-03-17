package core.gestores;

import core.beans.entidades.Bala;
import core.beans.entidades.Ball;
import core.beans.entidades.Enemy;
import core.beans.entidades.Player;
import core.utils.Global;
import core.utils.Util;
import processing.core.PVector;

import java.util.ArrayList;

public class GestorColisiones {

    public void validarColisionesEnemies(Player player, ArrayList<Bala> balasPlayer, ArrayList<Enemy> enemies, ArrayList<Bala> balasEnemies) {

        for (Enemy enemy : enemies) {
            if (enemy.isInmortal()) continue;

            if (PVector.dist(enemy.getPos(), player.pos) <= player.r / 2 + enemy.rad / 2) {
                player.decreaseLife();
            }

            int i = 0;
            while (!Global.over && i < balasEnemies.size()) {
                //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
                if (PVector.dist(player.pos, balasEnemies.get(i).getPos()) <= balasEnemies.get(i).getRad() / 2 + player.r / 2) {
                    balasEnemies.get(i).setDie(true);
                    player.decreaseLife();
                }
                i++;
            }

            i = 0;

            while (!enemy.isDie && i < balasPlayer.size()) {
                //INTERSECCION ENTRE BALA Y BICHO
                if (PVector.dist(enemy.getPos(), balasPlayer.get(i).getPos()) <= balasPlayer.get(i).getRad() / 2 + enemy.rad / 2) {
                    balasPlayer.get(i).setDie(true);
                    enemy.isDie = true;
                }
                i++;
            }

            i = 0;

            if (player.getHability(1).isEquiped) {
                ArrayList<Ball> balls = Util.ObjectsToBalls(player);

                while (!enemy.isDie && i < balls.size()) {
                    //INTERSECCION ENTRE BALA Y BICHO
                    if (PVector.dist(enemy.getPos(), balls.get(i).getPos()) <= balls.get(i).getRad() / 2 + enemy.rad / 2) {
                        balls.get(i).setDie(true);
                        enemy.isDie = true;
                    }
                    i++;
                }
            }
        }
    }
}
