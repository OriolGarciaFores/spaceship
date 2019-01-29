class ParticleSystem {
  ArrayList<Particle> particles;
  PVector origin;

  ParticleSystem(PVector position) {
    this.origin = position.copy();
    particles = new ArrayList<Particle>();
  }

  void addParticle(int i, color c) {
    for (int y = 0; y < i; y++) {
      particles.add(new Particle(origin, c));
    }
  }

  void update() {
    for (int i = particles.size()-1; i >= 0; i--) {
      Particle p = particles.get(i);
      p.update();
      if (p.isDead()) {
        particles.remove(i);
      }
    }
  }
  
  boolean isEmpty(){
    if(particles.isEmpty()){
      return true;
    }else{
      return false;
    }
  }
}
