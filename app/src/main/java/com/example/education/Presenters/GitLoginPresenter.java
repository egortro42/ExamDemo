package com.example.education.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.example.education.GitHub.RetrofitClient;
import com.example.education.Models.GitLoginModel;
import com.example.education.SharedPrefernces;
import com.example.education.Views.MainActivity;
import retrofit2.Retrofit;

public class GitLoginPresenter {
    private static SharedPreferences mSettings;
    private static SharedPrefernces token_prefernces;
    private static Activity activity;
    private static Context context;

    public GitLoginPresenter(SharedPreferences Settings, Activity some_activity, Context some_ontext){
        mSettings = Settings;
        token_prefernces = new SharedPrefernces(mSettings);
        setActivity(some_activity);
        context = some_ontext;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public static void setAcessToken(String code){
        if(code != null) {
            token_prefernces.PutAccessToken(code);
            SharedPrefernces token_prefernces;
            token_prefernces = new SharedPrefernces(mSettings);
            token_prefernces.PutAccessToken(code);
            GitHubCheckAuthPresenter gitHubCheckAuthPresenter = new GitHubCheckAuthPresenter(mSettings, activity, context);
            gitHubCheckAuthPresenter.CheckAuth();
        }else{
            Toast.makeText(activity, "Не удалось войти в аккаунт", Toast.LENGTH_SHORT).show();
            activity.startActivity(getIntent(" ", " "));
            activity.finish();
        }
    }

    public static Intent getIntent(String userName, String userType){
        Intent intentMain = new Intent(context, MainActivity.class);
        intentMain.putExtra("UserName", userName);
        intentMain.putExtra("UserType", userType);
        activity.startActivity(intentMain);
        activity.finish();
        return intentMain;
    }

    public void Login(String code){
        RetrofitClient retrofitClient = new RetrofitClient();
        Retrofit retrofit = retrofitClient.getRetrofitRXAct();
        GitLoginModel gitLoginModel = new GitLoginModel();
        String clientId = "401a1fd5124c8b95d2eb";
        String clientSecret = "efecdbded42a8ddc52588cdd8d0da239736e7412";
        gitLoginModel.getLogin(retrofit, clientId, clientSecret, code);

    }
}
