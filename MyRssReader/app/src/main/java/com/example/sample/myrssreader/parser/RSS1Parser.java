package com.example.sample.myrssreader.parser;

import android.text.TextUtils;

import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.data.Site;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * RSS1 Parser
 */
public class RSS1Parser implements FeedParser {

    private Site site;

    private List<Link> links;

    @Override
    public boolean parse(Document document) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();

        try {
            this.site = new Site();

            String title = xPath.evaluate("//channel/title/text()", document);
            this.site.setTitle(title);

            String description = xPath.evaluate("//channel/description/text()", document);
            this.site.setDescription(description);

            this.links = new ArrayList<>();

            NodeList items = (NodeList)xPath.evaluate("//item", document, XPathConstants.NODESET);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.ENGLISH);

            int article_count = items.getLength();
            for (int i = 0; i < article_count; i++) {
                Node item = items.item(i);

                Link link = new Link();
                link.setTitle(xPath.evaluate("./title/text()", item)); // タイトル
                link.setUrl(xPath.evaluate("./link/text()", item)); // URL
                link.setDescription(xPath.evaluate("./description/text()", item)); // 説明

                String pubDate = xPath.evaluate("./date/text()", item); // 発行日

                if (TextUtils.isEmpty(pubDate)) {
                    link.setPubDate(-1L);
                } else {
                    Date publishTime = dateFormat.parse(pubDate);
                    link.setPubDate(publishTime.getTime());
                }

                links.add(link);
            }

            return true;

        } catch (XPathExpressionException | ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public List<Link> getLinkList() {
        return links;
    }
}
