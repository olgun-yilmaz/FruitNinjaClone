package com.olgunyilmaz.fruitninjaclone.android;

import android.content.Context;
import android.content.SharedPreferences;

public class DataSaver implements DataSaverHelper{
    private static final String PACKAGE_NAME = "com.olgunyilmaz.fruitninjaclone.android";
    private SharedPreferences sharedPreferences;

    public DataSaver(Context context){
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
    }
    @Override
    public void saveData(String key, int value) {
        sharedPreferences.edit().putInt(key,value).apply();
    }

    @Override
    public int getData(String key) {
        return sharedPreferences.getInt(key,0);
    }
}
