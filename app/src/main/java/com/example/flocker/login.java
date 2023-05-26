package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class login extends AppCompatActivity {
    EditText number, password;
    Button login2;

    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_PASSWORD = "user_password";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("facelocker");

        //EditText와 Button을 초기화
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);
        login2 = findViewById(R.id.login2);
        personList = new ArrayList<>();

        //웹 서버에서 데이터 가져오기
        getData("http://facelocker.dothome.co.kr/login.php");

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = number.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if (id.isEmpty() || pw.isEmpty()) {
                    // 아이디와 비밀번호를 입력하지 않은 경우
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // DB에 있는 정보와 비교하여 로그인 처리
                    if (checkCredentials(id, pw)) {
                        Toast.makeText(getApplicationContext(),   ""+id+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();
                        // main 액티비티로 이동
                        Intent intent = new Intent(getApplicationContext(), main.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } else {
                        // 로그인 실패 시의 동작
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkCredentials(String id, String pw) {
        for (HashMap<String, String> person : personList) {
            String dbId = person.get(TAG_ID);
            String dbPw = person.get(TAG_PASSWORD);

            if (dbId.equals(id) && dbPw.equals(pw)) {
                return true; // ID와 비밀번호가 일치하는 경우
            }
        }
        return false; // ID와 비밀번호가 일치하지 않는 경우
    }


    protected void showList() {
        if (myJSON != null) {
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString(TAG_ID);
                    String password = c.getString(TAG_PASSWORD);

                    HashMap<String, String> persons = new HashMap<>();
                    persons.put(TAG_ID, id);
                    persons.put(TAG_PASSWORD, password);

                    personList.add(persons);
                }

                SimpleAdapter adapter = new SimpleAdapter(
                        login.this, personList, android.R.layout.simple_list_item_2,
                        new String[]{TAG_ID, TAG_PASSWORD},
                        new int[]{android.R.id.text1, android.R.id.text2}
                );


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData(String url) {
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
                    myJSON = result;
                    showList();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}




