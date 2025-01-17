package com.example.topwisepos.emv;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.topwisepos.MainActivity;
import com.example.topwisepos.TWApplication;
import com.example.topwisepos.device.ConfiUtils;
import com.example.topwisepos.device.Device;
import com.example.topwisepos.entity.TransData;
import com.example.topwisepos.entity.TransResult;
import com.example.topwisepos.param.AidParam;
import com.example.topwisepos.param.AppCombinationHelper;
import com.example.topwisepos.param.CapkParam;
import com.example.topwisepos.transmit.Online;
import com.example.topwisepos.utils.CardUtil;
import com.example.topwisepos.utils.ExtensionsKt;
import com.example.topwisepos.utils.LedColor;
import com.topwise.cloudpos.aidl.buzzer.AidlBuzzer;
import com.topwise.cloudpos.aidl.emv.level2.Combination;
import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.pinpad.GetPinListener;
import com.topwise.cloudpos.aidl.pinpad.PinParam;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.Align;
import com.topwise.cloudpos.aidl.printer.ImageUnit;
import com.topwise.cloudpos.aidl.printer.PrintTemplate;
import com.topwise.cloudpos.aidl.printer.TextUnit;
import com.topwise.cloudpos.data.LedCode;
import com.topwise.cloudpos.data.PinpadConstant;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.manager.AppLog;
import com.topwise.manager.TopUsdkManage;
import com.topwise.manager.emv.api.IEmv;
import com.topwise.manager.emv.entity.EmvAidParam;
import com.topwise.manager.emv.entity.EmvOnlineResp;
import com.topwise.manager.emv.entity.EmvPinEnter;
import com.topwise.manager.emv.enums.ECVMStatus;
import com.topwise.manager.emv.enums.EKernelType;
import com.topwise.manager.emv.enums.EOnlineResult;
import com.topwise.manager.emv.enums.EPinType;
import com.topwise.manager.emv.impl.ETransProcessListenerImpl;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.impl.TopTool;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Creation date：2021/6/23 on 16:30
 * Describe:
 * Author:wangweicheng
 */
public class EmvTransProcessHandler extends ETransProcessListenerImpl {
    private static final String TAG = EmvTransProcessHandler.class.getSimpleName();

    private final AidlPinpad mPinPad = TWApplication.usdkManage.getPinpad(0);

    private final TransData transData;
    private ConditionVariable cv;

    private int intResult;
    private final IEmv emv;
    private final OnEmvProcessStateChange onEmvProcessStateChange;
    private static final IConvert convert = TopTool.getInstance().getConvert();

    public EmvTransProcessHandler(TransData transData, IEmv emv, OnEmvProcessStateChange listener) {
        this.transData = transData;
        this.emv = emv;
        this.onEmvProcessStateChange = listener;
    }

    public static IEmv getEmvHandler() {
        return TWApplication.usdkManage.getEmvHelper();
    }

    public static void onInitCAPK(Context ctx) {
        CapkParam capkParam = new CapkParam();
        capkParam.init(ctx);
        capkParam.saveAll();
        AidParam aidParam = new AidParam();
        aidParam.init(ctx);
        aidParam.saveAll();
    }

    public static boolean keyInjected(String tmk, String tpk) {
        boolean successful = Device.injectMain(ConfiUtils.mMainkey, convert.strToBcd(tmk, IConvert.EPaddingPosition.PADDING_RIGHT));
        Log.d("TAG", "injectMain ======" + successful);

        successful = Device.injectPIK(ConfiUtils.KEYTYPE_PEK, ConfiUtils.mMainkey, ConfiUtils.pinIndex, convert.strToBcd(tpk, IConvert.EPaddingPosition.PADDING_RIGHT));
        Log.d("TAG", "injectPIK ======" + successful);

        return successful;
    }

    public static void beep() throws RemoteException {
        AidlBuzzer buzzer = TWApplication.usdkManage.getBuzzer();
        buzzer.beep(0, 1000);
    }

