package com.example.education.GitHub;

import com.example.education.GitHub.JsonResponseModels.GitHubRepo;
import com.example.education.GitHub.JsonResponseModels.ResponseBody;
import com.example.education.GitHub.JsonResponseModels.ResponseCheckAuth;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Response;

public interface GitHubClient {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Observable<AccessToken> getAccessTokenObserv(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code

    );

    @GET("/users/{user}/repos")
    Observable<List<GitHubRepo>> GetRepositoryObserv(@Path("user") String user);

    @Headers("Accept: application/json")
    @GET ("/applications/{client_id}/tokens/{access_token}")
    Observable <ResponseCheckAuth> CheckObserv (@Header("Authorization") String authorization, @Path("client_id") String client_id, @Path("access_token") String access_token);

    @Headers("Accept: application/json")
    @DELETE("/applications/{client_id}/tokens/{access_token}")
    Observable <Response<ResponseBody>> LogoutObserv (@Header("Authorization") String authorization, @Path("client_id") String client_id, @Path("access_token") String access_token);

}
