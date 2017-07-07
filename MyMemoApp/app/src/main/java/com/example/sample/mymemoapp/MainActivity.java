package com.example.sample.mymemoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements MemoLoadFragment.MemoLoadFragmentListener, SettingFragment.SettingFragmentListener {

    //2pane?
    private boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isDualPane = (findViewById(R.id.FragmentContainer) != null);
        if (isDualPane) {
            getFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new MemoLoadFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                if (isDualPane) {
                    // 2pane
                    android.app.FragmentManager manager = getFragmentManager();
                    if (manager.findFragmentById(R.id.FragmentContainer) instanceof SettingFragment) {
                        // fragment exist
                        getFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new MemoLoadFragment()).commit();
                    } else {
                        // fragment doesn't exist
                        getFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new SettingFragment()).commit();
                    }
                } else {
                    // single pane
                    Intent intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
                }
                return true;

            case R.id.action_save:
                MemoFragment memoFragment = (MemoFragment)getFragmentManager().findFragmentById(R.id.MemoFragment);
                memoFragment.save();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMemoSelected(@Nullable Uri uri) {
        if(isDualPane){
            // Update memo when 2pane
            MemoFragment memoFragment = (MemoFragment)getFragmentManager().findFragmentById(R.id.MemoFragment);
            memoFragment.load(uri);
        } else {
            // Kick memo when single pane
            Intent intent = new Intent(this, MemoActivity.class);
            intent.putExtra(MemoActivity.BUNDLE_KEY_URI, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onSettingChanged() {
        //2pane only
        MemoFragment memoFragment = (MemoFragment)getFragmentManager().findFragmentById(R.id.MemoFragment);
        memoFragment.reflectSettings();
    }
}
