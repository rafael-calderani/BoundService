package com.rm39951.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

/**
 * Created by logonrm on 12/06/2017.
 */

public class BoundService extends Service {
    private static String LOG_TAG = "BoundService";
    private IBinder mBinder = new ServiceBinder();
    private Chronometer chronos;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "onCreate Started");
        chronos = new Chronometer(this);
        chronos.setBase(SystemClock.elapsedRealtime());
        chronos.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "onBind Started");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "onRebind Started");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "onUnbind Started");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "onDestroy Started");
        chronos.stop();
        super.onDestroy();
    }

    public String getTimeStamp(){
        long elapsedMiliseconds = SystemClock.elapsedRealtime()-chronos.getBase();
        int hours = (int) (elapsedMiliseconds / 3600000);
        int minutes = (int) (elapsedMiliseconds - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMiliseconds - hours * 3600000 - minutes*60000)/1000;
        int millis = (int) (elapsedMiliseconds - hours * 3600000 - minutes * 60000 - seconds*1000);

        return String.format("%d:%d:%d:%d", hours, minutes, seconds, millis);
    }

    public class ServiceBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
}