package com.example.sample.slideshow;

/**
 * Created by YoshitakaFujisawa on 2017/08/10.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

import java.io.Console;

public class MainActivity extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, ViewFactory {

    Gallery gallery;
    ImageSwitcher imageSwitcher;
    Context context;
    int imageItem;
    // 選択中の画像Index
    int imageIdx = 0;

    // イメージ画像定義
    Integer[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image5,
            R.drawable.image4};

    // タッチイベントを処理するためのインタフェース
    private GestureDetector mGestureDetector;
    // X軸最低スワイプ距離
    private static final int SWIPE_MIN_DISTANCE = 50;
    // X軸最低スワイプスピード
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    // Y軸の移動距離　これ以上なら横移動を判定しない
    private static final int SWIPE_MAX_OFF_PATH = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        //findViewById(R.id.imageSwitcher).setOnTouchListener(this);

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

        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        //img = (ImageView)findViewById(R.id.imageSwitcher);
        //img.setOnTouchListener(this);

    }

    /*
    ボタンクリック操作を実装
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_next:
                imageIdx = (imageIdx + 1 < images.length) ? imageIdx + 1 : imageIdx;
                imageSwitcher.setImageResource(images[imageIdx]);

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
                imageIdx = (imageIdx > 0) ? imageIdx - 1 : imageIdx;
                imageSwitcher.setImageResource(images[imageIdx]);

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

    /*
    メニュー操作実装
     */
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
    マウスジェスチャーイベント定義
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

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

    /*
    タッチイベントのリスナー
     */
    private final GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        // フリックイベント
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            try {
                // 移動距離・スピードを出力
                float distance_x = Math.abs((event1.getX() - event2.getX()));
                float velocity_x = Math.abs(velocityX);
                //showMessage("横の移動距離:" + distance_x + " 横の移動スピード:" + velocity_x);

                // Y軸の移動距離が大きすぎる場合
                if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) {
                    showMessage("縦の移動距離が大きすぎ");
                }
                // 開始位置から終了位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if  (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //showMessage("右から左");
                    imageIdx = (imageIdx > 0) ? imageIdx - 1 : imageIdx;
                    imageSwitcher.setImageResource(images[imageIdx]);
                }
                // 終了位置から開始位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //showMessage("左から右");
                    imageIdx = (imageIdx + 1 < images.length) ? imageIdx + 1 : imageIdx;
                    imageSwitcher.setImageResource(images[imageIdx]);
                }

                return true;
            } catch (Exception e) {
                // TODO
            }

            return false;
        }
    };

}
