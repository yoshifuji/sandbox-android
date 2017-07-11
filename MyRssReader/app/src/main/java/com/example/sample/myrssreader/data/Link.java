package com.example.sample.myrssreader.data;

/**
 * Created by YoshitakaFujisawa on 2017/07/09.
 */

public class Link {

    private long id;
    private String title;
    private String description;
    private long pubDate;
    private String url;
    private long siteId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

}
