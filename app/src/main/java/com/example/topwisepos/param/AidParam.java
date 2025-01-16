package com.example.topwisepos.param;

import android.content.Context;
import android.text.TextUtils;

import com.topwise.cloudpos.struct.TlvList;
import com.topwise.manager.AppLog;
import com.topwise.manager.emv.entity.EmvAidParam;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 创建日期：2021/4/21 on 10:03
 * 描述:
 * 作者:wangweicheng
 */
public class AidParam extends LoadParam<EmvAidParam> {
    private static final String AIDNAME = "xml/aid.xml";
    public static List<EmvAidParam> aidList = new ArrayList<>();
    public static HashMap<String, TlvList> tlvListHashMap = new HashMap<>();

    @Override
    public boolean DelectAll() {
        aidList.clear();
        EmvAidParam.clearTlvMap();
        return true;
    }

    public static List<EmvAidParam> getEmvAidParamList() {
        return aidList;
    }

    /**
     * 保存全部
     */
    @Override
    public void saveAll() {
        if (list == null || list.size() == 0)
            return;

        DelectAll();

        for (String aid : list) {
            EmvAidParam aidParam = saveAid(aid);
            aidList.add(aidParam);
            EmvAidParam.putTlvMap(aidParam.getAid(), aidParam);
        }
    }

    /**
     * 保存单条
     * @param inData
     * @return
     */
    @Override
    public boolean save(String inData) {
        if (TextUtils.isEmpty(inData))
            return false;
        saveAid(inData);
        return true;
    }

    /**
     * 从xml文件解析List<String>
     * @param context
     * @return
     */
    @Override
    public List<String> init(Context context) {
        long l = System.currentTimeMillis();
        try {
            InputStream open = context.getResources().getAssets().open(AIDNAME);
            DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuidler = null;
            Document doc = null;
            docBuidler = docFact.newDocumentBuilder();
            doc = docBuidler.parse(open);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getElementsByTagName("aid");

            list = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String aidparam = element.getAttribute("aidparam");
                list.add(aidparam);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public static EmvAidParam getAidFromList(String aid) {
        if (aidList== null || aidList.isEmpty()) {
            return null;
        }
        if (TextUtils.isEmpty(aid)) {
            return null;
        }

        for (EmvAidParam aidParam:aidList) {
            if(aid.equals(aidParam.getAid())) {
                return aidParam;
            }
        }
        return null;
    }

    /**
     * 支持部分匹配，长度长，优先级高
     * @param aid
     * @return
     */
    public static EmvAidParam getCurrentAidParam(String aid) {
        AppLog.e(TAG,"getCurrentAidParam  aid: " + aid);

        EmvAidParam emvAidParam = null;
        for (int i = aid.length(); i >= 10; i= i-2) {
            AppLog.e(TAG,"getCurrentAidParam  i: " + i);
            String subAid = aid.substring(0, i);
            AppLog.e(TAG,"getCurrentAidParam  subAid: " + subAid);
            emvAidParam = getAidFromList(subAid);
            if (emvAidParam != null) {
                break;
            }
        }
        if (emvAidParam == null) {
           AppLog.e(TAG,"getCurrentAidParam  Unable to match AID parameters ====" + aid);
            return null;
        }
        AppLog.e(TAG,"getCurrentAidParam  " + emvAidParam.toString());

        return emvAidParam;
    }
}
