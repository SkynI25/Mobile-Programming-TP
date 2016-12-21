package com.example.insky.finalproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by InSky on 2016-12-21.
 */

public class Broadcast extends BroadcastReceiver {
    Context mContext;
    AlarmManager am;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.insky.finalproject")) {
            Intent in = new Intent("com.example.insky.finalproject");
            pendingIntent = PendingIntent. getBroadcast (context, 0, in, 0);
            am.setExact(AlarmManager. ELAPSED_REALTIME_WAKEUP , SystemClock. elapsedRealtime () + 5000, pendingIntent);

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // 전송 성공
                    Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // 전송 실패
                    Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    // 서비스 지역 아님
                    Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    // 무선 꺼짐
                    Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    // PDU 실패
                    Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                    break;
            }
        /*if (intent.getAction().equals("com.example.insky.finalproject")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("01094014406",null,"잘보내지나확인",null, null);
        }*/
        }
    }
}