    public static void blinkLed(LedColor color) throws RemoteException {
        LedColor ledColor;
        int colorCode = 0;
        ledColor = Objects.requireNonNullElse(color, LedColor.GREEN);
        switch (ledColor) {
            case RED: colorCode = LedCode.OPER_LED_RED; break;
            case GREEN: colorCode = LedCode.OPER_LED_GREEN; break;
            case YELLOW: colorCode = LedCode.OPER_LED_YELLOW; break;
        }
        AidlLed led = TWApplication.usdkManage.getLed();
        led.setLed(colorCode, true);
    }

    public static void print(ContextWrapper context, TransData data, MainActivity.PrintListener listener) {
        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"topwise.ttf");
            PrintTemplate.getInstance().init(context,typeface);
            PrintTemplate template = PrintTemplate.getInstance();
            template.clear();

            int largeFontSize = 48;
            Bitmap bitmap = getBmpFromAssets(context, "bmp/print_bitmap.bmp");
            template.add(new ImageUnit(bitmap, bitmap.getWidth(), bitmap.getHeight()));
            template.add(new TextUnit("Remita Receipt", largeFontSize, Align.CENTER));
            template.add((new TextUnit("Customer Receipt", TextUnit.TextSize.NORMAL, Align.CENTER)).setBold(true));
            template.add(new TextUnit(""));
            template.add(new TextUnit("TID: " + data.getTermID()));
            template.add(new TextUnit("Merchant ID: " + data.getMerchID()));
            template.add(new TextUnit("Card No: " + data.getPan()));
            template.add(new TextUnit("Card Holder: " + data.getCardHolderName()));
            template.add(new TextUnit("Card Type: " + CardUtil.INSTANCE.getCardTypFromAid(data.getAid())));
            template.add(new TextUnit("Card S/N: " + data.getCardSerialNo()));
            template.add(new TextUnit("Exp Date: " + data.getExpDate()));
            template.add(new TextUnit("Aid: " + data.getAid()));

            template.add(new TextUnit("DateTime: " + data.getDatetime()));
            template.add(new TextUnit("RRN: " + data.getRefNo()));
            template.add(new TextUnit("ResponseCode: " + data.getResponseCode()));
            template.add(new TextUnit(""));
            template.add(new TextUnit("Amount: "));
            template.add(new TextUnit(""));
            template.add(new TextUnit(ExtensionsKt.getToAmount(data.getAmount()), largeFontSize));
            template.add(new TextUnit("\n\n\n\n"));

            AidlPrinter printer = TWApplication.usdkManage.getPrinter();

