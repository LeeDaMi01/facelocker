package com.example.flocker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class main extends AppCompatActivity {

    Button locker, log, logout, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바
        getSupportActionBar().setIcon(R.drawable.logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        locker = findViewById(R.id.locker);
        log = findViewById(R.id.log);
        logout = findViewById(R.id.logout);
        cancel = findViewById(R.id.cancel);

        locker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.flocker.locker.class);
                startActivityForResult(intent, 2);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), log.class);
                startActivityForResult(intent, 3);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), first.class);
                startActivityForResult(intent, 4);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), quit.class);
                startActivityForResult(intent, 3);
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
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String id = data.getStringExtra("locker");
            Toast.makeText(getApplicationContext(), id + "응답: locker result message OK", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String id = data.getStringExtra("log");
            Toast.makeText(getApplicationContext(), id + "응답: log result message OK", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String id = data.getStringExtra("logout");
            Toast.makeText(getApplicationContext(), id + "응답: logout result message OK", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String id = data.getStringExtra("cancel");
            Toast.makeText(getApplicationContext(), id + "응답: cancel result message OK", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }


}