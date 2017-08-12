package com.example.sample.slideshow;

/**
 * Created by YoshitakaFujisawa on 2017/08/10.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.Formatter;

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
            R.drawable.image4,
            R.drawable.image5};

    // 画像ファイル名表示部
    private TextView tvFileName;

    // タッチイベントを処理するためのインタフェース
    private GestureDetector mGestureDetector;
    // X軸最低スワイプ距離
    private static final int SWIPE_MIN_DISTANCE = 50;
    // X軸最低スワイプスピード
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    // Y軸の移動距離　これ以上なら横移動を判定しない
    private static final int SWIPE_MAX_OFF_PATH = 250;
    // 識別用のコード
    private final static int CHOSE_FILE_CODE = 12345;

    //自動再生ハンドラ
    private final Handler handler = new Handler();
    private Runnable runnable;
    //自動再生時間間隔(単位はmsec)
    private final int interval = 3000;

    //Preference画面の設定値
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        tvFileName = (TextView)findViewById(R.id.tv_filename);

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
        //Set 'In Animation'
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        //Set 'Out Animation'
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
        imageSwitcher.setAlpha(Float.parseFloat("50.0"));

        //Set 'ジェスチャー操作'
        mGestureDetector = new GestureDetector(this, mOnGestureListener);

        //画像の自動再生処理
        runnable = new Runnable() {
            @Override
            public void run() {

                imageIdx++;
                imageIdx = imageIdx % images.length;//画像数の周期で巡回させる
                //      Log.d("Intro Screen", "Change Image " + index);
                imageSwitcher.setImageResource(images[imageIdx]);
                handler.postDelayed(this, interval);
            }
        };
        //自動再生ONにする：handler.postDelayed(runnable, interval);
        //自動再生OFFにする：handler.removeCallbacks(runnable);

        //画像ファイル名を設定
        setFileName();
    }

    /*
    ボタンクリック操作を実装
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_next:
                showRightImage();
                break;
            case R.id.btn_back:
                showLeftImage();
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
    Preferenceメニューを表示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    /*
    Preferenceメニュー操作を実装
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenu_01:
                //IntentでPreferenceの設定を取得
                Intent intent = new Intent(this, SettingsActivity.class);
                //startActivityForResult(intent, REQUEST_CODE);
                break;
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
    画像操作イベントを定義
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
                    showMessage("フリック失敗：縦の移動距離が大きすぎます");
                }
                // 開始位置から終了位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if  (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //showMessage("右から左");
                    showRightImage();
                }
                // 終了位置から開始位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //showMessage("左から右");
                    showLeftImage();
                }

                return true;
            } catch (Exception e) {
                // TODO
            }

            return false;
        }
    };

    /*
    右隣りの画像を表示
     */
    private void showRightImage() {
        imageIdx = (imageIdx + 1 < images.length) ? imageIdx + 1 : imageIdx;
        imageSwitcher.setImageResource(images[imageIdx]);
        setFileName();
    }

    /*
    左隣りの画像を表示
     */
    private void showLeftImage() {
        imageIdx = (imageIdx > 0) ? imageIdx - 1 : imageIdx;
        imageSwitcher.setImageResource(images[imageIdx]);
        setFileName();
    }

    /*
    画像ファイル名を表示
     */
    private void setFileName(){
        Formatter fm = new Formatter();
        fm.format("image%s", imageIdx + 1);
        tvFileName.setText(String.valueOf(fm));
    }

}
