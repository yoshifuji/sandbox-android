package com.example.sample.myrssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sample.myrssreader.R;
import com.example.sample.myrssreader.data.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoshitakaFujisawa on 2017/07/09.
 */

public class SiteAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Site> mSites;

    public SiteAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSites = new ArrayList<>();
    }

    public void addItem(Site site) {
        mSites.add(site);
        notifyDataSetChanged();
    }

    public void addAll(List<Site> sites) {
        mSites.addAll(sites);
        notifyDataSetChanged();
    }

    public void removeItem(long siteId) {
        int size = mSites.size();
        int position = -1;
        for (int i = 0; i < size; i++) {
            Site site = mSites.get(i);
            if (siteId == site.getId()) {
                mSites.remove(position);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public int getCount() {
        return mSites.size();
    }

    @Override
    public Object getItem(int position) {
        return mSites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SiteViewHolder holder;
        View itemView;

        if (convertView == null) {
            itemView = mInflater.inflate(R.layout.item_site, parent, false);
            holder = new SiteViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            itemView = convertView;
            holder = (SiteViewHolder) convertView.getTag();
        }

        Site site = (Site) getItem(position);
        holder.title.setText(site.getTitle());
        holder.linksCount.setText(mContext.getString(R.string.site_link_count, site.getLinkCount()));

        return itemView;
    }

    private static class SiteViewHolder {
        private TextView title;
        private TextView linksCount;

        public SiteViewHolder(View itemView) {
            title = (TextView) itemView.findViewById(R.id.Title);
            linksCount = (TextView) itemView.findViewById(R.id.ArticlesCount);
        }
    }
}
