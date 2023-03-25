package core.gestores;

import core.utils.Constants;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.HashMap;
import java.util.Map;

public class Logger {

    private Map<String, Long> logsRendimiento;
    private PGraphics graphics;

    public Logger(PGraphics graphics){
        logsRendimiento = new HashMap<>();
        this.graphics = graphics;
    }

    public void inicializarLogRendimiento(String key){
        if(!Constants.DEBUG) return;
        Long value = System.currentTimeMillis();
        logsRendimiento.put(key, value);
    }

    public void finalizarLogRendimiento(String key){
        if(!Constants.DEBUG) return;
        Long value = System.currentTimeMillis();
        Long valueDiferencia = value - logsRendimiento.get(key);
        logsRendimiento.put(key, valueDiferencia);
    }

    public void pintarLogsRendimiento(){
        if(!Constants.DEBUG) return;
        int posicionY = 35;
        for (String key : logsRendimiento.keySet()) {
            graphics.fill(57, 255, 20);
            graphics.textAlign(PConstants.BASELINE);
            graphics.textSize(18);
            graphics.text(key + ": " + logsRendimiento.get(key) + " ms", 20, posicionY);
            posicionY = posicionY + 15;
        }
    }
}
