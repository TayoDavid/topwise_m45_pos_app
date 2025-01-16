package com.example.topwisepos.emv;

import com.example.topwisepos.entity.TransData;

public interface OnEmvProcessStateChange {
    void onInputPin(String pin);
    void onDismissInputPin();
    void onRetry(int retryFlag);
    void onCardDetected(TransData transData);
    void onCompleted(TransData transData);
    void showLoader(boolean show);
}
