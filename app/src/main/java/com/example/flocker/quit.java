package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class quit extends AppCompatActivity {

    private Button quit_button;
    private CheckBox quit_checkbox;

    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);

        //액션바 이름
        getSupportActionBar().setTitle("회원 탈퇴");
        //액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quit_button = findViewById(R.id.quit_button);
        quit_checkbox = findViewById(R.id.quit_checkbox);

        //블루투스 활성화
        bluetooth = new Bluetooth(this);


        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //체크박스를 클릭한 경우, quit_check로 이동
                if (quit_checkbox.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), quit_check.class);
                    startActivityForResult(intent, 6);
                    //체크박스를 클릭하지 않은 경우
                } else {
                    Toast.makeText(getApplicationContext(), "동의를 체크하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
