package com.example.sample.mymemoapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by yoshitaka.fujisawa on 2017/07/07.
 */

public class MemoActivity extends Activity {
    public static final String BUNDLE_KEY_URI = "uri";
    private static final int REQUEST_SETTING = 0;

    @Override
    protected void onCreate(Bundle sevedInstanceState) {
        super.onCreate(sevedInstanceState);
        setContentView(R.layout.activity_memo);
        Uri uri = getIntent().getParcelableExtra(BUNDLE_KEY_URI);

        //Load memo fragment
        MemoFragment memoFragment = (MemoFragment) getFragmentManager().findFragmentById(R.id.MemoFragment);
        memoFragment.load(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Setting action
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_SETTING);
                return true;
            // Save action
            case R.id.action_save:
                MemoFragment memoFragment = (MemoFragment)getFragmentManager().findFragmentById(R.id.MemoFragment);
                memoFragment.save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_SETTING && resultCode == RESULT_OK){
            MemoFragment memoFragment = (MemoFragment)getFragmentManager().findFragmentById(R.id.MemoFragment);
            memoFragment.reflectSettings();
        }
    }

}
