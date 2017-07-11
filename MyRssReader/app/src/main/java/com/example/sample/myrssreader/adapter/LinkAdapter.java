package com.example.sample.myrssreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sample.myrssreader.R;
import com.example.sample.myrssreader.data.Link;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by YoshitakaFujisawa on 2017/07/09.
 */

public class LinkAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_LINK = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Link> mLinks;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Link link);
    }

    public LinkAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLinks = new ArrayList<>();
    }

    public void setOnItemClinkListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void addItems(List<Link> links) {
        mLinks.addAll(links);
        notifyDataSetChanged();
    }

    public void remoteItem(long feedId) {
        Iterator<Link> iterator = mLinks.iterator();
        while (iterator.hasNext()) {
            Link link = iterator.next();
            if (link.getSiteId() == feedId) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public void clearItems() {
        mLinks.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LINK) {
            View view = mInflater.inflate(R.layout.item_link, parent, false);
            return new LinkViewHolder(view, this);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LinkViewHolder) {
            LinkViewHolder articleHolder = (LinkViewHolder) holder;
            Link link = mLinks.get(position);

            articleHolder.title.setText(link.getTitle());
            articleHolder.description.setText(link.getDescription());
            articleHolder.timeAgo.setText(mContext.getString(R.string.link_publish_date, link.getPubDate()));
        }
    }

    @Override
    public int getItemCount() {
        return mLinks.size();
    }

    public int getItemViewType(int position) {
        return VIEW_TYPE_LINK;
    }

    // ViewHolder for article ist
    private static class LinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinkAdapter adapter;
        private TextView title;
        private TextView description;
        private TextView timeAgo;

        public LinkViewHolder(View itemView, LinkAdapter adapter) {
            super(itemView);

            this.adapter = adapter;
            title = (TextView) itemView.findViewById(R.id.Title);
            description = (TextView) itemView.findViewById(R.id.Description);
            timeAgo = (TextView) itemView.findViewById(R.id.TimeAgo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (adapter.mListener != null) {
                int position = getLayoutPosition();
                Link data = adapter.mLinks.get(position);
                adapter.mListener.onItemClick(data);
            }
        }
    }

}
