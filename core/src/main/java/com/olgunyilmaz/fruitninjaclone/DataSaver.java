package com.olgunyilmaz.fruitninjaclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DataSaver {
    public Preferences preferences;
    private static final String KEY = "highScore";
    private static final String PREF_NAME = "FruitNinjaPrefs";

    public DataSaver(){
        preferences = Gdx.app.getPreferences(PREF_NAME);
    }

    public void saveData(int highScore){
        preferences.putInteger(KEY,highScore);
        preferences.flush();
    }

    public int getData(){
        return preferences.getInteger(KEY,0);
    }
}
