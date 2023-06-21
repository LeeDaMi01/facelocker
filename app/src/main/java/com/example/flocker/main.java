package com.example.flocker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class main extends AppCompatActivity {

    private Button locker, log, logout, cancel;
    private TextView name;
    private String loginId;
    private String loginName;
    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;

    //블루투스 설정
    private Appsetup appsetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 이름
        getSupportActionBar().setTitle("FaceLocker");

        //액션바
        getSupportActionBar().setIcon(R.drawable.logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getsavedata = getSharedPreferences("savedata", MODE_PRIVATE);

        name = findViewById(R.id.name);
        name.setText(loginName + "님");

        loginId = getsavedata.getString("name", "");
        name.setText(loginId + "님");


        //블루투스 활성화
        appsetup = new Appsetup(this, this);
        appsetup.BluetoothEnable();


        locker = findViewById(R.id.locker);
        log = findViewById(R.id.log);
        logout = findViewById(R.id.logout);
        cancel = findViewById(R.id.cancel);

        locker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.flocker.locker.class);
                startActivityForResult(intent, 3);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), log.class);
                startActivityForResult(intent, 4);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setsavedata = getsavedata.edit();
                //저장된 로그인값 초기화(자동로그인 해지)
                setsavedata.putString("id", "");
                setsavedata.putString("pw", "");
                setsavedata.commit();
                Intent first = new Intent(getApplicationContext(), com.example.flocker.first.class);
                startActivity(first);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, quit.class);
                startActivityForResult(intent,5);
            }
        });
        //FCM 토큰 받아오기
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM_TEST", "FCM 등록 토큰 가져오기 실패", task.getException());
                    return;
                }

                String token = task.getResult();

                Log.d("FCM_TEST", "기기 등록 토큰: " + token);

                // 토큰을 PHP 스크립트로 전송
                sendTokenToServer(token);
            }
        });

    }
    //토큰 DB에 추가하기 위해 POST로 전송
    private void sendTokenToServer(String token) {
        String url = "http://facelocker.dothome.co.kr/token.php";

        // POST 요청을 보내기 위한 새로운 StringRequest 생성
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 필요한 경우 서버로부터의 응답 처리
                        Log.d("FCM_TEST", "토큰 업데이트 응답: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리
                        Log.e("FCM_TEST", "토큰 업데이트 오류: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // 서버로 전송할 매개변수를 담을 HashMap 생성
                Map<String, String> params = new HashMap<>();
                params.put("id", loginId); // 여기에 사용자의 loginId를 전달하세요
                params.put("token", token); // 여기에 FCM 토큰을 전달하세요

                return params;
            }
        };

        // 요청을 RequestQueue에 추가
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//파라미터로 받은 메뉴에다가 붙여달라.
        return true;
    }

    //옵션메뉴가 선택됐을 때 자동으로 호출되는 콜백함수 (메뉴함수 선택 시 )
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_action) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                //locker 확인 할때 값이 돌아옴
                if (resultCode == RESULT_OK) {
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    finish();
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;

        }
    }

}