package com.example.topwisepos.entity;

/**
 * Creation date：2021/8/30 on 16:43
 * Describe:
 * Author:wangweicheng
 */
public class TransResult {
    /**
     * 交易成功/ trans successful
     */
    public static final int SUCC = 0;
    /**
     * 超时/timeout
     */
    public static final int ERR_TIMEOUT = -1;
    /**
     * 连接超时/connect timeout
     */
    public static final int ERR_CONNECT = -2;
    /**
     * 发送失败/send fail
     */
    public static final int ERR_SEND = -3;
    /**
     * 接收失败/receive fail
     */
    public static final int ERR_RECV = -4;
    /**
     * 打包失败/pack fail
     */
    public static final int ERR_PACK = -5;
    /**
     * 解包失败/unpack fail
     */
    public static final int ERR_UNPACK = -6;
    /**
     * 非法包/bag pack
     */
    public static final int ERR_BAG = -7;
    /**
     * 解包mac错/mac fail
     */
    public static final int ERR_MAC = -8;
    /**
     * 处理码不一致/response code fail
     */
    public static final int ERR_PROC_CODE = -9;
    /**
     * 消息类型不一致/message code fail
     */
    public static final int ERR_MSG = -10;
    /**
     * 交易金额不符/trans amount fail
     */
    public static final int ERR_TRANS_AMT = -11;
    /**
     * 流水号不一致/trans number fail
     */
    public static final int ERR_TRACE_NO = -12;
    /**
     * 终端号不一致/Terminal no. fail
     */
    public static final int ERR_TERM_ID = -13;
    /**
     * 商户号不一致/merch fail
     */
    public static final int ERR_MERCH_ID = -14;
    /**
     * 无交易/no trans
     */
    public static final int ERR_NO_TRANS = -15;
    /**
     * 无原始交易/no original trans
     */
    public static final int ERR_NO_ORIG_TRANS = -16;
    /**
     * 此交易已撤销/had void
     */
    public static final int ERR_HAS_VOID = -17;
    /**
     * 此交易不可撤销/void no support
     */
    public static final int ERR_VOID_UNSUPPORT = -18;
    /**
     * 打开通讯口错误/open comm channel fail
     */
    public static final int ERR_COMM_CHANNEL = -19;
    /**
     * 失败/ fail
     */
    public static final int ERR_HOST_REJECT = -20;
    /**
     * 交易终止（终端不需要提示信息）/trans aborted
     */
    public static final int ERR_ABORTED = -21;
    /**
     * 预处理相关 终端未签到/no sign
     */
    public static final int ERR_NOT_LOGON = -22;
    /**
     * 预处理相关 交易笔数超限，立即结算/settle now
     */
    public static final int ERR_NEED_SETTLE_NOW = -23;
    /**
     * 预处理相关 交易笔数超限，稍后结算/need later settle
     */
    public static final int ERR_NEED_SETTLE_LATER = -24;
    /**
     * 预处理相关 存储空间不足/no free space
     */
    public static final int ERR_NO_FREE_SPACE = -25;
    /**
     * 预处理相关 终端不支持该交易/no support trans
     */
    public static final int ERR_NOT_SUPPORT_TRANS = -26;
    /**
     * 卡号不一致
     */
    public static final int ERR_CARD_NO = -27;
    /**
     * 密码错误/password wrong
     */
    public static final int ERR_PASSWORD = -28;
    /**
     * 参数错误/param error
     */
    public static final int ERR_PARAM = -29;

    /**
     * 终端批上送未完成/
     */
    public static final int ERR_BATCH_UP_NOT_COMPLETED = -31;
    /**
     * 金额超限/amount transfinite
     */
    public static final int ERR_AMOUNT = -33;
    /**
     * 工作密钥长度错误/twk length fail
     */
    public static final int ERR_TWK_LENGTH = -39;

    /**
     * 主管密码错/super pwd wrong
     */
    public static final int ERR_SUPPWD_WRONG = -40;
    /**
     * 主管密码错/manager pwd wrong
     */
    public static final int ERR_LOG_ON_ERR = -41;

    /**
     * 接收数据有误/receive data error
     */
    public static final int ERR_RECORD_DATA = -42;

    public static final int ERR_WRITE_PIN = -43;
    public static final int ERR_WRITE_MAK = -44;
    public static final int ERR_WRITE_TDK = -45;
}
