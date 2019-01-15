package com.example.education;

import android.content.SharedPreferences;

public class SharedPrefernces {

    private static final String APP_PREFERENCES = "mysettings";
    private String APP_PREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN";
    private SharedPreferences mSettings;

    public SharedPrefernces(SharedPreferences mSettings){
        setmSettings(mSettings);
    }

    public void setmSettings(SharedPreferences mSettings) {
        this.mSettings = mSettings;
    }


    public boolean isContains(){
        return mSettings.contains(APP_PREFERENCES_ACCESS_TOKEN);
    }

    public void PutAccessToken(String access_token){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_ACCESS_TOKEN, access_token);
        editor.apply();
    }

    public String getAccessToken(){
       return mSettings.getString(APP_PREFERENCES_ACCESS_TOKEN, "");
    }

    public void deleteToken(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.apply();
    }

}
