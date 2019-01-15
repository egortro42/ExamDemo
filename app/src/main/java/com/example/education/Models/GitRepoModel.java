package com.example.education.Models;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.education.GitHub.GitHubClient;
import com.example.education.GitHub.RetrofitClient;
import com.example.education.Presenters.GitHubRepoPresenter;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GitRepoModel {
    private final String TAG = "GitHubModel";
    private RetrofitClient retrofitClient;

    public GitRepoModel(){
        retrofitClient = new RetrofitClient();
    }

    public boolean CheckToken(SharedPreferences mSettings) {
        boolean isExist;
        String APP_PREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN";
        if (mSettings.contains(APP_PREFERENCES_ACCESS_TOKEN)) {
            isExist = true;
        } else {
            isExist = false;
        }
        return isExist;
    }


    public Observable<List<com.example.education.GitHub.JsonResponseModels.GitHubRepo>> getObservable(Retrofit retrofit, String repo_name){
        return retrofit.create(GitHubClient.class)
                .GetRepositoryObserv(repo_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void getRepo(Retrofit retrofit, String repo_name) {
        getObservable(retrofit, repo_name).subscribeWith(getObserver());
    }
    public void showPublicGitHubRepoObserv(String repo_name){
        Retrofit retrofit = retrofitClient.getRetrofitRX();
        getRepo(retrofit, repo_name);
    }

    public DisposableObserver<List<com.example.education.GitHub.JsonResponseModels.GitHubRepo>> getObserver(){
        final List<com.example.education.GitHub.JsonResponseModels.GitHubRepo> list_repo = new ArrayList<>();
        return new DisposableObserver <List<com.example.education.GitHub.JsonResponseModels.GitHubRepo>>() {

            @Override
            public void onNext(@NonNull List<com.example.education.GitHub.JsonResponseModels.GitHubRepo> gitHubRepos) {
                list_repo.addAll(gitHubRepos);
                for( com.example.education.GitHub.JsonResponseModels.GitHubRepo git : gitHubRepos ){
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                GitHubRepoPresenter.SetAdapter(list_repo);
            }
        };
    }



}

