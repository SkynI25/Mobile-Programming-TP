package com.example.insky.finalproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by InSky on 2016-12-21.
 */

public class Broadcast extends BroadcastReceiver {
    Context mContext;
    double x,y;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        int Time = Hour * 60 + Minute;

        // 시간이 흘렀을때
        if (intent.getAction().equals("com.example.insky.finalproject")) {
            //am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000 * Integer.parseInt(loadCT()) , pendingIntent);
            if (Time >= Integer.parseInt(loadSLS()) && Integer.parseInt(loadSLE()) >= Time) {
                return;
            } else if (Time >= Integer.parseInt(loadSLE())) {
                am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000 * Integer.parseInt(loadCT()), pendingIntent);
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(load(), null, loadCT() + " 분 동안 사용자가 핸드폰을 사용하지 않았습니다!", null, null);
            Toast.makeText(context, "HERE", Toast.LENGTH_SHORT).show();
            Log.d("onReceive()", "알람 받음");
        }

/*
            Date date = new Date();
            Calendar...
            if (cal.getTime() <=...<=)
            return;
*/

            //Log.d("onReceive()", "알람 받음");
//            Log.d("onReceive()", "스크린 OFF");
//            Intent in = new Intent("com.example.insky.finalproject");
//
//            //am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
//            //am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 6000, pendingIntent);
//            try {
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(load(), null, loadTime() + " 분 동안 사용자가 핸드폰을 사용하지 않았습니다!", null, null);
//                Toast.makeText(context, "HERE", Toast.LENGTH_SHORT).show();
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//
//            /*switch (getResultCode()) {
//                case Activity.RESULT_OK:
//                    // 전송 성공
//                    Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                    // 전송 실패
//                    Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_NO_SERVICE:
//                    // 서비스 지역 아님
//                    Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_RADIO_OFF:
//                    // 무선 꺼짐
//                    Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_NULL_PDU:
//                    // PDU 실패
//                    Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    Toast.makeText(context, "HERE", Toast.LENGTH_SHORT).show();
//            }*/
//        /*if (intent.getAction().equals("com.example.insky.finalproject")) {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage("01094014406",null,"잘보내지나확인",null, null);
//        }*/
//        } else if (action.equals("android.intent.action.SCREEN_ON")) {
//            Log.d("onReceive()", "스크린 ON");
//            am.cancel(pendingIntent);
//        }

            // 화면이 꺼졌을때
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(Time >= Integer.parseInt(loadSLS()) && Integer.parseInt(loadSLE()) >= Time ) {
                return;
            }
            intent = new Intent("com.example.insky.finalproject");
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000 * Integer.parseInt(loadCT()) , pendingIntent);
            Log.d("onReceive()", "꺼짐!!!");

            // 화면이 켜졌을때
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            am.cancel(pendingIntent);
            Log.d("onReceive()", "켜짐!!!");
        }
    }

    public String load() { // 파일을 한 바이트씩 읽어주는 함수
        try {
            FileInputStream fis = mContext.openFileInput("saverInfo.txt"); // 파일읽기 위해 파일 오픈
            byte[] data = new byte[fis.available()]; // 버퍼 생성후 읽기 수행
            fis.read(data);
            Log.v("load", new String(data));
            fis.close();
            return new String(data);
        } catch (Exception e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            Log.v("오류발생!", "이거안되요!");
            e.printStackTrace();
        }
        return "01094014406";
    }

    public String loadCT() { // 파일을 한 바이트씩 읽어주는 함수
        try {
            FileInputStream fis = mContext.openFileInput("userInfoCT.txt"); // 파일읽기 위해 파일 오픈
            byte[] data = new byte[fis.available()]; // 버퍼 생성후 읽기 수행
            fis.read(data);
            Log.v("load", new String(data));
            fis.close();
            return new String(data);
        } catch (Exception e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            Log.v("오류발생!", "이거안되요!");
            e.printStackTrace();
        }
        return "1";
    }

    public String loadSLS() { // 파일을 한 바이트씩 읽어주는 함수
        try {
            FileInputStream fis = mContext.openFileInput("userInfoSLS.txt"); // 파일읽기 위해 파일 오픈
            byte[] data = new byte[fis.available()]; // 버퍼 생성후 읽기 수행
            fis.read(data);
            Log.v("load", new String(data));
            fis.close();
            return new String(data);
        } catch (Exception e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            Log.v("오류발생!", "이거안되요!");
            e.printStackTrace();
        }
        return "0";
    }

    public String loadSLE() { // 파일을 한 바이트씩 읽어주는 함수
        try {
            FileInputStream fis = mContext.openFileInput("userInfoSLE.txt"); // 파일읽기 위해 파일 오픈
            byte[] data = new byte[fis.available()]; // 버퍼 생성후 읽기 수행
            fis.read(data);
            Log.v("load", new String(data));
            fis.close();
            return new String(data);
        } catch (Exception e) { // 예외처리. 파일 관련 API를 사용하는 경우 IOEXCEPTION을 해주어야 함
            Log.v("오류발생!", "이거안되요!");
            e.printStackTrace();
        }
        return "8";
    }

}
