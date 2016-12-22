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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by InSky on 2016-12-14.
 */

public class UserinfoActivity extends AppCompatActivity {
    private static final int DIALOG_SLEEPST = 1;
    private static final int DIALOG_SLEEPED = 2;
    private static final int DIALOG_COUNTTIME = 3;
    private static final int DIALOG_YES_NO_MESSAGE = 4; // 대화상자박스 실행을 위한 변수 선언
    String fileNameSLS = "userInfoSLS.txt";
    String fileNameSLE = "userInfoSLE.txt";
    String fileNameSLR = "userInfoSLR.txt";
    String fileNameCT = "userInfoCT.txt";
    public long hourToMinute1;
    public long hourToMinute2;
    public long hourToMinute3;
    public long hourToMinuteR;

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
            case R.id.sleepStart: {
                showDialog(DIALOG_SLEEPST);
                break;
            }
            case R.id.sleepEnd: {
                showDialog(DIALOG_SLEEPED);
                break;
            }
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
            case DIALOG_SLEEPST :
                TimePickerDialog sleep_tpd1 = new TimePickerDialog(UserinfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourToMinute1 = hourOfDay * 60 + minute;
                                String SleepTimeST = Long.toString(hourToMinute1);
                                saveSLS(SleepTimeST);
                                Intent inST = new Intent();
                                inST.putExtra("SleepTimeST", SleepTimeST);
                                setResult(RESULT_OK, inST);
                                Log.d("CheckST", SleepTimeST);
                            }
                        }, 0,0,false);
                return sleep_tpd1;

            case DIALOG_SLEEPED :
                TimePickerDialog sleep_tpd2 = new TimePickerDialog(UserinfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourToMinute2 = hourOfDay * 60 + minute;
                                String SleepTimeED = Long.toString(hourToMinute2);
                                saveSLE(SleepTimeED);
                                Intent inED = new Intent();
                                inED.putExtra("SleepTimeED", SleepTimeED);
                                //setResult(RESULT_OK, in);
                                Log.d("CheckED", SleepTimeED);
                                hourToMinuteR = hourToMinute2 - hourToMinute1;
                                if(hourToMinuteR<0) {
                                    hourToMinuteR = hourToMinuteR + 1440;
                                }
                                String SleepTimeR = Long.toString(hourToMinuteR);
                                saveSLR(SleepTimeR);
                                /*Intent intentR = new Intent();
                                inED.putExtra("SleepTimeR", SleepTimeR);
                                setResult(RESULT_OK, inED);*/
                                Log.d("CheckR", SleepTimeR);
                            }
                        }, 0,0,false);
                return sleep_tpd2;

            case DIALOG_COUNTTIME :
                TimePickerDialog count_tpd = new TimePickerDialog(UserinfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourToMinute3 = hourOfDay * 60 + minute;
                                String CountTime = Long.toString(hourToMinute3);
                                saveCT(CountTime);
                                Intent in = new Intent();
                                in.putExtra("CountTime", CountTime);
                                setResult(RESULT_OK, in);
                                Log.d("hourToMinuteCheck", CountTime);
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
                                if(hourToMinute1 == -1) {
                                    Toast.makeText(UserinfoActivity.this, "수면시작시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                                } else if(hourToMinute2 == -1) {
                                    Toast.makeText(UserinfoActivity.this, "수면종료시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                                } else if(hourToMinute3 == -1) {
                                    Toast.makeText(UserinfoActivity.this, "위기알림시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    finish(); // 종료
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
        }

        return super.onCreateDialog(id);
    }

    public void saveSLS(String data) { // 문자열을 바이트 단위로 읽어 들여서 파일 출력 처리를 해주는 함수
        //fileName = et1.getText().toString();

        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileNameSLS, Context.MODE_PRIVATE); // private 모드로 파일 생성후 출력
            fos.write(data.getBytes());
            Log.d("save", data);
            fos.close(); // 출력후 종료

        } catch (FileNotFoundException e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveSLE(String data) { // 문자열을 바이트 단위로 읽어 들여서 파일 출력 처리를 해주는 함수
        //fileName = et1.getText().toString();

        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileNameSLE, Context.MODE_PRIVATE); // private 모드로 파일 생성후 출력
            fos.write(data.getBytes());
            Log.d("save", data);
            fos.close(); // 출력후 종료

        } catch (FileNotFoundException e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCT(String data) { // 문자열을 바이트 단위로 읽어 들여서 파일 출력 처리를 해주는 함수
        //fileName = et1.getText().toString();

        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileNameCT, Context.MODE_PRIVATE); // private 모드로 파일 생성후 출력
            fos.write(data.getBytes());
            Log.d("save", data);
            fos.close(); // 출력후 종료

        } catch (FileNotFoundException e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSLR(String data) { // 문자열을 바이트 단위로 읽어 들여서 파일 출력 처리를 해주는 함수
        //fileName = et1.getText().toString();

        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileNameSLR, Context.MODE_PRIVATE); // private 모드로 파일 생성후 출력
            fos.write(data.getBytes());
            Log.d("save", data);
            fos.close(); // 출력후 종료

        } catch (FileNotFoundException e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
