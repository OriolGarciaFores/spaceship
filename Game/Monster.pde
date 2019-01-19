class Monster{
 protected PVector pos, speed, acc;
 protected float maxSpeed;
 protected color c;
 protected PVector target;
 protected int health;
 
 protected Player player;
 protected int score;
 
 float rad = 20f;
 
 protected boolean isDie = false;
 
 public void setPlayer(Player player){
   this.player = player;
 }
 
 public void setLoc(PVector loc){
   this.pos = new PVector(loc.x,loc.y);
 }
 
 public void setTarget(PVector ta){
   this.target = new PVector(ta.x, ta.y);
 }
 
 public void move(){
  calAcc();
  calSpeed();
  calPos();
  this.acc = new PVector();
 }
 
 private void calAcc(){
   PVector findTarget = new PVector(this.target.x-this.pos.x,this.target.y-this.pos.y);
   this.acc.add(findTarget);
 }
 
 private void calSpeed(){
   this.speed.add(this.acc);
   this.speed.limit(this.maxSpeed);
 }
 
 private void calPos(){
   this.pos.add(this.speed);
 }
 
 public void update(){
   setTarget(player.pos);
   move();
 }
 
}
