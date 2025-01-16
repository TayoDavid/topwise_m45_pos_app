package com.example.topwisepos.emv;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.topwise.cloudpos.aidl.emv.level2.EmvKernelConfig;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.example.topwisepos.param.SysParam;
import com.topwise.manager.emv.entity.EmvErrorCode;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.impl.TopTool;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 创建日期：2021/4/14 on 9:30
 * 描述: 保存 ic data
 * 作者:  wangweicheng
 */
public class EmvResultUtlis {
    private static final String TAG = EmvResultUtlis.class.getSimpleName();
    /**
     *    * 交易结果
     *      * 批准: 0x01
     *      * 拒绝: 0x02
     *      * 终止: 0x03
     */
    /**
     * 联机批准
     */
    public final static byte ONLINE_APPROVED = 0x01;
    /**
     * 交易拒绝
     */
    public final static byte ONLINE_DENIED = 0x02;
    /**
     * 脱机批准
     */
    public final static byte OFFLINE_APPROVED = 0x03;
    private IConvert convert =TopTool.getInstance().getConvert();


    public static String getDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    //set kernal data
    /**
     * set init EmvTerminalInfo
     * @return
     */
      public static EmvTerminalInfo emvTerminalInfo;
      public static EmvKernelConfig emvKernelConfig;

    public static EmvTerminalInfo setEmvTerminalInfo() {
        if (emvTerminalInfo != null) {
            return emvTerminalInfo;
        }

        emvTerminalInfo = new EmvTerminalInfo();
        IConvert convert = TopTool.getInstance().getConvert();
        String transCurCode = SysParam.COUNTRY_CODE;
        String terCountryCode = SysParam.COUNTRY_CODE;
        String terCapabilities = terCapabilities = "E0F8C8";

        emvTerminalInfo.setUnTerminalFloorLimit(20000);
        emvTerminalInfo.setUnThresholdValue(10000);
        String terminalId = SysParam.CUST_TID;
        emvTerminalInfo.setAucTerminalID(terminalId);
        emvTerminalInfo.setAucIFDSerialNumber("12345678");
        emvTerminalInfo.setAucTerminalCountryCode(convert.strToBcd(terCountryCode, IConvert.EPaddingPosition.PADDING_RIGHT));
        String mercherId = SysParam.Cust_MID;
        emvTerminalInfo.setAucMerchantID(mercherId);
        emvTerminalInfo.setAucMerchantCategoryCode(new byte[] {0x00, 0x01});
        emvTerminalInfo.setAucMerchantNameLocation(new byte[] {0x30, 0x30, 0x30, 0x31}); //"0001"
        emvTerminalInfo.setAucTransCurrencyCode(convert.strToBcd(transCurCode, IConvert.EPaddingPosition.PADDING_RIGHT));
        emvTerminalInfo.setUcTransCurrencyExp((byte) 2);
        emvTerminalInfo.setAucTransRefCurrencyCode(convert.strToBcd(transCurCode, IConvert.EPaddingPosition.PADDING_RIGHT));
        emvTerminalInfo.setUcTransRefCurrencyExp((byte) 2);


        emvTerminalInfo.setAucTerminalAcquireID("123456");
        emvTerminalInfo.setAucAppVersion(new byte[] {0x00, 0x030});
        emvTerminalInfo.setAucDefaultDDOL(new byte[] {(byte)0x9F, 0x37, 0x04});
        emvTerminalInfo.setAucDefaultTDOL(new byte[] {(byte)0x9F, 0x37, 0x04});

        emvTerminalInfo.setAucTACDenial(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00});
        emvTerminalInfo.setAucTACOnline(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00});
        emvTerminalInfo.setAucTACDefault(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00});

        emvTerminalInfo.setUcTerminalType((byte)0x22);  //22 印度客户也是配置这个值
        //终端是否支持脱机PIN 可以通过 setAucTerminalCapabilities 配置
        //emvTerminalInfo.setAucTerminalCapabilities(new byte[] {(byte)0xE0, (byte)0xF8, (byte)0xC8});
        emvTerminalInfo.setAucTerminalCapabilities(convert.strToBcd(terCapabilities, IConvert.EPaddingPosition.PADDING_RIGHT));  //E0F8C8
//        emvTerminalInfo.setAucAddtionalTerminalCapabilities(new byte[] {(byte)0xFF, (byte)0x00, (byte)0xF0, (byte)0xA0, 0x01});
        //paynext FF 80 F0 00 01
        emvTerminalInfo.setAucAddtionalTerminalCapabilities(new byte[] {(byte)0xFF, (byte)0x80, (byte)0xF0, (byte)0x50, 0x01});

        emvTerminalInfo.setUcTargetPercentage((byte) 20);
        emvTerminalInfo.setUcMaxTargetPercentage((byte) 50);
        emvTerminalInfo.setUcAccountType((byte) 0);
        emvTerminalInfo.setUcIssuerCodeTableIndex((byte) 0);
        return emvTerminalInfo;
    }

    /**
     * set init EmvKernelConfig
     * @return
     */
    public static EmvKernelConfig setEmvKernelConfig() {
        if (emvKernelConfig != null) {
            return emvKernelConfig;
        }
        emvKernelConfig = new EmvKernelConfig();
        emvKernelConfig.setbPSE((byte) 1);
        emvKernelConfig.setbCardHolderConfirm((byte) 1);
        emvKernelConfig.setbPreferredDisplayOrder((byte) 0);
        emvKernelConfig.setbLanguateSelect((byte) 1);
        emvKernelConfig.setbDefaultDDOL((byte) 1);
        emvKernelConfig.setbRevocationOfIssuerPublicKey((byte) 1);

        emvKernelConfig.setbBypassPINEntry((byte) 1);
        emvKernelConfig.setbSubBypassPINEntry((byte) 1);
        emvKernelConfig.setbGetdataForPINTryCounter((byte) 1);
        emvKernelConfig.setbFloorLimitCheck((byte) 1);
        emvKernelConfig.setbRandomTransSelection((byte) 1);
        emvKernelConfig.setbVelocityCheck((byte) 1);
        emvKernelConfig.setbTransactionLog((byte) 1);
        emvKernelConfig.setbExceptionFile((byte) 1);
        emvKernelConfig.setbTerminalActionCode((byte) 1);
        emvKernelConfig.setbDefaultActionCodeMethod((byte) EmvErrorCode.EMV_DEFAULT_ACTION_CODE_AFTER_GAC1);
        emvKernelConfig.setbTACIACDefaultSkipedWhenUnableToGoOnline((byte) 0);
        emvKernelConfig.setbCDAFailureDetectedPriorTerminalActionAnalysis((byte) 1);
        emvKernelConfig.setbCDAMethod((byte) EmvErrorCode.EMV_CDA_MODE1);
        emvKernelConfig.setbForcedOnline((byte) 0);
        emvKernelConfig.setbForcedAcceptance((byte) 0);
        emvKernelConfig.setbAdvices((byte) 0);
        emvKernelConfig.setbIssuerReferral((byte) 1);
        emvKernelConfig.setbBatchDataCapture((byte) 0);
        emvKernelConfig.setbOnlineDataCapture((byte) 1);
        emvKernelConfig.setbDefaultTDOL((byte) 1);
        emvKernelConfig.setbTerminalSupportAccountTypeSelection((byte) 1);
//        if ((boolean)SysParam.get(SysParam.PCI_MODE, false)) {
//            emvKernelConfig.setbPCIPINEntry((byte) 1);
//        }
        return emvKernelConfig;
    }
}
