package com.example.flocker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        PreferenceManager.setDefaultValues(this,R.xml.pref,false);

        //로그인 버튼 클릭시, 로그인 화면으로 이동
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivityForResult(intent, 1);
            }
        });


    }
    @Override
    //requestCode:요청, resultCode:결과, data:데이터
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //결과가 성공적으로 반환
        switch (requestCode){
            case 1 :
                //로그인 성공했다고 값이 반환되었음으로 메인화면 띄워줌
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    startActivityForResult(intent,2);

                }
                //로그인화면에서 뒤로가기 버튼눌렀을때 혹은 RESULT_CANCELED값을 반환받았을때
                else if (resultCode == RESULT_CANCELED){
                    //여기선 finish() 안함 실수로 뒤로가기 눌렀을경우 다시 로그인할수도 있기 때문에
                }
                break;
            case 2:
                //메인화면에서 돌아오는 요청
                if (resultCode == RESULT_OK){
                    //혹시 모르니 비워둠
                    //Intent intent = new Intent(getApplicationContext(), main.class);
                }
                //뒤로가기키를 잘못눌러서 로그인화면으로 다시 띄워지는것 로그아웃 요청이 아니라서 로그인 다시 시도시 자동로그인 진행
                else if (resultCode == RESULT_CANCELED) {
                    //개인적으론 그냥 종료되는걸 원하나 이렇게 그냥 함
                    //finish();
                }
                break;

        }
    }
}



