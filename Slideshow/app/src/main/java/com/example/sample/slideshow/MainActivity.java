package com.example.sample.slideshow;

/**
 * Created by YoshitakaFujisawa on 2017/08/10.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import utils.Util;

public class MainActivity extends Activity implements View.OnClickListener, ViewFactory {

    Gallery gallery;
    ImageSwitcher imageSwitcher;
    Context context;
    int imageItem;
    int idxImage = 0; //選択中の画像Index

    Integer[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        context = MainActivity.this;

        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageItem = position;
                imageSwitcher.setImageResource(images[position]);
            }
        });

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(MainActivity.this);

        //Setting 'In Animation'
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        //Setting 'Out Animation'
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));

        imageSwitcher.setAlpha(Float.parseFloat("50.0"));

        //img = (ImageView)findViewById(R.id.imageSwitcher);
        //img.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_next:
                idxImage = (idxImage + 1 < images.length) ? idxImage + 1 : idxImage;
                imageSwitcher.setImageResource(images[idxImage]);

                /*
                todo set updated filename
                 */
//                Util ut = new Util();
//                if(ut.checkEditTextInput(numberInput1, numberInput2)){
//                    int result = ut.calc(numberInput1, numberInput2, operatorSelector);
//                    tv_filename.setText(String.valueOf(result));
//                }
                break;
            case R.id.btn_back:
                idxImage = (idxImage > 0) ? idxImage - 1 : idxImage;
                imageSwitcher.setImageResource(images[idxImage]);

                /*
                todo set updated filename
                 */

                break;
        }
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[imageItem]);
        return imageView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenu_01:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Toastメッセージ表示
     */
    protected void showMessage(String msg) {
        Toast.makeText(
                this,
                msg, Toast.LENGTH_SHORT).show();
    }

    /*
    Image操作イベントを定義
     */
    private class ImageAdapter extends BaseAdapter {
        Context context;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return images.length;
        }

        public Object getItem(int position) {
            return images[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(this.context);
            imageView.setImageResource(images[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(200, 150));

            return imageView;
        }
    }

}
