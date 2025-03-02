package com.cheqin.booking.network;


public interface APIResponseListener<T> {

    void onSuccess(int reqCode, T responseObject);

    void onError( int errorCode, String errorMessage);
}