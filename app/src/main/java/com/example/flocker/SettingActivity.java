package com.example.flocker;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    SwitchPreference switchPreference;
    SwitchPreference switchPreference1; // 추가된 SwitchPreference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        switchPreference = (SwitchPreference) findPreference("autoSwitch");
        switchPreference.setSummaryOn("자동");
        switchPreference.setSummaryOff("수동");

        switchPreference1 = (SwitchPreference) findPreference("autoSwitch1"); // 추가된 SwitchPreference
        switchPreference1.setSummaryOn("자동");
        switchPreference1.setSummaryOff("수동");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // 필요한 경우 다른 설정에 대한 처리를 추가합니다.
        return true;
    }
}
