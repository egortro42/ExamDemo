package com.example.education.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.education.Models.ContactVO;
import com.example.education.Presenters.GitLogoutPresenter;
import com.example.education.R;
import com.example.education.SharedPrefernces;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.directions.DirectionsFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapFragment.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener,
        AllContactsFragment.OnListFragmentInteractionListener,
        GitHubFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        ChooseRepoFragment.OnFragmentInteractionListener,
        ChooseLocationFragment.OnFragmentInteractionListener,
        AcceleCameraFragment.OnFragmentInteractionListener {

    String TAG = "Actitvity111";
    Intent intent;
    SharedPrefernces token_prefernces;
    SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "mysettings";


    @Override
    public void onFragmentInteraction(Uri uri){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("UserName");
        String userType = intent.getStringExtra("UserType");

        String API_KEY = "b2e1333b-a8e5-4f75-aa04-ac28f9f367b7";
        MapKitFactory.setApiKey(API_KEY);
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView UserNameText = header.findViewById(R.id.UserName);
        UserNameText.setText(userName);
        TextView UserTypeText = header.findViewById(R.id.UserType);
        UserTypeText.setText(userType);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_camera) {
            fragmentClass = AcceleCameraFragment.class;
        } else if (id == R.id.nav_map) {
            fragmentClass = ChooseLocationFragment.class;
        } else if (id == R.id.nav_repository) {
            fragmentClass = ChooseRepoFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        } else if (id == R.id.nav_contacts) {
            fragmentClass = ContactsFragment.class;
        } else if (id == R.id.nav_contactsview) {
            fragmentClass = AllContactsFragment.class;
        } else if (id == R.id.nav_logout) {
            SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            GitLogoutPresenter gitLogoutPresenter = new GitLogoutPresenter(mSettings, this);
            gitLogoutPresenter.Logout();
        }else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, GitLoginActivity.class);
            startActivity(intent);
            finish();
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }


        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}