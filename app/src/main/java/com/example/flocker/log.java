package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class log extends AppCompatActivity {

    private Bluetooth bluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //액션바 이름
        getSupportActionBar().setTitle("개페 로그 확인");
        //액선바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //블루투스 활성화
        bluetooth = new Bluetooth(this);

    }
}