package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class locker extends AppCompatActivity {

    private Appsetup appsetup;
    private ImageView locker_1;
    private ImageView locker_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        //액션바 이름
        getSupportActionBar().setTitle("사물함 확인");
        //액선바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //블루투스 활성화
        appsetup = new Appsetup(this, this);
        appsetup.BluetoothEnable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 액션바의 뒤로가기 버튼 클릭 시 이전 화면으로 돌아가도록 처리
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        locker_1 = findViewById(R.id.locker1);
        locker_2 = findViewById(R.id.locker2);

        // 사물함 상태를 확인하기 위해 HTTP 요청을 보냅니다.
        String url = "http://facelocker.dothome.co.kr/using_locker.php";
        new HttpGetRequest().execute(url);
    }

    private class HttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String result = "";
            HttpURLConnection urlConnection = null;

            try {
                URL requestUrl = new URL(url);
                urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = readStream(urlConnection.getInputStream());
                } else {
                    result = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                try {
                    // JSON 응답을 파싱하여 사용 가능한 빈 사물함 번호를 얻습니다.
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray availableLockers = jsonObj.getJSONArray("availableLockers");

                    // 1번 사물함 상태 확인
                    if (availableLockers.toString().contains("1")) {
                        locker_1.setImageResource(R.drawable.full_box);
                    } else {
                        locker_1.setImageResource(R.drawable.blank_box);
                    }

                    // 2번 사물함 상태 확인
                    if (availableLockers.toString().contains("2")) {
                        locker_2.setImageResource(R.drawable.full_box);
                    } else {
                        locker_2.setImageResource(R.drawable.blank_box);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(locker.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(locker.this, "네트워크에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        private String readStream(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        }
    }
}
