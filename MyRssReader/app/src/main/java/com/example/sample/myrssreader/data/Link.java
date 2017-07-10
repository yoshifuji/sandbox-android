package com.example.sample.myrssreader.data;

/**
 * Created by YoshitakaFujisawa on 2017/07/09.
 */

public class Link {

    private long id;
    private String title;
    private String description;
    private long pubData;
    private String url;
    private long siteid;

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

    public long getPubData() {
        return pubData;
    }

    public void setPubData(long pubData) {
        this.pubData = pubData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSiteid() {
        return siteid;
    }

    public void setSiteid(long siteid) {
        this.siteid = siteid;
    }
}
