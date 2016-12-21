package com.example.insky.finalproject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int user = 3;
    int saver = 5;
    public String SleepTime;
    public String CountTime;
    public String Relation;
    public String Number;
    TextView Saver_text;
    TextView SleepTime_text;
    TextView CountTime_text;
    TextView Saver_number;
    AlarmMessageService AMService;
    boolean mBound = false;
    AlarmManager am;
    PendingIntent pendingIntent;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Saver_text = (TextView) findViewById(R.id.inputSaver);
        Saver_number = (TextView) findViewById(R.id.saverNumber);
        SleepTime_text = (TextView) findViewById(R.id.sleepValue);
        CountTime_text = (TextView) findViewById(R.id.countValue);
        Saver_number.setText(load());
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("com.example.insky.finalproject");
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000,pendingIntent);

        //registerReceiver(AlarmReceiver, intentFilter);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("start", "체크");
        Intent intent = new Intent(this, AlarmMessageService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("stop", "체크");
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == user) {
                SleepTime = data.getStringExtra("SleepTime");
                SleepTime_text.setText(SleepTime);
                CountTime = data.getStringExtra("CountTime");
                Log.d("CountTime", CountTime);
                CountTime_text.setText(String.valueOf(CountTime));


            } else if (requestCode == saver) {
                Relation = data.getStringExtra("saver");
                Saver_text.setText(Relation);
                Number = data.getStringExtra("number");
                Saver_number.setText(Number);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo: {
                Intent intent = new Intent(this, UserinfoActivity.class);
                startActivityForResult(intent, user);
                Log.v("Button", "userInfo");
                break;
            }
            case R.id.saverinfo: {
                Intent intent = new Intent(this, SaverinfoActivity.class);
                intent.putExtra("SaverNumber", Saver_number.getText().toString());
                startActivityForResult(intent, saver);
                Log.v("Button", "saverinfo");
                break;
            }
        }
    }

    public String load() { // 파일을 한 바이트씩 읽어주는 함수
        try {
            FileInputStream fis = openFileInput("saverInfo.txt"); // 파일읽기 위해 파일 오픈
            byte[] data = new byte[fis.available() + 1]; // 버퍼 생성후 읽기 수행
            fis.read(data);
            Log.v("load", new String(data));
            fis.close();
            return new String(data);
        } catch (Exception e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            Log.v("오류발생!", "이거안되요!");
            e.printStackTrace();
        }
        return "";
    }


    /*    protected void onDestroy() {
            super.onDestroy();
            try { // Alarm 발생 시 전송되는 broadcast 수신 receiver 를 해제
                unregisterReceiver(AlarmReceiver);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } // AlarmManager 에 등록한 alarm 취소 am.cancel(pendingIntent);
        }*/
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MainActivity", "onServiceConnected()");
            AlarmMessageService.LocalBinder binder = (AlarmMessageService.LocalBinder) service;
            AMService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MainActivity", "onServiceDisconnected()");
            mBound = false;
        }
    };

}