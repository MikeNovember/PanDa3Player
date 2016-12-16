package com.github.panda3.panda3player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.Toast;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IBannerActivity {

    Intent intentBindService;
    private BannerService myServiceBinder;
    boolean mBound = false;
    private IBannerActivity self;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BannerService.MyBinder myBinder = (BannerService.MyBinder) service;
            myServiceBinder = myBinder.getService();
            mBound = true;
            myServiceBinder.setParentActivity(self);
        }
    };

    private String currentlyPlayedVideoUri;
    private int currentlyPlayedVideoPosition = 0;
    private FragmentVideo fragment;
    private boolean CheckFragmentHack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("PanDa3 Player");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // If we have a saved state then we can restore it now
        if (savedInstanceState != null && savedInstanceState.getString("CurrentUri") != null) {
            CheckFragmentHack = savedInstanceState.getBoolean("CheckFragmentHack");
            playVideo(savedInstanceState.getString("CurrentUri"), savedInstanceState.getInt("CurrentPosition"));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        self = this;
        intentBindService = new Intent(getApplicationContext(), BannerService.class);
        startService(intentBindService);
        bindService(intentBindService, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
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
        fragment = new FragmentVideo();
        currentlyPlayedVideoUri = uri;
        currentlyPlayedVideoPosition = position;
        fragment.setUri(uri);
        fragment.setPosition(position);
        replaceFragment(fragment);
        CheckFragmentHack = true;

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
                CheckFragmentHack = false;
                break;
            case R.id.nav_fav:
                replaceFragment(new FragmentListFavourites());
                CheckFragmentHack = false;
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

    @Override
    public void replaceBannerImage(Uri imageUri) {
        Log.d("ACTIVITY", "Kurwa weszło");
        if (CheckFragmentHack){
            Log.d("ACTIVITY", "Kurwa weszło2");
            fragment.setBannerImage(imageUri);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            CheckFragmentHack=true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            CheckFragmentHack=true;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        if(CheckFragmentHack){
            outState.putBoolean("CheckFragmentHack", CheckFragmentHack);
            outState.putInt("CurrentPosition", fragment.getPosition());
            outState.putString("CurrentUri", fragment.getUri());
        }
    }
}
