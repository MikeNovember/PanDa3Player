package com.github.panda3.panda3player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class BoundService extends Service {
    private int counter = 0;
    private Toast toast;
    private Timer timer = new Timer();;
    private TimerTask timerTask;
    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            showToast("Your bound service is still working");
            counter++;
        }
    }

    private void showToast(String text) {
        toast.setText(text);
        toast.show();
    }

    public BoundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearTimerSchedule() {
        if(timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new BoundService.MyTimerTask();
    }

    @Override
    public void onDestroy() {
        clearTimerSchedule();
        showToast("Your bound service has been stopped");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        showToast("Your bound service has been binded");
        run();
        return mBinder;
    }

    public void run(){
        timer = new Timer();
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask, 6 * 1000, 8 * 1000);
        showToast("Your bound service has been started");
    }

    public void getCounter(){
        showToast("counter = " + counter);
    }
}
