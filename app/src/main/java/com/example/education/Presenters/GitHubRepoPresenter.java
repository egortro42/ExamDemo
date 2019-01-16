package com.example.education.Presenters;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import com.example.education.GitHub.GitHubRepoAdapter;
import com.example.education.Models.GitRepoModel;

import java.util.List;

public class GitHubRepoPresenter {
    private static String repo_name = "egortro42";
    private static SharedPreferences mSettings;
    private GitRepoModel gitHub_model;
    private static ListView listView;
    private static Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GitHubRepoPresenter(SharedPreferences mSettings){
        setmSettings(mSettings);
        gitHub_model = new GitRepoModel();
    }

    public static void setmSettings(SharedPreferences mSettings1){
        mSettings = mSettings1;
    }

    public static void setRepo_name(String repo_name1) {
        repo_name = repo_name1;
    }


    public static void SetAdapter(List<com.example.education.GitHub.JsonResponseModels.GitHubRepo> repos){
        listView.setAdapter(new GitHubRepoAdapter(activity, repos));
    }
    public void showPublicGitHubRepo (Context context){
        if (gitHub_model.CheckToken(mSettings)){
            gitHub_model.showPublicGitHubRepoObserv(repo_name);
        }else{
            Toast.makeText(activity, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
        }
    }

}

