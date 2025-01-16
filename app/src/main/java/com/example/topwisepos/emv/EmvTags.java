package com.example.topwisepos.emv;

import com.example.topwisepos.entity.TransData;
import com.topwise.manager.emv.api.IEmv;
import com.topwise.toptool.api.packer.ITlv;
import com.topwise.toptool.api.packer.TlvException;
import com.topwise.toptool.impl.TopTool;


/**
 * 创建日期：2021/4/16 on 15:15
 * 描述:
 * 作者:wangweicheng
 */
public class EmvTags {
    /**
     * sale 55 filed tags
     */
    public static final int[] TAGS_SALE_BYTE = {0x57, 0x9F26, 0x9F27, 0x9F10, 0x9F37, 0x9F36, 0x95, 0x9A, 0x9C, 0x9F02, 0x5F2A,
            0x82, 0x9F1A, 0x9F03, 0x9F33, 0x9F34, 0x9F35, 0x9F1E, 0x84, 0x9F09, 0x9F41, 0x9F63 };

    /**
     * query filed tags
     */
    public static final int[] TAGS_QUE = { 0x9F26, 0x9F27, 0x9F10, 0x9F37, 0x9F36, 0x95, 0x9A, 0x9C, 0x9F02, 0x5F2A,
            0x82, 0x9F1A, 0x9F03, 0x9F33, 0x9F34, 0x9F35, 0x9F1E, 0x84, 0x9F09, 0x9F41, 0x9F63 };
    /**
     * 上送
     *         postive55Tag = new String[]{TlvTagUtil.TAG_95, TlvTagUtil.TAG_9F1E,
     *                 TlvTagUtil.TAG_9F10, TlvTagUtil.TAG_9F36, TlvTagUtil.TAG_DF31};
     */
    public static final int[] TAGS_DUP_BYTE ={0x95, 0x9F1E, 0x9F10, 0x9F36, 0xDF31};

    /**
     * 冲正
     */
    public static final int[] TAGS_DUP = { 0x95, 0x9F10, 0x9F1E, 0xDF31 };
    //========================end

    public static byte[] getF55( IEmv iEmv) {
        return getValueList(TAGS_SALE_BYTE, iEmv);
    }

    private static byte[] getValueList(int[] tags, IEmv emv) {
        if (tags == null || tags.length == 0) {
            return null;
        }

        ITlv tlv = TopTool.getInstance().getPacker().getTlv();
        ITlv.ITlvDataObjList tlvList = tlv.createTlvDataObjectList();
        for (int tag : tags) {
            try {
                byte[] value = emv.getTlv(tag);
                if (value == null || value.length == 0) {
                    if (tag == 0x9f03) {
                        value = new byte[6];
                    } else {
                        continue;
                    }
                }
                ITlv.ITlvDataObj obj = tlv.createTlvDataObject();
                obj.setTag(tag);
                obj.setValue(value);
                tlvList.addDataObj(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return tlv.pack(tlvList);
        } catch (TlvException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * transtype:
     * SALE 0x00
     * QUERY  0x31
     * PRE Auth 0x03
     * Refund 0x20
     * SALE VOID  0x20
     * @param transData
     * @return
     */
    public static byte checkKernelTransType(TransData transData) {
        return 0x00;
    }
}
