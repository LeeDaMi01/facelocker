package com.example.flocker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class first extends AppCompatActivity {
    Button login1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //액션바 이름
        getSupportActionBar().setTitle("facelocker");

        //로그인 버튼
        login1 = findViewById(R.id.login1);

        //로그인 버튼 클릭시, 로그인 화면으로 이동
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivityForResult(intent, 2);
            }
        });
    }
    @Override
    //requestCode:요청, resultCode:결과, data:데이터
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //결과가 성공적으로 반환
        if(requestCode==2 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("fist");
            Toast.makeText(getApplicationContext(), name + "login result message OK", Toast.LENGTH_SHORT).show();
            //결과가 취소되었을 경우
        }else if(resultCode == RESULT_CANCELED){
            finish();
        }
    }
}



