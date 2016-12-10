package com.github.panda3.panda3player;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BannerService extends Service {
    private boolean isImage1 = true;
    private Uri image1Uri;
    private Uri image2Uri;
    private IBinder mBinder = new MyBinder();
    private int counter;
    private final Handler handler = new Handler();
    private  Runnable runnable;
    private IBannerActivity mBannerActivity;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.counter = 0;

        Toast.makeText(getApplicationContext(), "Bound service has been started", Toast.LENGTH_SHORT).show();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (mBannerActivity != null) {
                    if (isImage1)
                        mBannerActivity.replaceBannerImage(image2Uri);
                    else
                        mBannerActivity.replaceBannerImage(image1Uri);

                    isImage1 = !isImage1;
                }

                handler.postDelayed(this, 5000);
            }
        };

        image1Uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/banner1");
        image1Uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/banner2");
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.counter = 0;
    }

    public int getCounter() {
        return this.counter;
    }

    public class MyBinder extends Binder {
        BannerService getService() {
            return BannerService.this;
        }
    }

    public boolean stopService(Intent name) {
        Toast.makeText(getApplicationContext(), "Bound service has been stopped", Toast.LENGTH_SHORT).show();
        this.handler.removeCallbacks(runnable);
        this.counter = 0;
        return super.stopService(name);
    }

    public void setParentActivity(IBannerActivity bannerActivity) {
        Log.d("SERVICE", "setParentActivity");
        mBannerActivity = bannerActivity;
        Log.d("SERVICE", "setParentActivity end");
    }
}
