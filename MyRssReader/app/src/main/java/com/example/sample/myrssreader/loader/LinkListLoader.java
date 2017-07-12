package com.example.sample.myrssreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.database.RssRepository;

import java.util.List;

/**
 * Created by yoshitaka.fujisawa on 2017/07/12.
 */

public class LinkListLoader extends AsyncTaskLoader<List<Link>> {
    public LinkListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Link> loadInBackground() {
        return RssRepository.getAllLinks(getContext());
    }
}
