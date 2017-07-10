package com.example.sample.myrssreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.sample.myrssreader.data.Link;

import java.util.List;

/**
 * Created by YoshitakaFujisawa on 2017/07/09.
 */

public class LinkAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_LINK = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Link> mLinks;

    private AdapterView.OnItemClickListener mListener;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
