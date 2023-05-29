package com.example.flocker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.Manifest;

public class first extends AppCompatActivity {
    private Button login1;

    //최대 요청횟수
    private final int Num_of_Permission_requests = 2;

    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;


    //블루투스 활성화 비활성화 기능
    private Bluetooth bluetooth;

    //위치데이터 사용 요청에 이용될것 정의, 0보다 커야하는 정수
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //액션바 이름
        getSupportActionBar().setTitle("facelocker");

        //블루투스 활성화 비활성화 기능
        bluetooth = new Bluetooth(this);

        //로그인 버튼
        login1 = findViewById(R.id.login1);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //멈춤상태라면은 블루투스 비활성화
        //앱 종료시 블루투스 종료가 켜져있다면 블루투스 종료
        getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
        boolean bluetooth_set = getsavedata.getBoolean("bluetooth",false);
        if (bluetooth_set == false){
            bluetooth.disableBluetooth();
        }
    }

    //위치권한이 부여되었다고 여기로 값이 돌아옴 context를 연결해서 넘겨줬기 떄문에
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 부여된 경우
                bluetooth = new Bluetooth(this);
            } else {
                // 위치 권한이 거부된 경우
                getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
                setsavedata = getsavedata.edit();
                int Num_Request = getsavedata.getInt("Request_Num",0);
                //횟수를 카운팅하여 다시 요청함, 요청횟수 초과시 다시는 요청 안함
                if ( Num_Request > Num_of_Permission_requests){
                    Num_Request++;
                    setsavedata.putInt("Num_Request",Num_Request);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }else {
                    Toast.makeText(this, "앱을 이용하는데 제한이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}



