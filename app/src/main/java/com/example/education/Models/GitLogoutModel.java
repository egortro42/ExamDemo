package com.example.education.Models;

import android.util.Base64;

import com.example.education.GitHub.GitHubClient;
import com.example.education.GitHub.JsonResponseModels.ResponseBody;
import com.example.education.Presenters.GitLogoutPresenter;

import java.io.UnsupportedEncodingException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GitLogoutModel {
    private String TAG = "Logout";
    private String clientId = "fdd463cdcc2798d2b42a";
    private String clientSecret = "6de2ac25ff164d9e6ec1db609d640c89659a6511";

    public Observable<Response<ResponseBody>> getObservable(Retrofit retrofit, String code){
        return retrofit.create(GitHubClient.class)
                .LogoutObserv(getHeaderValue(), clientId, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public DisposableObserver<Response<ResponseBody>> getObserver(){
        return new DisposableObserver <Response<ResponseBody>> () {
            int response_code;
            @Override
            public void onNext(@NonNull Response response) {
                response_code = response.code();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                GitLogoutPresenter.setCode(0);
            }

            @Override
            public void onComplete() {
                GitLogoutPresenter.setCode(response_code);
            }
        };
    }

    public void getCheck(Retrofit retrofit, String code) {
        getObservable(retrofit, code).subscribeWith(getObserver());
    }
    public void Logout(Retrofit retrofit, String code){
        getCheck(retrofit, code);
    }

    private String getHeaderValue() {
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
