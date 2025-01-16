package com.example.topwisepos.transmit;

/**
 * Creation date：2021/9/1 on 15:05
 * Describe:
 * Author:wangweicheng
 */
public class Response {
    int retCode;
    String data;

    public int getRetCode() {
        return retCode;
    }

    public Response(int retCode, String data) {
        this.retCode = retCode;
        this.data = data;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
