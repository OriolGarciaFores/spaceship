package core.gestores;

import core.beans.comunes.DataNivel;
import core.utils.*;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public class GestorSaveData {
    private JSONObject json;
    private JSONArray data;
    private PApplet parent;

    private boolean isLoad;
    private boolean isExist;
    boolean isSaved;

    private ArrayList<DataNivel> datas;

    public GestorSaveData(PApplet pApplet) {
        this.isLoad = false;
        this.isExist = false;
        this.isSaved = false;
        this.data = new JSONArray();
        this.json = new JSONObject();
        this.datas = new ArrayList<DataNivel>();
        this.parent = pApplet;
        init_json();
    }

    private void init_json() {
        try {
            loadJSON();
            this.isLoad = true;
        } catch (Exception ex) {
            JSONObject dataLvl = new JSONObject();

            dataLvl.setInt("Lv", 1);
            dataLvl.setInt("Score", 0);

            this.data.setJSONObject(0, dataLvl);
            this.json.setJSONArray("Scores", this.data);

            parent.saveJSONObject(this.json, Constants.ROUTE_SAVE);
            this.isLoad = false;
        }
    }

    public ArrayList<DataNivel> loadData() {
        loadJSON();

        for (int i = 0; i < this.data.size(); i++) {
            JSONObject dataScore = this.data.getJSONObject(i);

            int lvl = dataScore.getInt("Lv");
            int score = dataScore.getInt("Score");

            this.datas.add(new DataNivel(lvl, score));
        }

        return this.datas;
    }

    public void update(DataNivel data) {
        loadJSON();

        for (int i = 0; i < this.data.size(); i++) {
            JSONObject score = this.data.getJSONObject(i);

            if (score.getInt("Lv") == data.getLvl()) {
                this.isExist = true;
                if (score.getInt("Score") < data.getScore()) {
                    score.setInt("Score", data.getScore());

                    this.data.setJSONObject(i, score);
                    this.json = new JSONObject();
                    this.json.setJSONArray("Scores", this.data);
                    parent.saveJSONObject(this.json, Constants.ROUTE_SAVE);
                    this.isLoad = false;
                    break;
                }
            }
        }

        if (!this.isExist) {
            int size = this.data.size();
            JSONObject score = new JSONObject();

            score.setInt("Lv", data.getLvl());
            score.setInt("Score", data.getScore());

            this.data.setJSONObject(size, score);
            this.json.setJSONArray("Scores", this.data);
            parent.saveJSONObject(this.json, Constants.ROUTE_SAVE);
            this.isLoad = false;
        } else {
            this.isExist = false;
        }

    }

    private void loadJSON() {
        if (!this.isLoad) {
            this.json = parent.loadJSONObject(Constants.ROUTE_SAVE);
            this.data = this.json.getJSONArray("Scores");
        }
    }
}
