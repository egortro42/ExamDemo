package com.example.education.Presenters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.education.GitHub.RetrofitClient;
import com.example.education.Models.GitLogoutModel;
import com.example.education.SharedPrefernces;

public class GitLogoutPresenter {
    private static SharedPrefernces token_prefernces;
    private static SharedPreferences mSettings;
    private static Activity activity;

    public GitLogoutPresenter(SharedPreferences mSettings, Activity some_activity){
        setmSettings(mSettings);
        token_prefernces = new SharedPrefernces(mSettings);
        activity = some_activity;
    }

    public static void setmSettings(SharedPreferences mSettings1){
        mSettings = mSettings1;
    }

    public static void setCode(int response_code){
        int code = response_code;
        if (code != 0){
            Toast.makeText(activity, "Вы вышли из своего аккаунта GitHub", Toast.LENGTH_SHORT).show();
            token_prefernces.deleteToken();
        }else {
            Toast.makeText(activity, "Ошибка выхода или отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
            token_prefernces.deleteToken();
        }
    }

    public void Logout(){
        token_prefernces = new SharedPrefernces(mSettings);
        GitLogoutModel gitLogoutModel = new GitLogoutModel();
        RetrofitClient retrofitClient = new RetrofitClient();
        gitLogoutModel.Logout(retrofitClient.getRetrofitRX(), token_prefernces.getAccessToken());
    }
}
