package com.example.topwisepos.param;

import android.text.TextUtils;

import com.topwise.cloudpos.aidl.emv.level2.Combination;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.manager.AppLog;
import com.topwise.manager.emv.entity.EmvAidParam;
import com.topwise.manager.emv.entity.EmvErrorCode;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.impl.TopTool;

import java.util.ArrayList;
import java.util.List;

public class AppCombinationHelper {
    private static final String TAG =  AppCombinationHelper.class.getSimpleName();
    private static List<Combination> AppCombinationList;

    public static AppCombinationHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public AppCombinationHelper() {
    }

    //静态内部类
    private static class SingletonHolder {
        private static final AppCombinationHelper sInstance = new AppCombinationHelper();
    }

    private boolean loadAidtoCombination() {
        List<EmvAidParam> uAids = AidParam.getEmvAidParamList();
        AppCombinationList = new ArrayList<>();
        IConvert convert = TopTool.getInstance().getConvert();
        for (EmvAidParam emvAid: uAids) {
            Combination combination = new Combination();
            combination.setUcAidLen(emvAid.getAid().length()/2);
            combination.setAucAID(BytesUtil.hexString2Bytes(emvAid.getAid()));
            combination.setUcPartMatch(1);
            String kernelId = emvAid.getAucKernType();
            //Kernel Identifier (Kernel ID) 81 06 43,  Russia Terminal Country Code:0643 defined by ISO 4217.
            if (!TextUtils.isEmpty(kernelId)) {
                byte[] buf = BytesUtil.hexString2Bytes(kernelId);
                combination.setUcKernIDLen(buf.length);
                combination.setAucKernelID(buf);
            } else {
                combination.setUcKernIDLen(1);
                combination.setAucKernelID(new byte[]{0x00});
            }

            //Byte 1
            //bit 6: 1 = EMV mode supported
            //bit 5: 1 = EMV contact chip supported
            //bit 3: 1 = Online PIN supported
            //bit 2: 1 = Signature supported
            //Byte 3
            //bit 8: 1 = Issuer Update Processing supported
            //bit 7: 1 = Consumer Device CVM supported
            byte[] TTQ = new byte[]{0x36, 0x00, (byte) 0x40, (byte) 0x80}; ;
            combination.setAucReaderTTQ(TTQ);
//                if (emvAid.getFloorlimitCheck() == 1) {
            if (!TextUtils.isEmpty(emvAid.getAucFloorLimit())) {
                combination.setUcTermFLmtFlg(EmvErrorCode.CLSS_TAG_EXIST_WITHVAL);
                //十六进制转 long emvAid.getAucFloorLimit()

                long temp = convert.strToLong(emvAid.getAucFloorLimit(), IConvert.EPaddingPosition.PADDING_RIGHT);
                AppLog.d(TAG, "init AidParams FloorlimitCheck: " + temp);
                combination.setUlTermFLmt(temp);
            } else {
                combination.setUcTermFLmtFlg(EmvErrorCode.CLSS_TAG_NOT_EXIST);
            }

            //if (aid.isRdCVMLimitFlg()) {
            if (!TextUtils.isEmpty(emvAid.getAucRdCVMLmt())) {
                combination.setUcRdCVMLmtFlg(EmvErrorCode.CLSS_TAG_EXIST_WITHVAL);
                combination.setAucRdCVMLmt(convert.strToBcd(emvAid.getAucRdCVMLmt(), IConvert.EPaddingPosition.PADDING_RIGHT));
                AppLog.d(TAG, "initData aid.getRdCVMLimit(): " + emvAid.getAucRdCVMLmt());
            } else {
                combination.setUcRdCVMLmtFlg(EmvErrorCode.CLSS_TAG_NOT_EXIST);
            }
            AppLog.d(TAG, "init AidParams RdClssTxnLmtFlg: " + emvAid.getAucRdClssTxnLmt());
            //if (aid.isRdClssTxnLimitFlg()) {
            if (!TextUtils.isEmpty(emvAid.getAucRdClssTxnLmt())) {
                combination.setUcRdClssTxnLmtFlg(EmvErrorCode.CLSS_TAG_EXIST_WITHVAL);
                if (emvAid.getAid().startsWith("A000000004" )) {
                    long RdClssTxnLmt = Long.parseLong(emvAid.getAucRdClssTxnLmt());
                    //For Mastercard - Please set to only Greater than amount will be rejected
                    String temp = String.format("%012d",(RdClssTxnLmt + 1));
                    combination.setAucRdClssTxnLmt(BytesUtil.hexString2Bytes(temp));
                    AppLog.d(TAG, "initData Mastercard RdClssTxnLimit(): " + temp);
                } else {
                    combination.setAucRdClssTxnLmt(convert.strToBcd(emvAid.getAucRdClssTxnLmt(), IConvert.EPaddingPosition.PADDING_RIGHT));
                    AppLog.d(TAG, "initData aid.getRdClssTxnLimit(): " + emvAid.getAucRdClssTxnLmt());
                }
            } else {
                combination.setUcRdClssTxnLmtFlg(EmvErrorCode.CLSS_TAG_NOT_EXIST);
            }

            AppLog.d(TAG, "init AidParams RdClssFLmtFlg: " + emvAid.getAucRdClssFLmt());
            //if (aid.isRdClssFloorLimitFlg()) {
            if (!TextUtils.isEmpty(emvAid.getAucRdClssFLmt())) {
                combination.setUcRdClssFLmtFlg(EmvErrorCode.CLSS_TAG_EXIST_WITHVAL);
                combination.setAucRdClssFLmt(convert.strToBcd(emvAid.getAucRdClssFLmt(), IConvert.EPaddingPosition.PADDING_RIGHT));
                AppLog.d(TAG, "initData aid.getRdClssFloorLimit(): " + emvAid.getAucRdClssFLmt());
            } else {
                combination.setUcRdClssFLmtFlg(EmvErrorCode.CLSS_TAG_NOT_EXIST);
            }

            combination.setUcZeroAmtNoAllowed(0);
            combination.setUcStatusCheckFlg(0);
            combination.setUcCrypto17Flg(1);
            combination.setUcExSelectSuppFlg(0);

            AppCombinationList.add(combination);
        }
        return true;
    }

    /**
     * AppCombinationList is static
     * @return
     */
    public synchronized List<Combination> getAppCombinationList() {
        if (AppCombinationList == null || AppCombinationList.isEmpty()) {
            loadAidtoCombination();
        }
        return AppCombinationList;
    }
}