            printer.addRuiImage(template.getPrintBitmap(), 0);
            printer.printRuiQueue(listener);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Multiple Aid options
     * if the card has multiple application ,this callback will be called .
     * in this callback ,the application list should show to custom,the custom should
     * choose one application ,and emv process will continue
     *
     * @param aids the aid list
     * @return int  the return value is the chosen aid  index
     */
    @Override
    public int onReqAppAidSelect(final String[] aids) {
        AppLog.d(TAG, "requestAidSelect = ");
        cv = new ConditionVariable();
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mContxt);
            builder.setTitle("Please Choose Application ");
            builder.setSingleChoiceItems(aids, intResult, (dialog, which) -> intResult = which);
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                cv.open();
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        });
        if (cv != null) {
            cv.block();
        }
        AppLog.d(TAG, "intResult = " + intResult);
        return intResult;
    }

    /**
     * kernel type
     * EMV 0x00
     * KERNTYPE_MC = 0x02;
     * KERNTYPE_VISA = 0x03;
     * KERNTYPE_AMEX = 0x04;
     * KERNTYPE_JCB = 0x05;
     * KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
     * KERNTYPE_DPAS = 0x06;//Discover DPAS
     * KERNTYPE_QPBOC = 0x07;
     * KERNTYPE_RUPAY = 0x0D;
     * KERNTYPE_PURE = 0x12;
     */
    @Override
    public void onUpToAppKernelType(EKernelType kernelType) {
        transData.setKernelType(kernelType.getKernelID());
    }

    /**
     * final aid select ,this callback is before gpo ,If
     * there are any parameters that need to be changed,
     * you can set the TLV here
     */
    @Override
    public boolean onReqFinalAidSelect() {
        AppLog.d(TAG, "finalAidSelect = ");
        byte[] aucAid;
        String aid = null;
        byte[] aucRandom;
        String random = null;
        aucAid = emv.getTlv(0x4F);
        aucRandom = emv.getTlv(0x9f37);
        if (aucAid != null) {
            aid = BytesUtil.bytes2HexString(aucAid);
            random = BytesUtil.bytes2HexString(aucRandom);
            transData.setAid(aid);
            transData.setRandom(random);
            Log.d(TAG, "aid: " + aid);
        } else {
            return false;
        }
        return true;

    }

    /**
     * confirm Card no
     *
     * @return boolean  true: check card successfully emv process continue
     * fail: check card fail,the emv process will stop
     */
    @Override
    public boolean onConfirmCardInfo(String cardNo) {
        transData.setPan(cardNo);
        onEmvProcessStateChange.onCardDetected(transData);

        return true;
    }

    /**
     * request input PinBlock, this callback should
     *
     * @param pinType     ONLINE_PIN_REQ :online pin mode
     *                    OFFLINE_PLAIN_TEXT_PIN_REQ:offline pin mode
     *                    PCI_MODE_REQ:PCI pin mode
     * @param pinTryCount just use for offline pin to  indicates the remain time  of inputting  pins
     * @return EmvPinEnter
     * ecvmStatus :
     * ENTER_OK:input pin successfully
     * ENTER_BYPASS: bypass
     * ENTER_CANCEL: user cancel
     * ENTER_TIME_OUT: time out
     * PlainTextPin: the offline pin Plain Text ,it just use for offline pin
     */
    @Override
    public EmvPinEnter onReqGetPinProc(final EPinType pinType, int pinTryCount) {
        onEmvProcessStateChange.onInputPin("");
        EmvPinEnter emvPinEnter = new EmvPinEnter();
        cv = new ConditionVariable();
        try {
            mPinPad.setPinKeyboardMode(1);
            PinParam pinParam = new PinParam(
                    ConfiUtils.pinIndex,
                    pinType.getType(),
                    transData.getPan(),
                    PinpadConstant.KeyType.KEYTYPE_PEK,
                    "0,4,5,6,7,8,9,10,11,12");
            mPinPad.getPin(pinParam.getParam(), new GetPinListener.Stub() {
                @Override
                public void onInputKey(int len, String msg) {
                    Log.i(TAG, " onInputKey: " + "Len: " + len + " Msg: " + msg);
                    onEmvProcessStateChange.onInputPin(msg);
                }

                @Override
                public void onError(int errorCode) {
                    Log.i(TAG, " onError " + errorCode);
                    onEmvProcessStateChange.onRetry(errorCode);
                    emvPinEnter.setEcvmStatus(ECVMStatus.ENTER_CANCEL);
                    cv.open();
                }

                @Override
                public void onConfirmInput(byte[] pin) {
                    if (pin == null || pin.length == 0) {
                        onEmvProcessStateChange.onRetry(99);
                        emvPinEnter.setEcvmStatus(ECVMStatus.ENTER_BYPASS);
                        transData.setHasPin(false);
                    } else {
                        String pinBlock = convert.bcdToStr(pin);
                        Log.i(TAG, " onConfirmInput:- PinBlock: " + pinBlock);
                        transData.setPinblock(pinBlock);
                        emvPinEnter.setEcvmStatus(ECVMStatus.ENTER_OK);
                        if (pinType == EPinType.ONLINE_PIN_REQ) {
                            transData.setHasPin(true);
                            transData.setPinblock(pinBlock);
                        } else {
                            transData.setHasPin(false);
                            emvPinEnter.setPlainTextPin(pinBlock);
                        }
                    }
                    onEmvProcessStateChange.onDismissInputPin();
                    cv.open();
                }

                @Override
                public void onCancelKeyPress() {
                    Log.i(TAG, " onCancelKeyPress ");
                    onEmvProcessStateChange.onRetry(98);
                    emvPinEnter.setEcvmStatus(ECVMStatus.ENTER_CANCEL);
                    cv.open();
                }

                @Override
                public void onStopGetPin() {
                    Log.i(TAG, " onStopGetPin ");
                }

                @Override
                public void onTimeout() {
                    Log.i(TAG, "get  onTimeout  ");
                    onEmvProcessStateChange.onRetry(97);
                }
            });

        } catch (RemoteException e) {
            onEmvProcessStateChange.onRetry(90);
            e.printStackTrace();
        }
        cv.block();
        return emvPinEnter;
    }

    /**
     * IC Request Online
     *
     * @return EmvOnlineResp
     */
    @Override
    public EmvOnlineResp onReqOnlineProc() {
        //
        Log.i(TAG, "onRequestOnline =========== ");
        EmvOnlineResp emvEntity = new EmvOnlineResp();

        saveCardInfoAndCardSeq();
        saveTvrTsi();
        getFiled55();

        int onlineRet = Online.getInstance().transMit(transData);
        AppLog.e("ActionEmvProcess", "onlineRet  " + onlineRet);

        if (onlineRet == TransResult.SUCC) {
            if ("00".equals(transData.getResponseCode())) {
                emvEntity.seteOnlineResult(EOnlineResult.ONLINE_APPROVE);
                String rspF55 = transData.getRecvIccData();
                emvEntity.parseFiled55(rspF55);
                emvEntity.setAuthRespCode(transData.getResponseCode().getBytes());
                emvEntity.setExistAuthRespCode(true);
                transData.setOnlineTrans(true);
            } else { //Online  reject
                transData.setOnlineTrans(false);
                emvEntity.seteOnlineResult(EOnlineResult.ONLINE_REFER);
            }
        } else {
            transData.setOnlineTrans(false);
            emvEntity.seteOnlineResult(EOnlineResult.ONLINE_FAILED);
        }

        onEmvProcessStateChange.onCompleted(transData);

        return emvEntity;
    }

    @Override
    public List<Combination> onLoadCombinationParam() {
        AppLog.d(TAG, " onLoadCombinationParam =========");
        return AppCombinationHelper.getInstance().getAppCombinationList();
    }

    @Override
    public EmvAidParam onFindCurAidParamProc(String sAid) {
        AppLog.d(TAG, " onFindCurAidParamProc =========" + sAid);
        return AidParam.getCurrentAidParam(sAid);
    }

    @Override
    public EmvCapk onFindIssCapkParamProc(String sRid, byte bCapkIndex) {
        AppLog.d(TAG, " onFindIssCapkParamProc =========" + sRid + " bCapkIndex = " + BytesUtil.byte2HexString(bCapkIndex));
        return CapkParam.getEmvCapkParam(sRid, bCapkIndex);
    }

    private void saveCardInfoAndCardSeq() {
        byte[] track2 = emv.getTlv(0x57);
        String strTrack2 = convert.bcdToStr(track2);
        strTrack2 = strTrack2.split("F")[0];
        transData.setTrack2(strTrack2);
        AppLog.d(TAG, " saveCardInfoAndCardSeq  strTrack2 =" + strTrack2);
        // pan
        String pan = getPan(strTrack2);
        transData.setPan(pan);
        AppLog.d(TAG, " saveCardInfoAndCardSeq  pan =" + pan);
        // expire data
        byte[] expDate = emv.getTlv(0x5f24);
        if (expDate != null && expDate.length > 0) {
            String temp = convert.bcdToStr(expDate);
            transData.setExpDate(temp.substring(0, 4));
            AppLog.d(TAG, " saveCardInfoAndCardSeq  expDate =" + temp);
        }
        // card serial number
        byte[] cardSeq = emv.getTlv(0x5f34);
        if (cardSeq != null && cardSeq.length > 0) {
            String temp = convert.bcdToStr(cardSeq);
            transData.setCardSerialNo(temp.substring(0, 2));
            AppLog.d(TAG, " saveCardInfoAndCardSeq  cardSeq =" + temp);
        }
    }

    /**
     * AID	    M	Tag 84
     * App Name	M	Tag 9F12
     * TC     	M	Tag 9F26 (2GAC)
     * TVR	    M	Tag 95 (2GAC)
     * TSI	    M	Tag 9B (2GAC)
     * Card Holder Name	M	Tag 5F20
     */
    private void saveTvrTsi() {
        IEmv emv = TopUsdkManage.getInstance().getEmvHelper();
        String temp;
        try {
            byte[] valueTVR = emv.getTlv(0x95);
            if (valueTVR != null && valueTVR.length > 0) {
                temp = convert.bcdToStr(valueTVR);
                if (!TextUtils.isEmpty(temp)) transData.setTvr(temp);
                AppLog.emvd("EmvResultUtlis setTVR(): " + temp);
            }
            byte[] valueATC = emv.getTlv(0x9F36);
            if (valueATC != null && valueATC.length > 0) {
                temp = convert.bcdToStr(valueATC);
                if (!TextUtils.isEmpty(temp)) transData.setAtc(temp);
                AppLog.emvd("EmvResultUtlis setATC(): " + temp);
            }
            //0x9B
            byte[] valueTis = emv.getTlv(0x9B);
            if (valueTis != null && valueTis.length > 0) {
                temp = convert.bcdToStr(valueTis);
                if (!TextUtils.isEmpty(temp)) transData.setTsi(temp);
                AppLog.emvd("EmvResultUtlis setTsi(): " + temp);
            }
            //0x9F26
            byte[] valueTc = emv.getTlv(0x9F26);
            if (valueTc != null && valueTc.length > 0) {
                temp = convert.bcdToStr(valueTc);
                if (!TextUtils.isEmpty(temp)) transData.setTc(temp);
                AppLog.emvd("EmvResultUtlis setTc(): " + temp);
            }
            //0x9F12
            byte[] valueAppname = emv.getTlv(0x9F12);
            if (valueAppname != null && valueAppname.length > 0) {
                temp = new String(valueAppname);
                if (!TextUtils.isEmpty(temp)) transData.setEmvAppName(temp);
                AppLog.emvd("EmvResultUtlis setEmvAppName(): " + temp);
            }
            //0x84
            byte[] valueAid = emv.getTlv(0x84);
            if (valueAid != null && valueAid.length > 0) {
                temp = convert.bcdToStr(valueAid);
                if (!TextUtils.isEmpty(temp)) transData.setAid(temp);
                AppLog.emvd("EmvResultUtlis setAid(): " + temp);
            }
            byte[] valuehname = emv.getTlv(0x5F20);
            if (valuehname != null && valuehname.length > 0) {
//                AppLog.emvd("EmvResultUtlis Card Holder Name(): " + convert.bcdToStr(valuehname));
                temp = new String(valuehname);
                if (!TextUtils.isEmpty(temp)) transData.setCardHolderName(temp);
                AppLog.emvd("EmvResultUtlis Card Holder Name(): " + temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFiled55() {
        byte[] f55 = EmvTags.getF55(emv);
        if (f55 != null && f55.length > 0) {
            transData.setSendIccData(convert.bcdToStr(f55));
        }

    }

    public String getPan(String track) {
        if (track == null)
            return null;

        int len = track.indexOf('=');
        if (len < 0) {
            len = track.indexOf('D');
            if (len < 0)
                return null;
        }

        if ((len < 10) || (len > 19))
            return null;

        return track.substring(0, len);
    }

    public static Bitmap getBmpFromAssets(ContextWrapper context, String filename) {
        Bitmap mBitmap;
        AssetManager mAssetManager = context.getResources().getAssets();
        try {
            InputStream mInputStream = mAssetManager.open(filename);
            mBitmap = BitmapFactory.decodeStream(mInputStream);
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            mBitmap = null;
        }
        return mBitmap;
    }

}
