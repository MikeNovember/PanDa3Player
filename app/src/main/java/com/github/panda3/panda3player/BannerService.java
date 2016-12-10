package com.github.panda3.panda3player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BannerService extends Service {
    private MyBinder binder;

    public BannerService() {
    }

    public class MyBinder extends Binder {
        BannerService getService() {
            return BannerService.this;
        }
    }

    public void onCreate() {
        super.onCreate();

    }

    public IBinder onBind(Intent intent) {
        return binder;
    }
}
