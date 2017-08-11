package com.example.sample.slideshow;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by YoshitakaFujisawa on 2017/08/11.
 */

public class SettingsPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

}
