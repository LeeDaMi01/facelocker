package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class log extends AppCompatActivity {
    private String logJSON;
    private JSONArray logs = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<logItem> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        // 액션바 이름
        getSupportActionBar().setTitle("개페 로그 확인");
        // 액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int UserID = PreferenceManager.getInt(this, "UserID");//ID받아오기

        String url ="http://facelocker.dothome.co.kr/log.php?userID= + UserID";

        // 리사이클러뷰 시작
        recyclerView = findViewById(R.id.logRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogAdapter(dataList);
        recyclerView.setAdapter(adapter);
        getData(url);

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
                if (result != null) {
                    try {
                        JSONObject JsonObj = new JSONObject(result);
                        logs = JsonObj.getJSONArray("result");


                        for (int i = 0; i < logs.length(); i++) {
                            JSONObject L = logs.getJSONObject(i);
                            int lockerNum = L.getInt("Locker_num");
                            String openTimeStr =L.getString("Opentime");

                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date parsedDate = inputFormat.parse(openTimeStr);
                            Timestamp openTime = new Timestamp(parsedDate.getTime());

                            logItem logItem = new logItem(lockerNum, openTime);
                            dataList.add(logItem);
                        }
                        adapter.notifyDataSetChanged();//어댑터 아이템 변경을 알림

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(log.this, "온포스트엑스큐트에서 익셉션 발생", Toast.LENGTH_SHORT).show();
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(log.this, "날짜 형식 변환 중 에러 발생", Toast.LENGTH_SHORT).show();
                    }
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