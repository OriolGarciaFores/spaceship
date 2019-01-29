import processing.sound.*;

class SystemSound {

  private final SoundFile[] sounds = new SoundFile[1];

  SystemSound(PApplet applet) {
    //sounds.add(new SoundFile(applet, "data/Sounds/Title.wav"));
    this.sounds[0] = new SoundFile(applet, "data/Sounds/Level1.wav");
    //sounds.add(new SoundFile(applet, "data/Sounds/Level2.wav"));
  }

  void play(int id) {
    if (!this.sounds[id].isPlaying()) {
      this.sounds[id].play();
      this.sounds[id].amp(0.1);
    }
  }

  void stop(int id) {
    this.sounds[id].stop();
  }
}
