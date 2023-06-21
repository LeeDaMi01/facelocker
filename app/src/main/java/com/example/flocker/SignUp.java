package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUp extends AppCompatActivity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextName;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("회원가입");
        //액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextId = findViewById(R.id.SignUpNum);
        editTextPw = findViewById(R.id.SignUpPw);
        editTextName = findViewById(R.id.SignUpName);
        buttonSignUp = findViewById(R.id.SignUpBtn);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();
                String pw = editTextPw.getText().toString();
                String name = editTextName.getText().toString();

                if (id.isEmpty() || pw.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignUp.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://facelocker.dothome.co.kr/signup.php"; // 회원가입 PHP 파일의 URL

                    // AsyncTask를 사용하여 백그라운드에서 회원가입 요청을 보냄
                    SignUpTask signUpTask = new SignUpTask();
                    signUpTask.execute(url, id, pw, name);
                }
            }
        });
    }

    private class SignUpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String id = params[1];
            String pw = params[2];
            String name = params[3];

            try {
                // URL 연결 설정
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestMethod("POST");

                // 데이터 전송을 위한 파라미터 설정
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(pw, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

                // 데이터 전송
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();

                // 응답 받기
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 응답 반환
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals("회원가입이 완료되었습니다.")) {
                    Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                    finish(); // 액티비티 종료
                } else {
                    Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUp.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
