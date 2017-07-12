package com.example.sample.myrssreader.parser;

import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.data.Site;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * To generate Site, List<Link>
 */
public class RSSParser  {

    private Site site;
    private List<Link> links;

    public boolean parse(InputStream in) {
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        dbfactory.setNamespaceAware(false);

        try {
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse(in);
            in.close();

            FeedParser parser = getParser(document);

            if (parser != null
                    && parser.parse(document)) {
                this.site = parser.getSite();
                this.links = parser.getLinkList();
                return true;
            }

        } catch (ParserConfigurationException |IOException |SAXException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Site getSite() {
        return site;
    }

    public List<Link> getLinkList() {
        return links;
    }

    private FeedParser getParser(Document document) {

        NodeList children = document.getChildNodes();
        FeedParser parser = null;

        for(int i = 0; i < children.getLength(); i++) {
            String childName = children.item(i).getNodeName();

            // rdf:RDF is RSS1.0、rss is RSS2.0
            if (childName.equals("rdf:RDF")) {
                // RSS 1.0
                parser = new RSS1Parser();
            } else if (childName.equals("rss")) {
                // RSS 2.0
                parser = new RSS2Parser();
            }
        }

        return parser;
    }

}
