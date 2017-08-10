package com.example.sample.demoslider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by YoshitakaFujisawa on 2017/08/10.
 */

public class AndroidImageAdapter extends PagerAdapter {
    Context mContext;

    AndroidImageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }

    /*
    以下でスライド画像で表示する画像ファイルを設定する。
    ファイル配置場所は、/app/res/drawable/直下としています。
     */
    private int[] sliderImagesId = new int[]{
            R.drawable.image1, R.drawable.image2, R.drawable.image3,
    };

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(sliderImagesId[i]);
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}