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

    private String loginId;
    private String loginName;
    private String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "Id";
    private static final String TAG_PASSWORD = "Pw";
    private static final String TAG_NAME = "Name";
    private JSONArray peoples = null;
    private ArrayList<HashMap<String, String>> personList;

    private SharedPreferences getsavedata;
    private SharedPreferences.Editor setsavedata;

    private Appsetup appsetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("FaceLocker");

        getsavedata = getSharedPreferences("savedata",MODE_PRIVATE);
        setsavedata = getsavedata.edit();

        appsetup = new Appsetup(this,this);

        appsetup.BluetoothEnable();


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

                        //Toast.makeText(getApplicationContext(),   ""+id+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();
                        // main 액티비티로 이동
                        Intent intent = new Intent(login.this, main.class);
                        //로그인 id 전달
                        intent.putExtra("loginId", loginId);
                        intent.putExtra("loginName", loginName);
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
            String dbnm = person.get(TAG_NAME);
            setsavedata.putString("name",dbnm);
            setsavedata.commit();
            //Toast.makeText(login.this, "ID : " + dbId + ", PW : " + dbPw, Toast.LENGTH_SHORT).show();
            if (dbId.equals(id) && dbPw.equals(pw)) {
                loginId = id;
                loginName = dbnm;
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
                    String id = c.getString(TAG_ID);
                    String password = c.getString(TAG_PASSWORD);
                    String name = c.getString(TAG_NAME);

                    //해시맵 객체 생성
                    HashMap<String, String> persons = new HashMap<>();
                    persons.put(TAG_ID, id);
                    persons.put(TAG_PASSWORD, password);
                    persons.put(TAG_NAME,name);

                    //생성된 해시맵 객체를 personList에 추가
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

    //웹 서버에서 데이터를 가져오는 AsyncTask
    //AsyncTask = 백그라운드 스레드에서 비동기 작업(네트워크 요청, 데이터베이스 엑세스, 파일 입출력) 수행
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

                Intent intent = new Intent(login.this, main.class);
                //로그인 id 전달
                intent.putExtra("loginId", loginId);
                intent.putExtra("loginName", loginName);
                startActivity(intent);
                finish();

                //로그인 눌렀을시 저장되는 아이디 패스워드 데이터 (자동로그인)
                setsavedata.putString("id",id);
                setsavedata.putString("pw",pw);
                setsavedata.commit();

                //전역 프리퍼런스에 id추가
                PreferenceManager.setInt(login.this, "UserID", Integer.parseInt(id));

                // 로그인 성공하였으므로 성공했다고 값 반환, 그리고 메인화면 띄워달라고 요청
                setResult(RESULT_OK);
                //Toast.makeText(getApplicationContext(),   ""+id+"님이 로그인 하셨습니다", Toast.LENGTH_SHORT).show();
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




