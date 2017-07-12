package com.example.sample.myrssreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.sample.myrssreader.database.RssRepository;

/**
 * Created by yoshitaka.fujisawa on 2017/07/12.
 */

public class DeleteSiteLoader extends AsyncTaskLoader<Integer> {
    private long id;

    public DeleteSiteLoader(Context context) {
        super(context);
        this.id = id;
    }

    public long getTargetId(){
        return id;
    }

    @Override
    public Integer loadInBackground(){
        return RssRepository.deleteSite(getContext(), id);
    }
}
