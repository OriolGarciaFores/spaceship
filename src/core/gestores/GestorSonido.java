package core.gestores;

import core.utils.Constants;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.sound.SoundFile;

public class GestorSonido {
    private final SoundFile[] sounds = new SoundFile[4];
    private final float timeFrame = 30;
    private int keyTimer = 0;
    private boolean onChange = false;
    private float volume;
    private int volumeTotal = 25;

    public GestorSonido(PApplet applet) {
        this.volume = 0.25f;
        this.sounds[0] = new SoundFile(applet, "resources/Sounds/TitleScreen.wav");
        this.sounds[1] = new SoundFile(applet, "resources/Sounds/Level1.wav");
        this.sounds[2] = new SoundFile(applet, "resources/Sounds/Level2.wav");
        this.sounds[3] = new SoundFile(applet, "resources/Sounds/Level3.wav");
    }

    public void play(int id) {
        if (!this.sounds[id].isPlaying()) {
            this.sounds[id].loop();
            this.sounds[id].amp(this.volume);
        }
    }

    public void stop(int id) {
        if (this.sounds[id].isPlaying()) {
            this.sounds[id].stop();
        }
    }

    public void beforeStop(){
        for(int i = 0; i < this.sounds.length; i++){
            stop(i);
        }
    }

    public void update() {
        timer();
        if (onChange)changeVolume();
    }

    public void changeVolume() {
        if (Constants.KEYBOARD.left && this.volumeTotal > 0) {
            this.volumeTotal -= 5;
        } else if (Constants.KEYBOARD.right && this.volumeTotal < 100) {
            this.volumeTotal += 5;
        }
        this.volume = (float)this.volumeTotal/100;
        this.sounds[0].amp(this.volume);
    }

    public void paint(PGraphics graphics) {
        graphics.textSize(24f);
        graphics.textAlign(PApplet.CENTER);
        graphics.text("VOLUME: " + this.volumeTotal + "%", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y);
    }

    private void timer() {
        keyTimer++;
        if (keyTimer > timeFrame && (Constants.KEYBOARD.left || Constants.KEYBOARD.right)) {
            onChange = true;
            keyTimer = 0;
        } else {
            onChange = false;
        }
    }
}
