package com.example.topwisepos.entity;

import com.topwise.manager.emv.entity.EinputType;

/**
 * Creation date：2021/6/23 on 16:29
 * Describe:
 * Author:wangweicheng
 *
 *
 */
public class TransData {
    private String orderNo; // 订单号	使用交易流水号接口获取
    private String orderNodis; // pos订单号 备注信息
    private String amount; // 交易金额  //单位是元 a
    private String cardAmount; // card Amount卡交易金额
    private String cashAmount; // cash Amount现金交易金额
    private String tipAmount; // tip Amount小费金额
    private String balance; // balance 余额
    private String balanceFlag; // 余额标识C/D

    //emv
    private byte emvResult; // emv ResultEMV交易的执行状态
    private String interOrgCode; // 国际组织代码

    private long transNo; // pos流水号
    private long origTransNo; // 原pos流水号
    private long batchNo; // 批次号
    private long origBatchNo; // 原批次号
    private int transType; // 交易类型
    private String origTransType; // 原交易类型
    private String merchID;

    private boolean isUpload; // 是否已批上送
    private String transState; // 交易状态
    private String oper;
    private String track1; // 磁道一信息
    private String track2; // 磁道二数据
    private String track3; // 磁道三数据
    private boolean isEncTrack; // 磁道是否加密
    private String reason; // 冲正原因
    private String reserved; // 63域附加域
    private String datetime; // 交易时间
    private String time; // 交易时间
    private String date; // 交易日期
    private String termID;
    private String pan; // 主账号
    private String expDate; // 卡有效期
    private String cardSerialNo; // 23 域，卡片序列号
    private EinputType enterMode; // 输入模式
    private boolean hasPin; // 是否有输密码
    private String sendIccData; // IC卡信息,55域
    private String scriptTag; // IC卡信息tag 9F5B
    private String dupIccData; // IC卡冲正信息,55域
    private boolean isOnlineTrans; // 是否为联机交易
    private String origDate; // 原交易日期
    private String origTime; // 原交易日期
    private String isserCode; // 发卡行标识码
    private String acqCode; // 收单机构标识码
    private boolean isSupportBypass;
    private String Random;

    private String issuerResp; // 发卡方保留域
    private String centerResp; // 中国银联保留域
    private String recvBankResp;// 受理机构保留域

    //    private String TVR;
//    private String TSI;
    private String ICPositiveData;
    /**
     * 响应码
     */
    private String responseCode;
    /**
     * 相应码对应的错误信息
     */
    private String responseMsg;
    private String settleDate; // 清算日期
    private String acqCenterCode; // 受理方标识码,pos中心号(返回包时用)
    private String refNo; // 系统参考号
    private String origRefNo; // 原系统参考号
    private String authCode; // 授权码
    private String origAuthCode; // 原授权码
    private String tc; // IC卡交易证书(TC值)tag9f26,(BIN)
    private String arqc; // 授权请求密文(ARQC)
    private String arpc; // 授权响应密文(ARPC)
    private String tvr; // 终端验证结果(TVR)值tag95
    private String aid; // 应用标识符AID
    private String emvAppLabel; // 应用标签
    private String emvAppName; // 应用首选名称
    private String tsi; // 交易状态信息(TSI)tag9B
    private String atc; // 应用交易计数器(ATC)值tag9f36
    private String origProcCode; //原消息码
    private String qrCode; //二维码数据
    private String origQrCode; //原二维码数据
    private String qrVoucher;
    private String origQrVoucher;
    private String pinKsn; // pinKsn
    private String dataKsn; // dataKsn
    private String field52; //打印提示
    private String field58; //打印数据
    private String field22; //22域
    private String procCode; //消息码
    private String cardHolderName; //持卡人姓名



    private String pinblock;
    //card type
//    public static final byte KERNTYPE_MC = 0x02;
//    public static final byte KERNTYPE_VISA = 0x03;
//    public static final byte KERNTYPE_AMEX = 0x04;
//    public static final byte KERNTYPE_JCB = 0x05;
//    public static final byte KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
//    public static final byte KERNTYPE_DPAS = 0x06;//Discover DPAS
//    public static final byte KERNTYPE_QPBOC = 0x07;
//    public static final byte KERNTYPE_RUPAY = 0x0D;
//    public static final byte  KERNTYPE_PURE = 0x12; //add wwc

