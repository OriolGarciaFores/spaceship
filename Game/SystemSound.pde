import processing.sound.*;

class SystemSound {

  private final SoundFile[] sounds = new SoundFile[3];
  private final float timeFrame = 30;
  private int keyTimer = 0;
  private boolean onChange = false;
  private float volume;
  private int volumeTotal = 50;

  SystemSound(PApplet applet) {
    this.volume = 0.5f;
    this.sounds[0] = new SoundFile(applet, "data/Sounds/TitleScreen.wav");
    this.sounds[1] = new SoundFile(applet, "data/Sounds/Level1.wav");
    this.sounds[2] = new SoundFile(applet, "data/Sounds/Level2.wav");
  }

  void play(int id) {
    if (!this.sounds[id].isPlaying()) {
      this.sounds[id].loop();
      this.sounds[id].amp(this.volume);
    }
  }

  void stop(int id) {
    if (this.sounds[id].isPlaying()) {
      this.sounds[id].stop();
    }
  }
  
  void beforeStop(){
    for(int i = 0; i < this.sounds.length; i++){
     stop(i);
    }
  }

  void update() {
    timer();
    if (onChange)changeVolume();
    paint();
  }

  void changeVolume() {
    if (KEYBOARD.left && this.volumeTotal > 0) {
      this.volumeTotal -= 10;
    } else if (KEYBOARD.right && this.volumeTotal < 100) {
      this.volumeTotal += 10;
    }
    this.volume = (float)this.volumeTotal/100;
    this.sounds[0].amp(this.volume);
  }

  void paint() {
    textSize(24f);
    textAlign(CENTER);
    text("VOLUME: " + this.volumeTotal + "%", CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.left || KEYBOARD.right)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }
}
