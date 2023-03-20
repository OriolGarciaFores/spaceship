package core.beans.entidades.bosses;

import core.beans.entidades.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Boss extends Enemy {

    protected boolean isStarted;
    protected boolean animationDead;
    protected boolean modoInvocacion;
    protected boolean invocacionTimer;
    protected int fase = 1;
    protected Map<Integer, Integer> tiposShipsInvocacion = new HashMap<>();
    public abstract void updateBoss();

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isAnimationDead() {
        return animationDead;
    }

    public void setAnimationDead(boolean animationDead) {
        this.animationDead = animationDead;
    }

    public boolean isModoInvocacion() {
        return modoInvocacion;
    }

    public boolean isInvocacionTimer() {
        return invocacionTimer;
    }

    public Map<Integer, Integer> getTiposShipsInvocacion() {
        return tiposShipsInvocacion;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    public void siguienteFase() {
        this.fase = this.fase + 1;
    }
}