    private int kernelType; // 0-emv 2- MC 3 -VISA 4 AMEX 5-JCB 6-_ZIP 7-QPBOC  13 -RUPAY 18 -PURE

    private boolean needScript;
    private String tag71;
    private String tag72;
    private String tag8A;
    private String tag91;

    private String recvIccData;

    public String getRecvIccData() {
        return recvIccData;
    }

    public void setRecvIccData(String recvIccData) {
        this.recvIccData = recvIccData;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNodis() {
        return orderNodis;
    }

    public void setOrderNodis(String orderNodis) {
        this.orderNodis = orderNodis;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(String balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public byte getEmvResult() {
        return emvResult;
    }

    public void setEmvResult(byte emvResult) {
        this.emvResult = emvResult;
    }

    public String getInterOrgCode() {
        return interOrgCode;
    }

    public void setInterOrgCode(String interOrgCode) {
        this.interOrgCode = interOrgCode;
    }

    public long getTransNo() {
        return transNo;
    }

    public void setTransNo(long transNo) {
        this.transNo = transNo;
    }

    public long getOrigTransNo() {
        return origTransNo;
    }

    public void setOrigTransNo(long origTransNo) {
        this.origTransNo = origTransNo;
    }

    public long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(long batchNo) {
        this.batchNo = batchNo;
    }

    public long getOrigBatchNo() {
        return origBatchNo;
    }

    public void setOrigBatchNo(long origBatchNo) {
        this.origBatchNo = origBatchNo;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getOrigTransType() {
        return origTransType;
    }

    public void setOrigTransType(String origTransType) {
        this.origTransType = origTransType;
    }

    public String getMerchID() {
        return merchID;
    }

    public void setMerchID(String merchID) {
        this.merchID = merchID;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getTrack1() {
        return track1;
    }

    public void setTrack1(String track1) {
        this.track1 = track1;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getTrack3() {
        return track3;
    }

    public void setTrack3(String track3) {
        this.track3 = track3;
    }

    public boolean isEncTrack() {
        return isEncTrack;
    }

    public void setEncTrack(boolean encTrack) {
        isEncTrack = encTrack;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTermID() {
        return termID;
    }

    public void setTermID(String termID) {
        this.termID = termID;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCardSerialNo() {
        return cardSerialNo;
    }

    public void setCardSerialNo(String cardSerialNo) {
        this.cardSerialNo = cardSerialNo;
    }

    public EinputType getEnterMode() {
        return enterMode;
    }

    public void setEnterMode(EinputType enterMode) {
        this.enterMode = enterMode;
    }

    public boolean isHasPin() {
        return hasPin;
    }

    public void setHasPin(boolean hasPin) {
        this.hasPin = hasPin;
    }

    public String getSendIccData() {
        return sendIccData;
    }

    public void setSendIccData(String sendIccData) {
        this.sendIccData = sendIccData;
    }

    public String getScriptTag() {
        return scriptTag;
    }

    public void setScriptTag(String scriptTag) {
        this.scriptTag = scriptTag;
    }

    public String getDupIccData() {
        return dupIccData;
    }

    public void setDupIccData(String dupIccData) {
        this.dupIccData = dupIccData;
    }

    public boolean isOnlineTrans() {
        return isOnlineTrans;
    }

    public void setOnlineTrans(boolean onlineTrans) {
        isOnlineTrans = onlineTrans;
    }

    public String getOrigDate() {
        return origDate;
    }

    public void setOrigDate(String origDate) {
        this.origDate = origDate;
    }

    public String getOrigTime() {
        return origTime;
    }

    public void setOrigTime(String origTime) {
        this.origTime = origTime;
    }

    public String getIsserCode() {
        return isserCode;
    }

    public void setIsserCode(String isserCode) {
        this.isserCode = isserCode;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }

    public boolean isSupportBypass() {
        return isSupportBypass;
    }

    public void setSupportBypass(boolean supportBypass) {
        isSupportBypass = supportBypass;
    }

    public String getIssuerResp() {
        return issuerResp;
    }

    public void setIssuerResp(String issuerResp) {
        this.issuerResp = issuerResp;
    }

    public String getCenterResp() {
        return centerResp;
    }

    public void setCenterResp(String centerResp) {
        this.centerResp = centerResp;
    }

    public String getRecvBankResp() {
        return recvBankResp;
    }

    public void setRecvBankResp(String recvBankResp) {
        this.recvBankResp = recvBankResp;
    }

    public String getICPositiveData() {
        return ICPositiveData;
    }

    public void setICPositiveData(String ICPositiveData) {
        this.ICPositiveData = ICPositiveData;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getAcqCenterCode() {
        return acqCenterCode;
    }

    public void setAcqCenterCode(String acqCenterCode) {
        this.acqCenterCode = acqCenterCode;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getOrigRefNo() {
        return origRefNo;
    }

    public void setOrigRefNo(String origRefNo) {
        this.origRefNo = origRefNo;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOrigAuthCode() {
        return origAuthCode;
    }

    public void setOrigAuthCode(String origAuthCode) {
        this.origAuthCode = origAuthCode;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getArqc() {
        return arqc;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }

    public String getArpc() {
        return arpc;
    }

    public void setArpc(String arpc) {
        this.arpc = arpc;
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getEmvAppLabel() {
        return emvAppLabel;
    }

    public void setEmvAppLabel(String emvAppLabel) {
        this.emvAppLabel = emvAppLabel;
    }

    public String getEmvAppName() {
        return emvAppName;
    }

    public void setEmvAppName(String emvAppName) {
        this.emvAppName = emvAppName;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public String getOrigProcCode() {
        return origProcCode;
    }

    public void setOrigProcCode(String origProcCode) {
        this.origProcCode = origProcCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getOrigQrCode() {
        return origQrCode;
    }

    public void setOrigQrCode(String origQrCode) {
        this.origQrCode = origQrCode;
    }

    public String getQrVoucher() {
        return qrVoucher;
    }

    public void setQrVoucher(String qrVoucher) {
        this.qrVoucher = qrVoucher;
    }

    public String getOrigQrVoucher() {
        return origQrVoucher;
    }

    public void setOrigQrVoucher(String origQrVoucher) {
        this.origQrVoucher = origQrVoucher;
    }

    public String getPinKsn() {
        return pinKsn;
    }

    public void setPinKsn(String pinKsn) {
        this.pinKsn = pinKsn;
    }

    public String getDataKsn() {
        return dataKsn;
    }

    public void setDataKsn(String dataKsn) {
        this.dataKsn = dataKsn;
    }

    public String getField52() {
        return field52;
    }

    public void setField52(String field52) {
        this.field52 = field52;
    }

    public String getField58() {
        return field58;
    }

    public void setField58(String field58) {
        this.field58 = field58;
    }

    public String getField22() {
        return field22;
    }

    public void setField22(String field22) {
        this.field22 = field22;
    }

    public String getProcCode() {
        return procCode;
    }

    public void setProcCode(String procCode) {
        this.procCode = procCode;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public int getKernelType() {
        return kernelType;
    }

    public void setKernelType(int kernelType) {
        this.kernelType = kernelType;
    }

    public boolean isNeedScript() {
        return needScript;
    }

    public void setNeedScript(boolean needScript) {
        this.needScript = needScript;
    }

    public String getTag71() {
        return tag71;
    }

    public void setTag71(String tag71) {
        this.tag71 = tag71;
    }

    public String getTag72() {
        return tag72;
    }

    public void setTag72(String tag72) {
        this.tag72 = tag72;
    }

    public String getTag8A() {
        return tag8A;
    }

    public void setTag8A(String tag8A) {
        this.tag8A = tag8A;
    }

    public String getTag91() {
        return tag91;
    }

    public void setTag91(String tag91) {
        this.tag91 = tag91;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }
    public String getPinblock() {
        return pinblock;
    }

    public void setPinblock(String pinblock) {
        this.pinblock = pinblock;
    }
}
