package com.github.panda3.panda3player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Button newActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        FragmentVideo fragmentA = new FragmentVideo();

        transaction.add(R.id.fragment_placeholder, fragmentA);
        transaction.commit();

        newActivityButton = (Button) findViewById(R.id.fullscreenVideo);
        newActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFullScreenActivity(view);
            }
        });
    }


    private void startFullScreenActivity(View view) {
        Log.i(LOG_TAG, "FullScreen enabled");
        Intent intentList = new Intent(this, FullscreenActivity.class);
        startActivity(intentList);
    }
}
