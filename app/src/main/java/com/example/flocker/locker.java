package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class locker extends AppCompatActivity {

    private Appsetup appsetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        //액션바 이름
        getSupportActionBar().setTitle("사물함 확인");
        //액선바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //블루투스 활성화
        appsetup = new Appsetup(this,this);
        appsetup.BluetoothEnable();
    }
}