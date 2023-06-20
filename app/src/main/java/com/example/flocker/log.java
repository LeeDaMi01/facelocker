package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class log extends AppCompatActivity {

    private Bluetooth bluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //액션바 이름
        getSupportActionBar().setTitle("개폐 로그 확인");
        //액선바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //블루투스 활성화
        bluetooth = new Bluetooth(this);

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