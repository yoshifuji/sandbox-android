package com.example.sample.myrssreader.parser;

import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.data.Site;

import org.w3c.dom.Document;

import java.util.List;

/**
 * RSS 1.0 / 2.0 / ATOM Parser Interface
 */
public interface FeedParser {

    boolean parse(Document document);
    Site getSite();
    List<Link> getLinkList();

}
