package com.example.education.Models;

import android.util.Base64;

import com.example.education.GitHub.GitHubClient;
import com.example.education.GitHub.JsonResponseModels.ResponseCheckAuth;
import com.example.education.Presenters.GitHubCheckAuthPresenter;

import java.io.UnsupportedEncodingException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GitCheckAuthModel {
    private String TAG = "CheckAuth";
    private String clientId = "fdd463cdcc2798d2b42a";


    public Observable<ResponseCheckAuth> getObservable(Retrofit retrofit, String code){
        return retrofit.create(GitHubClient.class)
                .CheckObserv(getHeaderValue(), clientId, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public DisposableObserver<ResponseCheckAuth> getObserver(){
        return new DisposableObserver <ResponseCheckAuth> () {
            String Login;
            String Type;
            @Override
            public void onNext(@NonNull ResponseCheckAuth responseCheckAuth) {
                //response.code();
                Login = responseCheckAuth.getUser().getLogin();
                Type = responseCheckAuth.getUser().getType();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                GitHubCheckAuthPresenter.SetUserFields(null, null);
            }

            @Override
            public void onComplete() {
                GitHubCheckAuthPresenter.SetUserFields(Login, Type);
            }
        };
    }

    public void getCheck(Retrofit retrofit, String code) {
        getObservable(retrofit, code).subscribeWith(getObserver());
    }
    public void checkAuth(Retrofit retrofit, String code){
        getCheck(retrofit, code);
    }

    private String getHeaderValue() {
        String clientSecret = "6de2ac25ff164d9e6ec1db609d640c89659a6511";
        String basic_auth = clientId + ":" + clientSecret;
        byte[] data = new byte[0];
        try {
            data = basic_auth.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
