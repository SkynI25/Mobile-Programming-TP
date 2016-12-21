package com.example.insky.finalproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by InSky on 2016-12-14.
 */

public class UserinfoActivity extends AppCompatActivity {
    final int DIALOG_SLEEP = 1;
    final int DIALOG_COUNTTIME = 2;
    private static final int DIALOG_YES_NO_MESSAGE = 3; // 대화상자박스 실행을 위한 변수 선언
    public long hourToMinute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        /* Button sleepBt = (Button)findViewById(R.id.sleep);
        Button countdownBt = (Button)findViewById(R.id.countdown);
        Button exit = (Button)findViewById(R.id.Exit);

        sleepBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_SLEEP);
            }
        });
        countdownBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_COUNTTIME);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_YES_NO_MESSAGE);
            }
        }); */
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countdown: {
                showDialog(DIALOG_COUNTTIME);
                break;
            }
            case R.id.Exit : {
                showDialog(DIALOG_YES_NO_MESSAGE);
                break;
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_SLEEP :
                TimePickerDialog sleep_tpd = new TimePickerDialog(UserinfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                long hourToMinute = hourOfDay * 60 + minute;
                                String SleepTime = Long.toString(hourToMinute);
                                Intent in = new Intent();
                                in.putExtra("SleepTime", SleepTime);
                                setResult(RESULT_OK, in);
                            }
                        }, 0,0,false);
                return sleep_tpd;

            case DIALOG_COUNTTIME :
                TimePickerDialog count_tpd = new TimePickerDialog(UserinfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourToMinute = hourOfDay * 60 + minute;
                                String CountTime = Long.toString(hourToMinute);
                                Intent in = new Intent();
                                in.putExtra("CountTime", CountTime);
                                setResult(RESULT_OK, in);
                                Log.d("hourToMinute", CountTime);
                            }
                        }, 0,0,true);
                return count_tpd;

            case DIALOG_YES_NO_MESSAGE :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("종료 확인 대화 상자")
                        .setMessage("나가시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() { // 원래는 아니오 예 순으로 뜨지만
                            @Override
// 우리 눈에 보기 편하게 만들고 싶어서 PositiveButton에 아니오를 넣고 Negative에 예를 넣었다.
                            public void onClick(DialogInterface dialog, int which) { // 아무것도 수행하지 않는다

                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() { // 예를 누를 경우
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish(); // 종료
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
        }

        return super.onCreateDialog(id);
    }
}
