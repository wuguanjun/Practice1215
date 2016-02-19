package com.example.practice1215.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice1215.R;
import com.example.practice1215.activity.baseActivity.BaseActivity;
import com.example.practice1215.bean.LinkMan;
import com.example.practice1215.utils.MySAX;
import com.example.practice1215.utils.MyXMLPullUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by guanjun on 2016/2/1.
 */
public class ResOperateTestActivity extends BaseActivity {
    private TextView tv_content;
    private EditText et_name, et_email;
    private EditText et_read_name, et_read_email;

    //SAX
    private EditText et_sax_name, et_sax_email;
    //XML
    private EditText et_xml_name, et_xml_email;

    //json
    private TextView tv_json_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("Resource Operation");
        initView();
    }

    private void initView() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_read_name = (EditText) findViewById(R.id.et_read_name);
        et_read_email = (EditText) findViewById(R.id.et_read_email);
        et_sax_name = (EditText) findViewById(R.id.et_sax_name);
        et_sax_email = (EditText) findViewById(R.id.et_sax_email);
        et_xml_name = (EditText) findViewById(R.id.et_xml_name);
        et_xml_email = (EditText) findViewById(R.id.et_xml_email);

        tv_json_content = (TextView) findViewById(R.id.tv_json_content);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_read_raw:
                readRaw();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_read:
                read();
                break;
            case R.id.btn_sax:
                sax();
                break;
            case R.id.btn_xml:
                xml();
                break;
            case R.id.btn_json:
                jsonSave();
                break;
            case R.id.btn_json_save1:
                jsonSave1();
                break;
            case R.id.btn_json_parse:
                jsonParse();
                break;
        }
    }

    /**
     * json解析
     */
    private void jsonParse() {
        String str = "[{\"id\":1,\"name\":\"李兴华\",\"age\":30},{\"id\":2,\"name\":\"MLDN\",\"age\":10}]";
        StringBuffer buf = new StringBuffer();
        try {
            List<Map<String, Object>> all = parseJson(str);
            Iterator<Map<String, Object>> iter = all.iterator();
            while (iter.hasNext()) {
                Map<String, Object> map = iter.next();
                buf.append("ID: " + map.get("id") + ",姓名：" + map.get("name") + ",年龄：" + map.get("age") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_json_content.setText(buf.toString());
    }

    public List<Map<String, Object>> parseJson(String data) throws Exception {
        List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
        JSONArray array = new JSONArray(data);
        for (int x = 0; x < array.length(); x++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject obj = array.getJSONObject(x);
            map.put("id", obj.getInt("id"));
            map.put("name", obj.getString("name"));
            map.put("age", obj.getInt("age"));
            all.add(map);
        }
        return all;
    }

    private void jsonSave1() {
        String nameData[] = new String[]{"李兴华", "魔乐科技", "MLDN"};
        int ageData[] = new int[]{30, 5, 7};
        boolean isMarriedData[] = new boolean[]{false, true, false};
        double salaryData[] = new double[]{3000.0, 5000.0, 9000.0};
        Date birthdayData[] = new Date[]{new Date(), new Date(), new Date()};
        String companyName = "北京魔乐科技软件学院（MLDN软件实训中心）";
        String companyAddr = "北京市西城区美江大厦6层";
        String companyTel = "010-51283346";

        JSONObject allData = new JSONObject();
        JSONArray sing = new JSONArray();
        for (int x = 0; x < nameData.length; x++) {
            JSONObject temp = new JSONObject();
            try {
                temp.put("name", nameData[x]);
                temp.put("age", ageData[x]);
                temp.put("married", isMarriedData[x]);
                temp.put("salary", salaryData[x]);
                temp.put("birthday", birthdayData[x]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sing.put(temp);
        }

        try {
            allData.put("persondata", sing);
            allData.put("company", companyName);
            allData.put("address", companyAddr);
            allData.put("telephone", companyTel);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "jsonparse.txt");  //定义File类对象
        if (!file.getParentFile().exists()) {    //父文件夹不存在时，创建
            file.getParentFile().mkdirs();
        }

        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            out.print(allData.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        File file2 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "jsonparse.txt");  //定义File类对象

        InputStream in = null;
        try {
            in = new FileInputStream(file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[8];
        try {
            while (in.read(b) > 0) {
                sb.append(new String(b));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * json保存
     */
    private void jsonSave() {
        String data[] = {"www.mldnjava.cn", "lixinghua", "bbs.mldn.cn"};
        JSONObject allData = new JSONObject();    //最外层json对象
        JSONArray sing = new JSONArray();         //定义新的JSONArray对象

        for (int x = 0; x < data.length; x++) {   //创建一个新的JSONObject对象
            JSONObject temp = new JSONObject();
            try {
                temp.put("myurl", data[x]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sing.put(temp);
        }

        //保存所有数据
        try {
            allData.put("urldata", sing);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {   //判断sd卡是否存在
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "json.txt");  //定义File类对象
        if (!file.getParentFile().exists()) {    //父文件夹不存在时，创建
            file.getParentFile().mkdirs();
        }

        PrintStream out = null;

        try {
            out = new PrintStream(new FileOutputStream(file));
            out.print(allData.toString());
            Toast.makeText(this, "PrintStream", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

//        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//
//        File file2 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "json.txt");  //定义File类对象
//
//        InputStream in = null;
//        try {
//            in = new FileInputStream(file2);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        StringBuffer sb = new StringBuffer();
//        byte[] b = new byte[8];
//        try {
//            while (in.read(b) > 0) {
//                sb.append(new String(b));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    private void xml() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {   //判断sd卡是否存在
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "test.xml");  //定义File类对象
        if (!file.exists()) {
            return;
        }

        try {
            InputStream in = new FileInputStream(file);
            MyXMLPullUtil util = new MyXMLPullUtil(in);
            List<LinkMan> all = util.getAllLinkMan();
            et_xml_name.setText(all.get(0).getName());
            et_xml_email.setText(all.get(0).getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sax() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {   //判断sd卡是否存在
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "test.xml");  //定义File类对象
        if (!file.exists()) {
            return;
        }

        /** 1.建立SAX解析工厂 */
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        MySAX sax = new MySAX();
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        try {
            parser.parse(file, sax);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LinkMan> all = sax.getAll();
        et_sax_name.setText(all.get(0).getName());
        et_sax_email.setText(all.get(0).getEmail());

    }

    /**
     * 从sd卡的xml文件中读取
     */
    private void read() {
        /** 判断sd卡是否存在 */
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "test.xml");
        /** 判断父文件夹是否存在，不存在则创建 */
        if (!file.exists()) {
            return;
        }

        /** 建立DocumentBuilderFactory,以便取得DocumentBuilder */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        /** 通过DocumentBuilderFactory取得DocumentBuilder */
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        /** 定义Document接口对象，通过DocumentBuilder类进行DOM树的转换操作 */
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NodeList nl = doc.getElementsByTagName("linkman");
        for (int x = 0; x < nl.getLength(); x++) {
            Element e = (Element) nl.item(x);
            et_read_name.setText(e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            et_read_email.setText(e.getElementsByTagName("email").item(0).getFirstChild().getNodeValue());
        }
    }

    /**
     * 生成xml文件保存在sd卡
     */
    private void save() {
        /** 判断sd卡是否存在 */
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "test.xml");
        /** 判断父文件夹是否存在，不存在则创建 */
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        /** 1.建立DocumentBuilderFactory,以便取得DocumentBuilder */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        /** 2.通过DocumentBuilderFactory取得DocumentBuilder */
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        /** 3.定义Document接口对象，通过DocumentBuilder类进行DOM树的转换操作 */
        Document doc = null;
        doc = builder.newDocument();

        /** 4.建立各个操作节点 */
        org.w3c.dom.Element addresslist = doc.createElement("addresslist");
        org.w3c.dom.Element linkman = doc.createElement("linkman");
        org.w3c.dom.Element name = doc.createElement("name");
        org.w3c.dom.Element email = doc.createElement("email");

        /** 5.设置节点的文本内容，即为每一个节点添加文本节点 */
        name.appendChild(doc.createTextNode(ResOperateTestActivity.this.et_name.getText().toString()));
        email.appendChild(doc.createTextNode(ResOperateTestActivity.this.et_email.getText().toString()));
        /** 设置节点关系 */
        linkman.appendChild(name);
        linkman.appendChild(email);
        addresslist.appendChild(linkman);
        doc.appendChild(addresslist);
        /** 7.输出文件到文档中 */
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        try {
            t.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取raw中的文件
     */
    private void readRaw() {
        Resources res = getResources();
        InputStream in = res.openRawResource(R.raw.test);
        Scanner scanner = new Scanner(in);
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext()) {
            sb.append(scanner.next()).append("\n");
        }
        scanner.close();

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv_content.setText(sb.toString());
    }
}
