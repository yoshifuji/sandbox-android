package com.example.sample.myrssreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;

import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.data.Site;
import com.example.sample.myrssreader.database.RssRepository;
import com.example.sample.myrssreader.net.HttpGet;
import com.example.sample.myrssreader.parser.RSSParser;

import java.io.InputStream;
import java.util.List;

/**
 * Created by YoshitakaFujisawa on 2017/07/12.
 */

public class AddSiteLoader extends AsyncTaskLoader {
    private String url;

    public AddSiteLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Site loadInBackground() {

        if (!TextUtils.isEmpty(this.url)) {
            HttpGet httpGet = new HttpGet(this.url);
            if (!httpGet.get()) return null;

            InputStream in = httpGet.getResponse();
            RSSParser parser = new RSSParser();
            if (!parser.parse(in)) return null;

            Site site = parser.getSite();
            List<Link> links = parser.getLinkList();

            site.setUrl(url);
            site.setLinkCount(links.size());

            long feedId = RssRepository.insertSite(getContext(), site);
            site.setId(feedId);
            if (feedId > 0 && links.size() > 0) {
                RssRepository.insertLinks(getContext(), feedId, links);
                return site;
            }
        }

        return null;
    }
}
