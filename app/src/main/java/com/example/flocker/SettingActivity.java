package com.example.flocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglayout);

        // 액션바 이름
        getSupportActionBar().setTitle("환경설정");
        // 액션바에 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.Setting_Layout,new SettingFragmnet()).commit();

    }

    public static class SettingFragmnet extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

        private SharedPreferences getsavedata;
        private SharedPreferences.Editor setsavedata;
        private Preference Bluetooth_MAC;
        private EditTextPreference setting_MAC;
        private SwitchPreference BluetoothSwitch;

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.pref,rootKey);

            getsavedata = getContext().getSharedPreferences("savedata",MODE_PRIVATE);
            setsavedata = getsavedata.edit();

            //스마트폰 MAC 확인하는 부분 띄우기
            Bluetooth_MAC = findPreference("Bluetooth_MAC");
            setting_MAC = findPreference("setting_MAC");

            BluetoothSwitch = findPreference("BluetoothSwitch");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                BluetoothSwitch.setChecked(true);
            }
            //초기 설정
            String MAC = getsavedata.getString("MAC","");
            setting_MAC.setSummary(MAC);
            setting_MAC.setText(MAC);
            Bluetooth_MAC.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    Intent intent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
                    startActivity(intent);
                    return false;
                }
            });

        }



        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("AlarmSwitch")){
                //알람 설정 적용
            }
            else if (key.equals("BluetoothSwitch")) {
                //블루투스 설정
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                    boolean bluetoothEnabled = sharedPreferences.getBoolean(key,false);
                    if(bluetoothEnabled){
                        //블루투스 설정이 앱 종료시에도 켜짐일때
                        //블루투스 상태 저장
                        setsavedata.putBoolean("bluetooth",bluetoothEnabled);
                        setsavedata.commit();
                    }else {
                        //블루투스 설정이 앱 종료시 꺼짐일때
                        //블루투스 상태 저장
                        setsavedata.putBoolean("bluetooth",bluetoothEnabled);
                        setsavedata.commit();
                    }
                }else {
                    BluetoothSwitch.setChecked(true);
                    Toast.makeText(getContext(),"안드로이드 13(TIRAMISU)이상 은 본 기능이 지원하지 않습니다.",Toast.LENGTH_SHORT).show();
                }


            }
            else if (key.equals("setting_MAC")) {
                //mac 설정할때
                String MAC = setting_MAC.getText().toString().trim();
                setting_MAC.setSummary(MAC);
                setsavedata.putString("MAC",MAC);
                setsavedata.commit();

                //mac 데이터 db 에 전송
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                String getid = getsavedata.getString("id","");
                Macup macup = new Macup(getid, MAC, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(macup);
                //여기까지 데이터 전송


            }
        }


        //이 아래로는 이 앱이 혹시나 강제종료나 다운되었을때 설정된 기능이 유지되도록 하는 기능임
        //onSharedPreferenceChanged 기능을 다시 부르는 ..? 솔직히 정확한건 잘 모르겠음
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
    //액션바에 있는 뒤로가기 버튼을 눌러도 데이터(메인화면 사용자 이름) 그대로 유지
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 액션바의 뒤로가기 버튼 클릭 시 이전 화면으로 돌아가도록 처리
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
