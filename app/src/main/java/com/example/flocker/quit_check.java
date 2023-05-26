package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class quit_check extends AppCompatActivity {

    Button quit_check_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_check);

        quit_check_button = findViewById(R.id.quit_check_button);
        quit_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), first.class);
                startActivityForResult(intent, 7);
            }
        });
    }
}