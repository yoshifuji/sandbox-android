package com.example.sample.myrssreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.sample.myrssreader.data.Site;
import com.example.sample.myrssreader.database.RssRepository;

import java.util.List;

/**
 * Created by yoshitaka.fujisawa on 2017/07/12.
 */

public class SiteListLoader extends AsyncTaskLoader<List<Site>> {
    public SiteListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Site> loadInBackground() {
        return RssRepository.getAllSites(getContext());
    }
}
