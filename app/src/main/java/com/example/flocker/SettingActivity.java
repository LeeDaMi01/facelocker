package com.example.flocker;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import androidx.appcompat.app.ActionBar;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    SwitchPreference switchPreference;
    SwitchPreference switchPreference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.pref);

        switchPreference = (SwitchPreference) findPreference("autoSwitch");
        switchPreference1 = (SwitchPreference) findPreference("autoSwitch1"); // 추가된 SwitchPreference

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        return true;
    }
}
