class DataLvl{
  private int lvl;
  private int score;
  
  DataLvl(int lvl, int score){
    this.lvl = lvl;
    this.score = score;
  }
  
  public void setLvl(int lvl){
    this.lvl = lvl;
  }
  
  public void setScore(int score){
    this.score = score;
  }
  
  public int getLvl(){
    return this.lvl;
  }
  
  public int getScore(){
    return this.score;
  }
}
