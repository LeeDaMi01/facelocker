package com.example.flocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


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
            //Toast.makeText(getContext(),key,Toast.LENGTH_SHORT).show();
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
                    BluetoothSwitch.setChecked(false);
                    Toast.makeText(getContext(),"안드로이드 13(TIRAMISU)은 본 기능이 지원하지 않습니다.",Toast.LENGTH_SHORT).show();
                }


            }
            else if (key.equals("setting_MAC")) {
                //mac 설정할때
                String MAC = setting_MAC.getText().toString();
                setting_MAC.setSummary(MAC);
                setsavedata.putString("MAC",MAC);
                setsavedata.commit();

                /*
                db에 넣는거 작성
                 */

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
