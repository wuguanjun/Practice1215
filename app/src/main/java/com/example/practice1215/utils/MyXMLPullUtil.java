package com.example.practice1215.utils;

import com.example.practice1215.bean.LinkMan;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guanjun on 2016/2/2.
 */
public class MyXMLPullUtil {
    private InputStream input = null;
    private OutputStream output = null;
    private List<LinkMan> allOut = null;

    public MyXMLPullUtil(InputStream in) {
        input = in;
    }

    public MyXMLPullUtil(OutputStream out, List<LinkMan> all) {
        this.output = out;
        this.allOut = all;
    }

    public List<LinkMan> getAllLinkMan() throws Exception {
        List<LinkMan> all = null;
        LinkMan man = null;
        String elementName = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = null;

        parser = factory.newPullParser();
        parser.setInput(this.input, "GBK");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                all = new ArrayList<LinkMan>();
            } else if (eventType == XmlPullParser.START_TAG) {
                elementName = parser.getName();
                if ("linkman".equals(elementName)) {
                    man = new LinkMan();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                elementName = parser.getName();
                if ("linkman".equals(elementName)) {
                    all.add(man);
                    man = null;
                }
            } else if (eventType == XmlPullParser.TEXT) {
                if ("name".equals(elementName)) {
                    man.setName(parser.getText());
                } else if ("email".equals(elementName)) {
                    man.setEmail(parser.getText());
                }
            }
            eventType = parser.next();
        }
        return all;
    }

    public void save() throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xs = factory.newSerializer();
        xs.setOutput(this.output, "UTF-8");
        xs.startDocument("UTF_8", true);
        xs.startTag(null, "addresslist");
        Iterator<LinkMan> iter = allOut.iterator();
        while (iter.hasNext()) {
            LinkMan man = iter.next();
            xs.startTag(null, "linkman");
            xs.startTag(null, "name");
            xs.text(man.getName());
            xs.endTag(null, "name");
            xs.startTag(null, "email");
            xs.text(man.getEmail());
            xs.endTag(null, "email");
            xs.endTag(null, "linkman");
        }
        xs.endTag(null, "addresslist");
        xs.endDocument();
        xs.flush();
    }
}
