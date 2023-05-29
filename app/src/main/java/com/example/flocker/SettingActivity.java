package com.example.flocker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;


public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglayout);

        getSupportFragmentManager().beginTransaction().replace(R.id.Setting_Layout,new SettingFragmnet()).commit();

    }

    public static class SettingFragmnet extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

        private SharedPreferences getsavedata;
        private SharedPreferences.Editor setsavedata;

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.pref,rootKey);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("AlarmSwitch")){
                //알람 설정 적용
            }
            else if (key.equals("BluetoothSwitch")) {
                //블루투스 설정
                boolean bluetoothEnabled = sharedPreferences.getBoolean(key,false);
                if(bluetoothEnabled){
                    //블루투스 설정이 앱 종료시에도 켜짐일때
                    getsavedata = getContext().getSharedPreferences("savedata",MODE_PRIVATE);
                    setsavedata = getsavedata.edit();
                    //블루투스 상태 저장
                    setsavedata.putBoolean("bluetooth",bluetoothEnabled);
                    setsavedata.commit();
                }else {
                    //블루투스 설정이 앱 종료시 꺼짐일때
                    getsavedata = getContext().getSharedPreferences("savedata",MODE_PRIVATE);
                    setsavedata = getsavedata.edit();
                    //블루투스 상태 저장
                    setsavedata.putBoolean("bluetooth",bluetoothEnabled);
                    setsavedata.commit();
                }

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

}
