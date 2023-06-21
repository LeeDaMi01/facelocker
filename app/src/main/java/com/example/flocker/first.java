package com.example.flocker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class first extends AppCompatActivity {
    private Button login1;

    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;

    //각종 퍼미션 및 블루투스 기능
    private Appsetup appsetup;


    //위치데이터 사용 요청에 이용될것 정의, 0보다 커야하는 정수
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //액션바 이름
        getSupportActionBar().setTitle("FaceLocker");

        //로그인 버튼
        login1 = findViewById(R.id.login1);

        //퍼미션 및 각종 기능 넣어둔곳
        appsetup = new Appsetup(this,this);

        //권한 체크(퍼미션 체크했을때)
        if (!appsetup.PermissionCheck()){
            //퍼미션 요청
            appsetup.requestPermission();
        }

        getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
        setsavedata = getsavedata.edit();

        appsetup.BluetoothEnable();

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
            appsetup.BluetothDisable();
        }
    }

    //위치권한이 부여되었다고 여기로 값이 돌아옴 context를 연결해서 넘겨줬기 떄문에
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            //퍼미션 수행이 올바르게 되었는지 체크, 여기서 리턴값이 돌아온게 false 였다면 사용자가 거부한거임
            if (!appsetup.permissionResult(requestCode, permissions, grantResults)) {
                // 다시 permission 요청
                appsetup.requestPermission();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            //만일 이후에 여기에서 퍼미션 추가적으로 처리할것이 있거나
            //퍼미션 이후 처리할게 있다면 여기서 처리

            //퍼미션 관련 처리후 이쪽으로 오니까 블루투스 활성화 요청
            //블루투스 권한을 못얻었다면 BluetoothOnable() 내부에서 활성화 안하게 되어있으니 그냥 요청
            appsetup.BluetoothEnable();
        }
    }
}



