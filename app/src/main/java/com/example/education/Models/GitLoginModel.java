package com.example.education.Models;

import android.util.Log;

import com.example.education.GitHub.AccessToken;
import com.example.education.GitHub.GitHubClient;
import com.example.education.Presenters.GitLoginPresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GitLoginModel {

    String TAG = "GitLoginModel";

    public Observable<AccessToken> getObservable(Retrofit retrofit, String client_Id, String client_secret, String code){
        return retrofit.create(GitHubClient.class)
                .getAccessTokenObserv(client_Id, client_secret, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<AccessToken> getObserver(){
        return new DisposableObserver <AccessToken>() {
            String accessToken;
            @Override
            public void onNext(@NonNull AccessToken code) {
                    accessToken = code.getAccessToken();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                GitLoginPresenter.setAcessToken(null);
            }

            @Override
            public void onComplete() {
                GitLoginPresenter.setAcessToken(accessToken);
            }
        };
    }

    public void getLogin(Retrofit retrofit, String client_Id, String client_secret, String code) {
        getObservable(retrofit, client_Id, client_secret, code).subscribeWith(getObserver());
    }
}
