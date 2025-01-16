package com.example.topwisepos;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static com.example.topwisepos.emv.EmvResultUtlis.getDate;
import static com.example.topwisepos.emv.EmvResultUtlis.getDatetime;
import static com.example.topwisepos.emv.EmvResultUtlis.getTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.topwisepos.databinding.ActivityMainBinding;
import com.example.topwisepos.device.ConfiUtils;
import com.example.topwisepos.emv.EmvResultUtlis;
import com.example.topwisepos.emv.EmvTags;
import com.example.topwisepos.emv.EmvTransProcessHandler;
import com.example.topwisepos.emv.OnEmvProcessStateChange;
import com.example.topwisepos.entity.TransData;
import com.example.topwisepos.param.AppCombinationHelper;
import com.example.topwisepos.utils.ExtensionsKt;
import com.google.gson.Gson;
import com.topwise.cloudpos.aidl.emv.level2.EmvKernelConfig;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.manager.AppLog;
import com.topwise.manager.card.entity.CardData;
import com.topwise.manager.card.impl.CardReader;
import com.topwise.manager.emv.api.IEmv;
import com.topwise.manager.emv.entity.EinputType;
import com.topwise.manager.emv.entity.EmvOutCome;
import com.topwise.manager.emv.entity.EmvTransPraram;
import com.topwise.manager.emv.enums.ETransStatus;
import com.topwise.manager.emv.impl.TransProcess;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends FragmentActivity implements OnEmvProcessStateChange {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static Context mContxt;
    private ActivityMainBinding binding;

    private CardReader cardReader;
    private TransData transData;

    private EditText pinEdt;
    private AlertDialog pinDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContxt = this;
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.amountEdt.setShowSoftInputOnFocus(false);
        binding.amountEdt.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                proceed();
            }
            return false;
        });

        setContentView(binding.getRoot());

        loadAidsAndCapks();

        binding.keyExchangeBtn.setOnClickListener(v -> {
            boolean successful = EmvTransProcessHandler.keyInjected(ConfiUtils.Mainkey, ConfiUtils.Pinkey);
            if (successful) {
                Toast.makeText(this, "Key Exchange Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Key Exchange Failed", Toast.LENGTH_SHORT).show();
            }
        });

        binding.purchaseBtn.setOnClickListener(v -> {
            proceed();
        });
    }

    private void proceed() {
        String amount = Objects.requireNonNull(binding.amountEdt.getText()).toString();
        if (amount.isEmpty()) {
            Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
        } else {
            processPurchase(amount);
        }
    }

    private void processPurchase(String amount) {
        transData = new TransData();
        transData.setMerchID("123456789012345");
        transData.setTermID("12345678");
        transData.setTransNo(123456);
        transData.setBatchNo(1);
        transData.setDate(getDate().substring(4));
        transData.setTime(getTime());
        transData.setDatetime(getDatetime());
        transData.setAmount(amount);

        checkCard();
    }

    private void checkCard() {
        showProgressDialog("Please Wait", "Reading Card...");
        TransProcess.getInstance().preInit(AppCombinationHelper.getInstance().getAppCombinationList());
        cardReader = CardReader.getInstance();
        cardReader.startFindCard(false, true, true, 6, new CardReader.onReadCardListener() {
            @Override
            public void getReadState(CardData cardData) {
                if (cardData != null && CardData.EReturnType.OK == cardData.geteReturnType()) {
                    switch (cardData.geteCardType()) {
                        case IC:
                            AppLog.d("jeremy", " ===========  find IC  card =============");
                            transData.setEnterMode(EinputType.CT);
                            break;
                        case RF:
                            transData.setEnterMode(EinputType.CTL);
                            AppLog.d("jeremy", " ===========  find RF card =============");
                            break;
                        default:
                            break;
                    }
                    gotoEmv();
                } else if (cardData != null && (cardData.geteReturnType() == CardData.EReturnType.OTHER_ERR || cardData.geteReturnType() == CardData.EReturnType.TIMEOUT)) {
                    displayDialog("Card Error", "Card not found! Please insert card");
                    cardReader.cancel();
                } else {
                    cardReader.cancel();
                }
                dismissLoading();
            }

            @Override
            public void onNotification(CardData.EReturnType eReturnType) {
                Log.i(TAG, "onNotification: " + eReturnType);
            }
        });
    }

    private void loadAidsAndCapks() {
        EmvTransProcessHandler.onInitCAPK(this);
    }

    private void gotoEmv() {
        EmvTerminalInfo emvTerminalInfo = EmvResultUtlis.setEmvTerminalInfo();
        EinputType einputType = transData.getEnterMode();
        if (einputType == EinputType.CT) {
            emvTerminalInfo.setUcTerminalEntryMode((byte) 0x05);
        } else {
            emvTerminalInfo.setUcTerminalEntryMode((byte) 0x07);
        }

        EmvTransPraram emvTransPraram = new EmvTransPraram(EmvTags.checkKernelTransType(transData));
        Calendar calender = Calendar.getInstance();
        @SuppressLint("DefaultLocale") String year = String.format("%04d", calender.get(Calendar.YEAR));
        emvTransPraram.setAucTransDate(year.substring(2) + transData.getDate());
        emvTransPraram.setAucTransTime(transData.getTime());
        emvTransPraram.setTransNo(transData.getTransNo());
        String amount = transData.getAmount();
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        String amountOther = transData.getCashAmount();
        if (TextUtils.isEmpty(amountOther)) {
            amountOther = "0";
        }
        emvTransPraram.setAmount(Long.parseLong(amount));
        emvTransPraram.setAmountOther(Long.parseLong(amountOther));
        emvTransPraram.setAucTransCurCode("566");

        EmvKernelConfig emvKernelConfig = EmvResultUtlis.setEmvKernelConfig();

        IEmv emvHandler = EmvTransProcessHandler.getEmvHandler();
        emvHandler.init(einputType);
        emvHandler.setProcessListener(new EmvTransProcessHandler(transData, emvHandler, this));
        emvHandler.setTerminalInfo(emvTerminalInfo);
        emvHandler.setTransPraram(emvTransPraram);
        emvHandler.setKernelConfig(emvKernelConfig);

        EmvOutCome emvOutCome = emvHandler.StartEmvProcess();

        if (ETransStatus.ONLINE_APPROVE == emvOutCome.geteTransStatus() ||
                ETransStatus.OFFLINE_APPROVE == emvOutCome.geteTransStatus()) {
            runOnUiThread(() -> Toast.makeText(this, "Emv Process Success", Toast.LENGTH_LONG).show());
        } else if (ETransStatus.ONLINE_REQUEST == emvOutCome.geteTransStatus()) { //eg rufund
            runOnUiThread(() -> Toast.makeText(this, "Emv Process Success", Toast.LENGTH_LONG).show());
        } else {
            runOnUiThread(() -> Toast.makeText(this, "Emv Process Failed", Toast.LENGTH_LONG).show());
        }
        cardReader.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContxt = null;
    }

    @Override
    public void onInputPin(String pin) {
        dismissLoading();
        runOnUiThread(() -> {
            if (pin.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please Enter Pin " + ExtensionsKt.getToAmount(transData.getAmount()));
                builder.setCancelable(false);
                pinEdt = new EditText(this);
                pinEdt.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                pinEdt.setInputType(InputType.TYPE_MASK_CLASS);
                pinEdt.setCursorVisible(false);
                pinEdt.setShowSoftInputOnFocus(false);
                builder.setView(pinEdt);
                pinDialog = builder.create();
                pinDialog.show();
            } else {
                pinEdt.setText(pin);
            }
        });
    }

    @Override
    public void onDismissInputPin() {
        pinDialog.dismiss();
    }

    private void displayDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(title)
                .setMessage(msg)

                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onRetry(int retryFlag) {
        dismissLoading();
        Log.e("EMV LISTENER", "OnRetry: $retryFlag");
        switch (retryFlag) {
            case 97: {
                displayDialog("Pin Pad Error", "Time out");
            }
            break;
            case 98: {
                displayDialog("Pin Pad Cancelled", "You have cancelled pin input.");
            }
            break;
            case 90: {
                displayDialog("Pin Pad Error", "Pin is null or empty.");
            }
            break;
            case 99: {
                displayDialog("Pin Pad Error", "Something went wrong with the pin pad.");
            }
            break;
            default: {
                displayDialog("Error", "Something went wrong");
            }
        }
    }

    @Override
    public void onCardDetected(TransData transData) {
        dismissLoading();
    }

    @Override
    public void onCompleted(TransData transData) {
        dismissLoading();
        String transDataJson = new Gson().toJson(transData);
        Log.i(TAG, transDataJson);
        runOnUiThread(() -> {
            EmvTransProcessHandler.print(this, transData, new PrintListener());
        });
    }

    @Override
    public void showLoader(boolean show) {
        showProgressDialog("Please Wait", "Processing...");
    }

    private void showProgressDialog(String title, String msg) {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
    }

    private void dismissLoading() {
        if (progressDialog != null) {
            runOnUiThread(() -> {
                progressDialog.dismiss();
                progressDialog = null;
            });
        }
    }

    public class PrintListener extends AidlPrinterListener.Stub {

        @Override
        public void onError(int code) {
            runOnUiThread(() -> {
                displayDialog("Error", "Error printing receipt");
            });
        }

        @Override
        public void onPrintFinish() {
            runOnUiThread(() -> {
                try {
                    Toast.makeText(MainActivity.this, "Receipt printed successfully", Toast.LENGTH_LONG).show();
                    EmvTransProcessHandler.beep();
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Unable to make the beep sound", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}