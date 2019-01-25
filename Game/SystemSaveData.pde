class SystemSaveData {
  private JSONObject json;
  private JSONArray data;
  private final String route = "data/Save/data.json";
  private boolean isLoad;
  private boolean isExist;
  
  private ArrayList<DataLvl> datas;
  
  public SystemSaveData(){
    this.isLoad = false;
    this.isExist = false;
    this.data = new JSONArray();
    this.json = new JSONObject();
    this.datas = new ArrayList<DataLvl>();
    init_json();
  }
  
  private void init_json(){
    try{
      loadJSON(); //<>//
      this.isLoad = true; //<>//
    }catch(Exception ex){
      JSONObject dataLvl = new JSONObject();
      
      dataLvl.setInt("Lv", 1);
      dataLvl.setInt("Score", 0);
      
      this.data.setJSONObject(0, dataLvl);
      this.json.setJSONArray("Scores", this.data);
      
      saveJSONObject(this.json,route);
      this.isLoad = false;
    }
  }
  
  ArrayList<DataLvl> loadData(){
    loadJSON();
    
    for(int i = 0; i < this.data.size(); i++){
      JSONObject dataScore = this.data.getJSONObject(i);
      
      int lvl = dataScore.getInt("Lv");
      int score = dataScore.getInt("Score");
      
      this.datas.add(new DataLvl(lvl, score));      
    }
    
    return this.datas;
  }
  
  void update(DataLvl data){
    loadJSON();
    
    for(int i = 0; i < this.data.size(); i++){
      JSONObject score = this.data.getJSONObject(i);

      if(score.getInt("Lv") == data.getLvl()){
        this.isExist = true;
        if(score.getInt("Score") < data.getScore()){
          score.setInt("Score", data.getScore());
          
          this.data.setJSONObject(i, score);
          this.json = new JSONObject();
          this.json.setJSONArray("Scores", this.data);
          saveJSONObject(this.json, route);
          this.isLoad = false;
          break;
        }
      }
    }
    
    if(!this.isExist){
      int size = this.data.size();
      JSONObject score = new JSONObject();
      
      score.setInt("Lv", data.getLvl());
      score.setInt("Score", data.getScore());
      
      this.data.setJSONObject(size, score);
      this.json.setJSONArray("Scores", this.data);
      saveJSONObject(this.json, route);
      this.isLoad = false;
    }else{
      this.isExist = false;
    }
    
  }
  
  private void loadJSON(){
    if(!this.isLoad){
      this.json = loadJSONObject(route);
      this.data = this.json.getJSONArray("Scores");    
    }
  }
}
