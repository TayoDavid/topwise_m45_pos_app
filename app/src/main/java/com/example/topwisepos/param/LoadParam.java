package com.example.topwisepos.param;

import android.content.Context;
import android.text.TextUtils;

import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.manager.AppLog;
import com.topwise.manager.emv.entity.EmvAidParam;
import com.topwise.manager.emv.entity.EmvCapkParam;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.api.packer.ITlv;
import com.topwise.toptool.api.packer.TlvException;
import com.topwise.toptool.impl.TopTool;

import java.util.List;

/**
 * 创建日期：2021/4/21 on 10:02
 * 描述: 逻辑 可以从xml 读取文件，到数据库，也可以单独加载到数据库
 * 作者:wangweicheng
 */
public abstract class LoadParam<T> {
    protected static final String TAG = LoadParam.class.getSimpleName();

    protected List<String> list;


    public abstract boolean DelectAll();

    /**
     * 保存到数据库
     * @return
     */
    public abstract void saveAll();

    /**
     * 单独保存记录
     * @param inData
     * @return
     */
    public abstract boolean save(String inData);

    /**
     * 从xml读取list
     * @param context
     * @return
     */
    public abstract List<String> init(Context context);

    /**
     * 解析保存
     * @param aid
     * @return
     */
    protected EmvAidParam saveAid(String aid) {
        ITlv tlv = TopTool.getInstance().getPacker().getTlv();
        IConvert convert =  TopTool.getInstance().getConvert();
        ITlv.ITlvDataObjList aidTlvList;
        ITlv.ITlvDataObj tlvDataObj;
        EmvAidParam aidParam;
        byte[] value = null;
        byte[] bytes = convert.strToBcd(aid, IConvert.EPaddingPosition.PADDING_LEFT);
        try {
            aidTlvList = tlv.unpack(bytes);
            aidParam = new EmvAidParam();
            // 9f06 AID
            tlvDataObj = aidTlvList.getByTag(0x9f06);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    aidParam.setAid(convert.bcdToStr(value));
                }
            }
            // DF01
            tlvDataObj = aidTlvList.getByTag(0xDF01);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    aidParam.setbPartMatch(true);
                }
            }
            // 9F08
            tlvDataObj = aidTlvList.getByTag(0x9f08);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    aidParam.setAucAPPVer(convert.bcdToStr(value));
                    aidParam.setbAPPVerFlg(true);
                }
            }

            // DF11
            tlvDataObj = aidTlvList.getByTag(0xDF11);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    aidParam.setAucTACDefault(convert.bcdToStr(value));
                    aidParam.setbTACDefaultFlg(true);
                }
            }

            // DF12
            tlvDataObj = aidTlvList.getByTag(0xDF12);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    aidParam.setAucTACOnline(convert.bcdToStr(value));
                    aidParam.setbTACOnlineFlg(true);
                }
            }

                // DF13
                tlvDataObj = aidTlvList.getByTag(0xDF13);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucTACDenail(convert.bcdToStr(value));
                        aidParam.setbTACDenailFlg(true);
                    }
                }

                // 9F1B
                tlvDataObj = aidTlvList.getByTag(0x9F1B);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucFloorLimit(convert.bcdToStr(value));
                        aidParam.setbFloorLimitFlg(true);
                    }
                }

                // DF15 byte 4
                tlvDataObj = aidTlvList.getByTag(0xDF15);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucThreshold(convert.bcdToStr(value));
                        aidParam.setbThresholdFlg(true);
                    }
                }

                // DF16
                tlvDataObj = aidTlvList.getByTag(0xDF16);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setUcMaxTP(value[0]);
                        aidParam.setbMaxTPFlg(true);
                    }
                }

                // DF17
                tlvDataObj = aidTlvList.getByTag(0xDF17);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setUcTP(value[0]);
                        aidParam.setbTPFlg(true);
                    }
                }

                // DF14
                tlvDataObj = aidTlvList.getByTag(0xDF14);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucTermDDOL(convert.bcdToStr(value));
                        aidParam.setbTermDDOLFlg(true);
                    }
                }

                // DF18
                tlvDataObj = aidTlvList.getByTag(0xDF18);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {

                    }
                }

                // 9F7B
                tlvDataObj = aidTlvList.getByTag(0x9F7B);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRdClssTxnLmtOnDevice(convert.bcdToStr(value));
                        aidParam.setbRdClssTxnLmtOnDeviceFlg(true);
                    }
                }

                // DF19
                tlvDataObj = aidTlvList.getByTag(0xDF19);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRdClssFLmt(convert.bcdToStr(value));
                        aidParam.setbRdClssFLmtFlg(true);
                    }
                }

                // DF20
                tlvDataObj = aidTlvList.getByTag(0xDF20);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRdClssTxnLmt(convert.bcdToStr(value));
                        aidParam.setbRdClssTxnLmtFlg(true);
                    }
                }

                // DF21
                tlvDataObj = aidTlvList.getByTag(0xDF21);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRdCVMLmt(convert.bcdToStr(value));
                        aidParam.setbRdCVMLmtFlg(true);
                    }
                }

                //9F1C s termId
                tlvDataObj = aidTlvList.getByTag(0x9F1C);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucTermID(convert.bcdToStr(value));
                        aidParam.setbTermIDFlg(true);
                    }
                }
                //5F2A s transCurrCode
                tlvDataObj = aidTlvList.getByTag(0x5F2A);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucCurrencyCode(convert.bcdToStr(value));
                        aidParam.setbCurrencyCodeFlg(true);
                    }
                }
                //5F36 i transCurrExp
                tlvDataObj = aidTlvList.getByTag(0xDF8101);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setUcCurrencyExp(value[0]);
                        aidParam.setbCurrencyExpFlg(true);
                    }
                }
                //9F3C s referCurrCode
                tlvDataObj = aidTlvList.getByTag(0x9F3C);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRefCurrencyCon(convert.bcdToStr(value));
                        aidParam.setbRefCurrencyCodeExt(true);
                    }
                }
                //9F3D byte referCurrExp
                tlvDataObj = aidTlvList.getByTag(0x9F3D);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setUcRefCurrencyExp(value[0]);
                        aidParam.setbRefCurrencyExpExt(true);
                    }
                }
                //DF8101 int referCurrCon
                tlvDataObj = aidTlvList.getByTag(0xDF8101);
                if (tlvDataObj != null) {
                    value = tlvDataObj.getValue();
                    if (value != null && value.length > 0) {
                        aidParam.setAucRefCurrencyCode(convert.bcdToStr(value));
                        aidParam.setbRefCurrencyConExt(true);
                    }
                }
                AppLog.e(TAG, "mUAidDaoUtils uAid  ==" + aidParam.toString());

                return aidParam;
            } catch(Exception e){
                e.printStackTrace();
                AppLog.e(TAG, "TlvException ==");
                return null;
            }
    }

    /**
     *
     * @param capk
     * @return
     */
    protected EmvCapkParam saveCapk(String capk) {
        try {
            ITlv tlv = TopTool.getInstance().getPacker().getTlv();
            IConvert convert = TopTool.getInstance().getConvert();
            ITlv.ITlvDataObjList capkTlvList;
            ITlv.ITlvDataObj tlvDataObj;
            EmvCapkParam capkParam;
            byte[] value = null;
            byte[] bytes = convert.strToBcd(capk, IConvert.EPaddingPosition.PADDING_LEFT);
            capkTlvList = tlv.unpack(bytes);
            capkParam = new EmvCapkParam();
            // 9f06 RID
            tlvDataObj = capkTlvList.getByTag(0x9f06);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setRID( convert.bcdToStr(value));
                }
            }
            // 9F2201
            tlvDataObj = capkTlvList.getByTag(0x9f22);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setKeyID(value[0]&0xFF);
                }
            }
            // DF02
            tlvDataObj = capkTlvList.getByTag(0xDF02);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setModul( convert.bcdToStr(value));
                }
            }
            // DF03
            tlvDataObj = capkTlvList.getByTag(0xDF03);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setCheckSum( convert.bcdToStr(value));
                }
            }
            // DF06
            tlvDataObj = capkTlvList.getByTag(0xDF06);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setHashInd(value[0]);
                }
            }
            // DF04
            tlvDataObj = capkTlvList.getByTag(0xDF04);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setExponent( convert.bcdToStr(value));
                }
            }
            // DF05
            tlvDataObj = capkTlvList.getByTag(0xDF05);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
    //                        String expDate = ;
    //                        if (value.length == 4) {
    //                            expDate =  convert.bcdToStr(value).substring(2, 8);
    //                        } else {
    //                            expDate = new String(value);
    //                            expDate = expDate.substring(2, 8);
    //                        }
                    capkParam.setExpDate(convert.bcdToStr(value));
                }
            }
            // DF07
            tlvDataObj = capkTlvList.getByTag(0xDF07);
            if (tlvDataObj != null) {
                value = tlvDataObj.getValue();
                if (value != null && value.length > 0) {
                    capkParam.setArithInd(value[0]);
                }
            }

            if (!TextUtils.isEmpty(capkParam.getRID()) && capkParam.getKeyID() >= 0){
                String sKeyId = BytesUtil.byte2HexString( (byte) capkParam.getKeyID());
                String ridindex =  capkParam.getRID() + sKeyId;
                capkParam.setRIDKeyID(ridindex);
            }
            return capkParam;
        } catch (TlvException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static byte[] getCAPKChecksum(EmvCapkParam capk) {
        byte[] Modulbytes = BytesUtil.hexString2Bytes(capk.getModul());
        byte[] Exponentbytes = BytesUtil.hexString2Bytes(capk.getExponent());

        byte[] data = new byte[5 + 1 + Modulbytes.length + Exponentbytes.length];
        System.arraycopy(BytesUtil.hexString2Bytes(capk.getRID()), 0, data, 0, 5);
        data[5] = (byte)capk.getKeyID();
        System.arraycopy(Modulbytes, 0, data, 6, Modulbytes.length);
        System.arraycopy(Exponentbytes, 0, data, 6 + Modulbytes.length, Exponentbytes.length);
        byte[] sha1 = null;//= EmvErrorCode.getSHA1(data, 0, data.length);
//        Log.d(TAG, "onSelectCapk getCAPKChecksum, sha1: " + BytesUtil.bytes2HexString(sha1));
        return sha1;
    }
}
