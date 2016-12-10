package com.github.panda3.panda3player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* Tutaj, przy starcie apki, powinno zostać załadowane z bazy uri ostatniego oglądanego filmiku.
    * Wtedy jeśli użyszkodnik wybierze od razu fragment z oglądaniem filmiku, to wyświetli się ostatnio oglądany
    */
    private String currentlyPlayedVideoUri;
    private int currentlyPlayedVideoPosition = 0;

    Context context;
    Intent intentBindService;
    private BoundService myServiceBinder;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            Log.d("ServiceConnection","connected");
            BoundService.MyBinder binder = (BoundService.MyBinder) service;
            myServiceBinder = binder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {

            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("PanDa3 Player");
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        intentBindService = new Intent(context,BoundService.class);
        bindService(intentBindService, mConnection, Context.BIND_AUTO_CREATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentList = new Intent(this, AboutActivity.class);
            intentList.putExtra("FLAG_ID", "About activity started");
            startActivity(intentList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void playVideo(String uri, int position) {
        FragmentVideo fragment = new FragmentVideo();
        currentlyPlayedVideoUri = uri;
        currentlyPlayedVideoPosition = position;
        fragment.setUri(uri);
        fragment.setPosition(position);
        replaceFragment(fragment);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_player:
                playVideo(currentlyPlayedVideoUri, currentlyPlayedVideoPosition);
                break;
            case R.id.nav_quit:
                finish();
                break;
            case R.id.nav_videos:
                replaceFragment(new FragmentListVideos());
                break;
            case R.id.nav_fav:
                replaceFragment(new FragmentListFavourites());
                break;
        }
        return true;
    }

    private void replaceFragment(Fragment fragment)
    {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}
