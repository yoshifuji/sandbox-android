package com.example.sample.mymemoapp;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by yoshitaka.fujisawa on 2017/07/06.
 */

public class MemoFragment extends Fragment {
    private MemoEditText mMemoEditText;
    private Uri mMemoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //generate layout xml
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        mMemoEditText = (MemoEditText) view.findViewById(R.id.Memo);
        return view;
    }

    public void reflectSettings() {
        Context context = getActivity();

        if (context != null) {
            setFontSize(SettingPrefUtil.getFontSize(context));
            setTypeface(SettingPrefUtil.getTypeface(context));
            setMemoColor(SettingPrefUtil.isScreenReverse(context));
        }
    }

    private void setFontSize(float fontSizePx) {
        mMemoEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizePx);
    }

    private void setTypeface(int typeface) {
        mMemoEditText.setTypeface(Typeface.DEFAULT, typeface);
    }

    private void setMemoColor(boolean reverse) {
        int backgroundColor = reverse ? Color.BLACK : Color.WHITE;
        int textColor = reverse ? Color.WHITE : Color.BLACK;

        mMemoEditText.setBackgroundColor(backgroundColor);
        mMemoEditText.setTextColor(textColor);
    }

    public void save() {
        if (mMemoUri != null)
            MemoRepository.update(getActivity(), mMemoUri, mMemoEditText.getText().toString());
        else
            MemoRepository.create(getActivity(), mMemoEditText.getText().toString());

        Toast.makeText(getActivity(), "保存しました", Toast.LENGTH_SHORT).show();
    }

    public void load(Uri uri) {
        mMemoUri = uri;

        if (uri != null) {
            String memo = MemoRepository.findMemoByUri(getActivity(), uri);
            mMemoEditText.setText(memo);
        } else {
            mMemoEditText.setText(null); //with null URI
        }
    }

}
