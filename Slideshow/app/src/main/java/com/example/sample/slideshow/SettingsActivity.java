package com.example.sample.slideshow;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

/**
 * Created by YoshitakaFujisawa on 2017/08/11.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content,
                new SettingsPreferenceFragment());
        fragmentTransaction.commit();
    }

}
