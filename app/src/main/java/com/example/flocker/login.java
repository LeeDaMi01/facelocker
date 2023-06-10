package com.example.flocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private EditText number, password;
    private Button login2;

    private String myJSON;

    private String loggedInId;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_PASSWORD = "user_password";
    private static final String TAG_NAME = "name";
    private JSONArray peoples = null;
    private ArrayList<HashMap<String, String>> personList;

    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;

    private Bluetooth bluetooth;


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

                LoginLogic(id,pw);

                if (id.isEmpty() || pw.isEmpty()) {
                    // 아이디와 비밀번호를 입력하지 않은 경우
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // DB에 있는 정보와 비교하여 로그인 처리
                    if (checkCredentials(id, pw)) {

                        Toast.makeText(getApplicationContext(),   ""+id+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();
                        // main 액티비티로 이동
                        Intent intent = new Intent(login.this, main.class);
                        //로그인 id 전달
                        intent.putExtra("loggedInId", loggedInId);
                        startActivity(intent);
                        finish();

                    } else {
                        // 로그인 실패 시의 동작
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //클릭 했을시 로그인 시도
    private boolean checkCredentials(String id, String pw) {
        for (HashMap<String, String> person : personList) {
            String dbId = person.get(TAG_ID);
            String dbPw = person.get(TAG_PASSWORD);

            if (dbId.equals(id) && dbPw.equals(pw)) {
                loggedInId = id;
                return true; //id 비밀번호가 일치했을때
            }
        }
        return false; // ID와 비밀번호가 일치하지 않는 경우
    }


    protected void showList() {
        if (myJSON != null) {
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS); //로그인 결과 가져오기 (result)

                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString(TAG_ID); // user_id 가져와서 id로 저장
                    String password = c.getString(TAG_PASSWORD); //user_password 가져와서 password로 저장
                    String name = c.getString(TAG_NAME); // name 가져와서 name으로 저장

                    //해시맵 객체 생성
                    HashMap<String, String> persons = new HashMap<>();
                    persons.put(TAG_ID, id);
                    persons.put(TAG_PASSWORD, password);
                    persons.put(TAG_NAME, name);

                    //생성된 해시맵 객체를 personList에 추가
                    personList.add(persons);
                }
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
                    URL url = new URL(uri); // url 객체 생성
                    HttpURLConnection con = (HttpURLConnection) url.openConnection(); //url 사용해서 http 연결, HttpURLConeection 객체 생성
                    StringBuilder sb = new StringBuilder(); //데이터 저장하기 위한 StringBuilder 생성

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream())); // 데이터 읽기 위해 BufferedReader 객체 생성

                    String json;
                    //한 줄씩 데이터 읽어와서 StringBuilder 객체에 추가
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json).append("\n");
                    }

                    //문자열 반환, 공백 문자 제거
                    return sb.toString().trim();

                    //예외처리
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

                    //자동로그인 구현
                    getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
                    String saveid = getsavedata.getString("id","");
                    String savepw = getsavedata.getString("pw","");
                    if(saveid.length() != 0 || savepw.length() != 0){
                        LoginLogic(saveid,savepw);
                    }
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void LoginLogic(String id, String pw){

        getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
        setsavedata = getsavedata.edit();

        if (id.isEmpty() || pw.isEmpty()) {
            // 아이디와 비밀번호를 입력하지 않은 경우
            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            // DB에 있는 정보와 비교하여 로그인 처리
            if (checkCredentials(id, pw)) {

                //로그인 눌렀을시 저장되는 아이디 패스워드 데이터 (자동로그인)
                setsavedata.putString("id",id);
                setsavedata.putString("pw",pw);
                setsavedata.commit();

                // 로그인 성공하였음으로 성공했다고 값 반환, 그리고 메인화면 띄워달라고 요청
                setResult(RESULT_OK);
                Toast.makeText(getApplicationContext(),   ""+id+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();
                //로그인에 성공하였음으로 로그인 화면 종료
                finish();
            } else {
                // 로그인 실패 시의 동작

                //자동로그인 실패시 id 또는 pw 가 변경되었음 보통 주로 pw 만 변경함으로 id 만 제공함
                number = findViewById(R.id.number);
                number.setText(getsavedata.getString("id",""));

                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




