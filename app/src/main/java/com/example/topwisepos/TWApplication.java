package com.example.topwisepos;

import android.app.Application;
import android.widget.Toast;
import com.example.topwisepos.param.*;
import com.topwise.manager.AppLog;
import com.topwise.manager.TopUsdkManage;

import java.util.List;

public class TWApplication extends Application {

    private static final String TAG = TWApplication.class.getSimpleName();

    public static TWApplication mApp;
    public static TopUsdkManage usdkManage;


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        init();
    }

    private void init(){
        usdkManage = TopUsdkManage.getInstance();
        usdkManage.init(this, ret -> {
            AppLog.i(TAG,"init OnConnection " + ret);
            if (ret){
                com.example.topwisepos.device.Device.enableHomeAndRecent(true);
            }else{
                Toast.makeText(TWApplication.this,"SDK Bind Failed!",Toast.LENGTH_SHORT).show();
            }
        });

        onInitCAPK();
    }

    public void onInitCAPK(){
        CapkParam capkParam = new CapkParam();
        capkParam.init(this);
        capkParam.saveAll();
        AidParam aidParam = new AidParam();
        aidParam.init(this);
        aidParam.saveAll();
    }
}
