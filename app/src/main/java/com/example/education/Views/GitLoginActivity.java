package com.example.education.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.education.Presenters.GitLoginPresenter;
import com.example.education.R;
import com.example.education.SharedPrefernces;

public class GitLoginActivity extends AppCompatActivity {
    String TAG = "Actitvity111";
    private String redirectUri = "gitauth://callback";
    String code = null;
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;
    SharedPrefernces token_prefernces;

    private Intent getIntentBrowser() {
        String clientId = "fdd463cdcc2798d2b42a";
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" +
                "?client_id=" + clientId + "&scope=repo&redirect_uri=" + redirectUri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "GitLoginActivity, onCreate");
        setContentView(R.layout.activity_git_auth);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token_prefernces = new SharedPrefernces(mSettings);
        startActivity(getIntentBrowser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            code = uri.getQueryParameter("code");
            GitLoginPresenter gitLoginPresenter = new GitLoginPresenter(mSettings, this, this);
            gitLoginPresenter.Login(code);
        }

    }
}
