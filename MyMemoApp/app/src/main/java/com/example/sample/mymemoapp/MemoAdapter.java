package com.example.sample.mymemoapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by yoshitaka.fujisawa on 2017/07/07.
 */

public class MemoAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    static class ViewHolder {
        TextView title;
        TextView lastModified;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.ContentText);
            lastModified = (TextView) view.findViewById(R.id.UpdateTimestamp);
        }
    }

    public MemoAdapter(Context context, Cursor c) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.memo_list_row, null);
        ViewHolder holder = new ViewHolder(view);//TODO: Is it OK??
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(MemoDBHelper.TITLE));
        String lastModified = cursor.getString(cursor.getColumnIndex(MemoDBHelper.DATE_MODIFIED));

        ViewHolder holder = (ViewHolder)view.getTag();
        holder.title.setText(title);
        holder.lastModified.setText(lastModified);
    }
}
