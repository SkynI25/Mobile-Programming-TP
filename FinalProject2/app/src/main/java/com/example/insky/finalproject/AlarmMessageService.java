package com.example.insky.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by InSky on 2016-12-21.
 */

public class AlarmMessageService extends Service {

    BroadcastReceiver mReceiver = new Broadcast();

    @Override
    public void onCreate() {

        super.onCreate();

        IntentFilter filter = new IntentFilter();

        filter.addAction("com.example.insky.finalproject");
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        registerReceiver(mReceiver, filter);
        Log.d("onCreate()","브로드캐스트리시버 등록됨");

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if(mReceiver != null)
        unregisterReceiver(mReceiver);
    }

    public class LocalBinder extends Binder {
        AlarmMessageService getService() {
            return AlarmMessageService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
