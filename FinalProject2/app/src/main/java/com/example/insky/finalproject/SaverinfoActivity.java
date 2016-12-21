package com.example.insky.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.data;

/**
 * Created by InSky on 2016-12-14.
 */

public class SaverinfoActivity extends AppCompatActivity {
    EditText et1;
    EditText et2;
    String fileName = "saverInfo.txt";
    private static final int DIALOG_YES_NO_MESSAGE = 1; // 대화상자박스 실행을 위한 변수 선언
    public String SaverNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saver);
        et1 = (EditText) findViewById(R.id.edittext01);
        et2 = (EditText) findViewById(R.id.edittext02);

        Intent in = getIntent();
        SaverNumber = in.getStringExtra("SaverNumber");
        et2.setText(SaverNumber);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
                if (et1.getText().toString().equals("") || et1.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "보호자를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (et2.getText().toString().equals("") || et2.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "보호자의 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    String number = et2.getText().toString();
                    save(number);
                    intent.putExtra("saver", et1.getText().toString());
                    intent.putExtra("number", et2.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(this, "저장하였습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cancel: {
                showDialog(DIALOG_YES_NO_MESSAGE);
                break;
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_YES_NO_MESSAGE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("저장 확인 대화 상자")
                        .setMessage("저장하시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() { // 원래는 아니오 예 순으로 뜨지만
                            @Override
// 우리 눈에 보기 편하게 만들고 싶어서 PositiveButton에 아니오를 넣고 Negative에 예를 넣었다.
                            public void onClick(DialogInterface dialog, int which) { // 아무것도 수행하지 않는다

                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() { // 예를 누를 경우
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Dialog", "Cancel");
                                finish(); // 종료
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
        }
        return null;
    }


    public void save(String data) { // 문자열을 바이트 단위로 읽어 들여서 파일 출력 처리를 해주는 함수
        //fileName = et1.getText().toString();

        if (data == null || data.isEmpty() == true) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE); // private 모드로 파일 생성후 출력
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