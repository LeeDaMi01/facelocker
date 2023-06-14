package com.example.flocker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class main extends AppCompatActivity {

    private Button locker, log, logout, cancel;

    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;

    //블루투스 설정
    private Appsetup appsetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바
        getSupportActionBar().setIcon(R.drawable.logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);

        //블루투스 활성화
        appsetup = new Appsetup(this,this);
        appsetup.BluetoothEnable();


        //intent 로 안받고 저장된 SharedPreferences로 값을 가져옴
        String name = getsavedata.getString("name","");
        //Toast.makeText(getApplicationContext(),   ""+name+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();

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
                setsavedata.putString("id","");
                setsavedata.putString("pw","");
                setsavedata.commit();
                setResult(RESULT_OK);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), quit.class);
                startActivityForResult(intent, 5);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);//파라미터로 받은 메뉴에다가 붙여달라.
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
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 3 :
                //locker 확인 할때 값이 돌아옴
                if (resultCode == RESULT_OK){
                }
                else if (resultCode == RESULT_CANCELED){
                }
                break;
            case 4:
                if (resultCode == RESULT_OK){
                }
                else if (resultCode == RESULT_CANCELED) {
                }
                break;
            case 5:
                if (resultCode == RESULT_OK){
                }
                else if (resultCode == RESULT_CANCELED) {
                }
                break;

        }
    }
}