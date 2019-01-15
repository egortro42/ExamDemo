package com.example.education.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.education.GitHub.RetrofitClient;
import com.example.education.Models.GitCheckAuthModel;
import com.example.education.SharedPrefernces;
import com.example.education.Views.GitLoginActivity;
import com.example.education.Views.MainActivity;

public class GitHubCheckAuthPresenter {
    private static SharedPrefernces token_prefernces;
    private static SharedPreferences mSettings;
    private static Activity activity;
    private static Context context;

    public static void SetUserFields(String UserName, String UserType){
        if (UserName != null && UserType != null){
            Intent intentMain = new Intent(context, MainActivity.class);
            intentMain.putExtra("UserName", UserName);
            intentMain.putExtra("UserType", UserType);
            activity.startActivity(intentMain);
            activity.finish();
        }else{
            Intent intentMain = new Intent(context, GitLoginActivity.class);
            intentMain.putExtra("UserName", " ");
            intentMain.putExtra("UserType", " ");
            activity.startActivity(intentMain);
            activity.finish();
        }
    }

    public static void setmSettings(SharedPreferences mSettings1){
        mSettings = mSettings1;
    }

    public GitHubCheckAuthPresenter(SharedPreferences mSettings, Activity some_activity, Context some_context){
        setmSettings(mSettings);
        token_prefernces = new SharedPrefernces(mSettings);
        activity = some_activity;
        context = some_context;
    }

    public void CheckAuth(){
        token_prefernces = new SharedPrefernces(mSettings);
        GitCheckAuthModel gitCheckAuthModel = new GitCheckAuthModel();
        RetrofitClient retrofitClient = new RetrofitClient();
        gitCheckAuthModel.checkAuth(retrofitClient.getRetrofitRX(), token_prefernces.getAccessToken());
    }
}
