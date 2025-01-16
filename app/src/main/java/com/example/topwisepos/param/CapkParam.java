package com.example.topwisepos.param;

import android.content.Context;
import android.text.TextUtils;

import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.manager.AppLog;
import com.topwise.manager.emv.entity.EmvCapkParam;

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
 * 创建日期：2021/4/21 on 9:49
 * 描述:
 * 作者:wangweicheng
 */
public class CapkParam extends LoadParam<EmvCapkParam>{
    private static final String CAPKNAME = "xml/capk.xml";
    private static final String CAPKNAME_TEST = "xml/capk_test.xml";
    public  static HashMap<String, EmvCapkParam> capkMap = new HashMap();

    @Override
    public boolean DelectAll() {
        capkMap.clear();
        return true;
    }

    @Override
    public void saveAll() {
        if (list == null || list.size() == 0) {
            return;
        }
        DelectAll();
        for (String capk:list) {
            EmvCapkParam emvCapkParam = saveCapk(capk);
            capkMap.put(emvCapkParam.getRIDKeyID(), emvCapkParam);
        }
    }

    @Override
    public boolean save(String capk) {
        if (TextUtils.isEmpty(capk))
            return false;

        EmvCapkParam capkParam = saveCapk(capk);

        return true;
    }

    @Override
    public List<String> init(Context context) {
        long l = System.currentTimeMillis();
        try {
            String name = "";
            name = CAPKNAME;
            InputStream open = context.getResources().getAssets().open(name);
            DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuidler = null;
            Document doc = null;
            docBuidler = docFact.newDocumentBuilder();
            doc = docBuidler.parse(open);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getElementsByTagName("capk");
            list = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String aidparam = element.getAttribute("capkparam");
                list.add(aidparam);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     *
     * @param rid
     * @param bCapkIndex
     * @return
     */
    public static EmvCapk getEmvCapkParam(String rid, byte bCapkIndex) {
        String sKeyId = BytesUtil.byte2HexString(bCapkIndex);
        StringBuffer sRIDKEY_ID = new StringBuffer();
        sRIDKEY_ID.append(rid);
        sRIDKEY_ID.append(sKeyId);
        AppLog.e(TAG,"EmvCapkParam sRIDKEY_ID : " + sRIDKEY_ID.toString());
        EmvCapkParam capk = getAidFromList(rid, bCapkIndex);
        if (capk == null) {
            AppLog.e(TAG,"EmvCapkParam getEmvCapkParam no find " + rid + " bCapkIndex =" + BytesUtil.byte2HexString(bCapkIndex) + " CAPK" );
            return null;
        }
        AppLog.e(TAG,"EmvCapkParam getEmvCapkParam 0000000000001 " + capk.toString());

        EmvCapk emvCapk = new EmvCapk();
        emvCapk.setRID(BytesUtil.hexString2Bytes(capk.getRID()));
        emvCapk.setKeyID(capk.getKeyID());

        byte[] tempExpDate = new byte[3]; //YYMMDD
        byte[] bcdExpDate =  BytesUtil.hexString2Bytes(capk.getExpDate());
        if (4 == bcdExpDate.length) { //2009123
            System.arraycopy(bcdExpDate, 1, tempExpDate, 0, 3);
        } else if (8 == bcdExpDate.length) {
            byte[] bcdExpDatea =  BytesUtil.hexString2Bytes(new String(bcdExpDate));
            System.arraycopy(bcdExpDatea, 1, tempExpDate, 0, 3);
        } else {  // Default period of validity
            //301231
            tempExpDate[0] = 0x30;
            tempExpDate[1] = 0x12;
            tempExpDate[2] = 0x31;
        }

        AppLog.d(TAG, "EmvCapkParam  tempExpDate(): " + BytesUtil.bytes2HexString(tempExpDate));
        emvCapk.setExpDate(tempExpDate);
        emvCapk.setHashInd(capk.getHashInd());
        emvCapk.setArithInd(capk.getArithInd());
        emvCapk.setCheckSum(BytesUtil.hexString2Bytes(capk.getCheckSum()));

        byte[] orgData = BytesUtil.hexString2Bytes(capk.getModul());
        if (orgData != null) {
            emvCapk.setModul(orgData);
        }
        orgData = BytesUtil.hexString2Bytes(capk.getExponent());
        if (orgData != null) {
            emvCapk.setExponent(orgData);
        }
        return emvCapk;
    }

    public static EmvCapkParam getAidFromList(String capk, byte index) {
        if (capkMap== null || capkMap.isEmpty()) {
            return null;
        }
        if (TextUtils.isEmpty(capk)) {
            return null;
        }
        String sKeyId = capk+BytesUtil.byte2HexString(index);
        EmvCapkParam capkParam = capkMap.get(sKeyId);
        return capkParam;
    }
}


