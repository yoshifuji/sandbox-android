package com.example.sample.mymemoapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import java.util.Collections;
import java.util.Set;

/**
 * Created by yoshitaka.fujisawa on 2017/07/07.
 */

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    //Notify changed event
    public interface SettingFragmentListener {
        void onSettingChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getPreferenceManager().setSharedPreferencesName(SettingRepository.PREF_FILE_NAME); //TODO: Check here later
        getPreferenceManager().setSharedPreferencesName(SettingPrefUtil.PREF_FILE_NAME);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTypefaceSummary(getPreferenceManager().getSharedPreferences());
        setPrefixSummary(getPreferenceManager().getSharedPreferences());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this); //register event listener
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this); //unregister event listener
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Activity activity = getActivity();

        //Notify to activity if implementation exist
        if (activity instanceof SettingFragmentListener) {
            SettingFragmentListener listener = (SettingFragmentListener) activity;
            listener.onSettingChanged();
        }

        if (activity.getString(R.string.key_text_style).equals(key)) {
            setTypefaceSummary(sharedPreferences);
        } else if (activity.getString(R.string.key_file_name_prefix).equals(key)) {
            setPrefixSummary(sharedPreferences);
        }
    }

    //Update typeface
    private void setTypefaceSummary(SharedPreferences prefs) {
        String key = getActivity().getString(R.string.key_text_style);
        Preference preference = findPreference(key);
        Set<String> selected = prefs.getStringSet(key, Collections.<String>emptySet());

        preference.setSummary(TextUtils.join("/", selected.toArray()));
    }

    //Update prefix summary
    private void setPrefixSummary(SharedPreferences prefs) {
        String key = getActivity().getString(R.string.key_file_name_prefix);
        Preference preference = findPreference(key);
        String prefix = prefs.getString(key, "");

        preference.setSummary(prefix);
    }
}
