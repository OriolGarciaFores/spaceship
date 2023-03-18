package core.gestores;

import core.beans.entidades.Bala;
import core.beans.entidades.Ball;
import core.beans.entidades.Player;
import core.utils.*;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class GestorDisparos {
    private final ArrayList<Bala> balasPlayer;
    private final ArrayList<Bala> balasEnemy;
    private final ArrayList<Ball> ballsEnemies;

    private final Player player;

    private int timerShoot = 0;
    private final float shootDist = Constants.FRAMES / 6;


    public GestorDisparos(Player player) {
        this.balasPlayer = new ArrayList<>();
        this.balasEnemy = new ArrayList<>();
        this.ballsEnemies = new ArrayList<>();
        this.player = player;
    }

    public void update() {
        timer();
        move();
    }

    private void timer() {
        timerShoot++;
        if (timerShoot >= shootDist) {
            setShotPlayer();
            timerShoot = 0;
        }
    }

    private void move() {
        if (!balasPlayer.isEmpty()) {
            for (int i = balasPlayer.size() - 1; i >= 0; i--) {
                Bala b = balasPlayer.get(i);
                b.update();

                if (b.isDie()) {
                    balasPlayer.remove(i);
                }
            }
        }

        if (!balasEnemy.isEmpty()) {
            for (int i = balasEnemy.size() - 1; i >= 0; i--) {
                Bala b = balasEnemy.get(i);
                b.update();

                if (b.isDie()) {
                    balasEnemy.remove(i);
                }
            }
        }

        if(!ballsEnemies.isEmpty()){
            for (int i = ballsEnemies.size() - 1; i >= 0; i--) {
                Ball ball = ballsEnemies.get(i);
                ball.update();

                if (ball.isDie()) {
                    ballsEnemies.remove(i);
                }
            }
        }
    }

    public void paint(PGraphics graphics) {
        if (!balasPlayer.isEmpty()) {
            for (Bala b : balasPlayer) {
                b.paint(graphics);
            }
        }

        if (!balasEnemy.isEmpty()) {
            for (Bala b : balasEnemy) {
                b.paint(graphics);
            }
        }

        if(!ballsEnemies.isEmpty()) {
            for (Ball ball : ballsEnemies){
                ball.paint(graphics);
            }
        }
    }

    public void setShotPlayer() {
        String direct = "";

        if (!Constants.KEYBOARD.shotUp && !Constants.KEYBOARD.shotDown && !Constants.KEYBOARD.shotLeft && !Constants.KEYBOARD.shotRight ||
                Constants.KEYBOARD.shotUp && Constants.KEYBOARD.shotDown || Constants.KEYBOARD.shotLeft && Constants.KEYBOARD.shotRight) {
            return;
        }

        if (Constants.KEYBOARD.shotUp) {
            direct += "U";
        }
        if (Constants.KEYBOARD.shotDown) {
            direct += "D";
        }
        if (Constants.KEYBOARD.shotLeft) {
            direct += "L";
        }
        if (Constants.KEYBOARD.shotRight) {
            direct += "R";
        }

        addBalaPlayer(this.player.pos, direct);
    }

    private void addBalaPlayer(PVector pos, String direct) {
        balasPlayer.add(new Bala(new PVector(pos.x, pos.y), direct));
    }

    public void addBalaEnemy(PVector pos, String direct) {
        balasEnemy.add(new Bala(new PVector(pos.x, pos.y), direct, Color.decode("#FAD91C")));
    }

    public void addBallEnemy(Ball ball){
        ballsEnemies.add(ball);
    }

    public ArrayList<Bala> getBalasPlayer() {
        return balasPlayer;
    }

    public ArrayList<Bala> getBalasEnemy() {
        return balasEnemy;
    }

    public ArrayList<Ball> getBallsEnemies() {
        return ballsEnemies;
    }
}
