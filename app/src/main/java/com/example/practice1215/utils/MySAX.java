package com.example.practice1215.utils;

import com.example.practice1215.bean.LinkMan;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanjun on 2016/2/2.
 */
public class MySAX extends DefaultHandler {        //继承DefaultHandler
    private List<LinkMan> all = null;              //保存所有元素
    private String elementName = null;             //保存元素名称
    private LinkMan man = null;                    //定义封装的对象

    @Override
    public void startDocument() throws SAXException {
//        super.startDocument();
        this.all = new ArrayList<LinkMan>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        super.startElement(uri, localName, qName, attributes);
        if ("linkman".equals(localName)) {
            this.man = new LinkMan();
        }
        this.elementName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
//        super.characters(ch, start, length);
        if (this.elementName != null) {
            String data = new String(ch, start, length);
            if ("name".equals(this.elementName)) {
                this.man.setName(data);
            } else if ("email".equals(this.elementName)) {
                this.man.setEmail(data);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        super.endElement(uri, localName, qName);
        if ("linkman".equals(localName)) {
            this.all.add(this.man);
            this.man = null;
        }
        this.elementName = null;
    }

    public List<LinkMan> getAll() {
        return this.all;
    }
}
