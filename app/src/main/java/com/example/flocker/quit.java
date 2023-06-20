package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class quit extends AppCompatActivity {

    private Button quit_button;
    private CheckBox quit_checkbox;

    private String loginId;

    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);

        //사용자 로그인 id 받기
        Intent intent = getIntent();
        loginId = intent.getStringExtra("loginId");
        if (loginId != null) {
            Log.d("quit", "loginId: " + loginId);
        } else {
            Log.d("quit", "로그인 정보가 전달되지 않았습니다.");
        }

        // 액션바 이름
        getSupportActionBar().setTitle("회원 탈퇴");
        // 액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quit_button = findViewById(R.id.quit_button);
        quit_checkbox = findViewById(R.id.quit_checkbox);

        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 체크박스를 클릭한 경우, quit_check로 이동
                if (quit_checkbox.isChecked()) {
                    sendDeleteRequest(loginId);

                    // quit_check 액티비티로 이동
                    Intent intent = new Intent(getApplicationContext(), quit_check.class);
                    startActivityForResult(intent, 6);

                    //체크박스를 클릭하지 않은 경우, 안내문(토스트)
                } else {
                    Toast.makeText(getApplicationContext(), "동의를 체크하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendDeleteRequest(final String loginId) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                String deleteUrl = "http://facelocker.dothome.co.kr/delete.php";
                try {
                    String postData = "id=" +loginId; //사용자 로그인 (loginId)
                    URL url = new URL(deleteUrl); // url 객체 생성
                    HttpURLConnection con = (HttpURLConnection) url.openConnection(); //url 사용해서 http 연결, HttpURLConeection 객체 생성
                    con.setRequestMethod("POST"); //POST 방식
                    con.setDoOutput(true); //출력 스트림 설정
                    con.getOutputStream().write(postData.getBytes()); //출력 스트림 작성

                    int responseCode = con.getResponseCode(); //서버로부터 받은 응답 코드 responseCode 변수에 저장
                    con.disconnect(); //http 연결 종료

                    return responseCode;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }.execute();// AsyncTask 실행
    }
    //액션바에 있는 뒤로가기 버튼을 눌러도 데이터(메인화면 사용자 이름) 그대로 유지
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 액션바의 뒤로가기 버튼 클릭 시 이전 화면으로 돌아가도록 처리
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
