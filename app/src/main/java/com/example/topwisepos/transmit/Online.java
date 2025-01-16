package com.example.topwisepos.transmit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.topwisepos.entity.TransData;
import com.example.topwisepos.entity.TransResult;

/**
 * Creation date：2021/9/1 on 15:09
 * Describe:
 * Author:wangweicheng
 */
public class Online {
    private static final String TAG =  Online.class.getSimpleName();
    private static Online online;
    protected Gson jsonGson;
    private Online() {
        jsonGson =  new GsonBuilder().create();
    }
    public synchronized static Online getInstance() {
        if (online == null) {
            online = new Online();
        }
        return online;
    }

    public int transMit(TransData transData){

      //  int ret = comm.onInitPath();
        //like this
//        SaleRequest initialiseRequest = new SaleRequest();
//        initialiseRequest.setF003("000000");//hardcode keep it zero zero in pos
//        initialiseRequest.setF004(com.example.topwisepos.utils.getFormatted);///AMOUNTWHICHU HAVE ENTRED IN AMOUNT PAGE
//        initialiseRequest.setF011(stan);///STAN INCREMENTAL
//        initialiseRequest.setF022(posentrymode+"1");//posentrymode
//        initialiseRequest.setF025("00");
////        initialiseRequest.setF023("11");//hardcode
////        initialiseRequest.setF025("00");//pontofservice
//        initialiseRequest.setF035(f035);//track2data replace D
////        initialiseRequest.setF036("10001001767500A00137");
//        initialiseRequest.setF041("10020611");//tid hard code  database
//        initialiseRequest.setF042("107113000078456");//database  hard code
//        initialiseRequest.setF047("30");//ENCRPTIONDATA WHAT KIND OF?????
//        if (transData.isHasPin()){
//                    initialiseRequest.setF052(pinblockdata);//PINBLOCKDATA
//             initialiseRequest.setF053("10001001767500A00137");
//        }

//        initialiseRequest.setF055(emv_data);
//        initialiseRequest.setF057("20210826");//YYYYMMDDbatchnuber
//        initialiseRequest.setF062(stan);//reciptnumber
//        initialiseRequest.setMsgType("0200");//response
//        Gson gson = new Gson();
        String testData = "{\"F003\":\"000000\",\"F004\":\"000000250000\",\"F011\":\"002130\",\"F014\":\"4912\",\"F022\":\"071\",\"F023\":\"01\",\"F025\":\"00\",\"F035\":\"1743252BF9555EAEA7F5BC8FC34242E57D905BA78D16309E\",\"F036\":\"10001002502900A0009F\",\"F041\":\"10025029\",\"F042\":\"119113000090432\",\"F047\":\"22\",\"F052\":\"C0E2932C7A3113EA\",\"F053\":\"10001002502900A00097\",\"F055\":\"9F260859CA1F7CE86EF0279F2701809F10120110A0000F040000000000000000000000FF9F37048FC34B409F36020002950500000480019A032108319B0200009C01009F02060000002500005F2A0203565F340101820219819F1A0203569F03060000000000009F3303E060089F34034203009F3501229F1E0830303030303030318407A00000000410109F090200029F4104000000089F0702FF004F07A0000000041010\",\"F057\":\"000002\",\"F062\":\"002130\",\"MsgType\":\"0200\"}";
        Response testResponse = new Response(0,"");
                //comm.onSendAndRecv(testData);
        testResponse.setRetCode(0);
        testResponse.setData("{\"F062\":\"002130\",\"F041\":\"10025029\",\"F011\":\"002130\",\"F022\":\"071\",\"F055\":\"910A000000000000000000008A023030\",\"MsgType\":\"0210\",\"F012\":\"145820\",\"F013\":\"0831\",\"F046\":\"81 - CRYPTO ERROR\",\"F003\":\"000000\",\"F004\":\"000000250000\",\"F037\":\"000007916860\",\"F059\":\"530208\",\"F104\":\"NSDL Payments Bank Limited\",\"F039\":\"00\"}");

        //unpack to transData
        return unpack(testResponse.getData(),transData);
    }


    private int unpack(String jsonRecv,TransData transData){
        JsonMessage jRecv = jsonGson.fromJson(jsonRecv, JsonMessage.class);
        String temp = jRecv.getF039();
        if (TextUtils.isEmpty(temp)){
            return TransResult.ERR_BAG;
        }
        transData.setResponseCode(temp);
        // field 22
         temp = jRecv.getF022();;
        if (!TextUtils.isEmpty(temp)) {
            transData.setField22(temp);
            Log.d(TAG,"unpack F022 = " + temp );
        }
        // field 23 CardSerialNo
        temp = jRecv.getF023();;
        if (!TextUtils.isEmpty(temp)) {
            transData.setCardSerialNo(temp);
            Log.d(TAG,"unpack F023 = " + temp );
        }
        // field 25
        // field 26

        // field 32 AcqCenterCode
        temp = jRecv.getF032();
        if (!TextUtils.isEmpty(temp)) {
            transData.setAcqCenterCode(temp);
            Log.d(TAG,"unpack F032 = " + temp );
        }

        // field 35
        // field 36

        // field 37 RefNo
        temp = jRecv.getF037();
        if (!TextUtils.isEmpty(temp)) {
            transData.setRefNo(temp);
            Log.d(TAG,"unpack F037 = " + temp );
        }

        // field 38 AuthCode
        temp = jRecv.getF038();
        if (!TextUtils.isEmpty(temp)) {
            transData.setAuthCode(temp);

            Log.d(TAG,"unpack F038 = " + temp );
        }

        // field 41 check Terminal no
        temp = jRecv.getF041();
        if (!TextUtils.isEmpty(temp)) {
            transData.setTermID(temp);
            Log.d(TAG,"unpack F041 = " + temp );
        }

        // field 42 check Merchants
        temp = jRecv.getF042();
        if (!TextUtils.isEmpty(temp)) {
            transData.setMerchID(temp);
            Log.d(TAG,"unpack F042 = " + temp );
        }

        // field 43

        // field 44
        temp = jRecv.getF044();
        if (!TextUtils.isEmpty(temp)) {
            Log.d(TAG,"unpack F044 = " + temp );

        }
        //46
        temp = jRecv.getF046();
        if (!TextUtils.isEmpty(temp)) {
            Log.d(TAG,"unpack F046 = " + temp );
          //  transData.setXXX
        }
        // field 48

        // field 52
        temp = jRecv.getF052();
        if (!TextUtils.isEmpty(temp)) {
            transData.setField52(temp);
            Log.d(TAG,"unpack F052 = " + temp );
        }

        // field 53

        // field 54
        temp = jRecv.getF054();
        if (!TextUtils.isEmpty(temp)) {
//            transData.setBalanceFlag(temp.substring(7, 8));
//            transData.setBalance(temp.substring(temp.length() - 12, temp.length()));
            Log.d(TAG,"unpack F054 = " + temp );
        }

        // field 55
        temp = jRecv.getF055();
        if (!TextUtils.isEmpty(temp)) {
            transData.setRecvIccData(temp);
            Log.d(TAG,"unpack F055 = " + temp );
        }
        //....
        return 0;
    }
}
