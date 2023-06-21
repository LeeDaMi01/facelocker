package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class log extends AppCompatActivity {
    private JSONArray logs = null;

    private Appsetup appsetup;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<logItem> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //액션바 이름
        getSupportActionBar().setTitle("개폐 로그 확인");
        //액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //블루투스 활성화
        appsetup = new Appsetup(this, this);
        appsetup.BluetoothEnable();

        int UserID = PreferenceManager.getInt(this, "UserID"); // ID받아오기

        String url = "http://facelocker.dothome.co.kr/log.php?userID=" + UserID;
        //String url = "http://facelocker.dothome.co.kr/logex.php";

        // 리사이클러뷰 시작
        recyclerView = findViewById(R.id.logRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogAdapter(dataList);
        recyclerView.setAdapter(adapter);

        getData(url);
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

    private void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json).append("\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj.has("result")) {
                        logs = jsonObj.getJSONArray("result");

                        if (logs.length() > 0) {
                            for (int i = 0; i < logs.length(); i++) {
                                JSONObject L = logs.getJSONObject(i);
                                int lockerNum = L.getInt("Locker_num");
                                String openTimeStr = L.getString("Opentime");

                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date parsedDate = inputFormat.parse(openTimeStr);
                                Timestamp openTime = new Timestamp(parsedDate.getTime());

                                logItem logItem = new logItem(lockerNum, openTime);
                                dataList.add(logItem);
                            }
                            adapter.notifyDataSetChanged(); // 어댑터 아이템 변경을 알림
                        }
                        else{
                            Toast.makeText(log.this, "개폐 로그가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(log.this, "opPostExecute Exception 발생", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(log.this, "날짜 형식 변환 중 에러 발생", Toast.LENGTH_SHORT).show();
                }
            }

        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public class logItem {
        int locNum;
        Timestamp openTime;

        public logItem(int locNum, Timestamp openTime) {
            this.locNum = locNum;
            this.openTime = openTime;
        }
    }
}
