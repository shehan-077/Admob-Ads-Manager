package com.shehan.adsmanager.callback;

public interface RequestHandler {

    void onSuccess();
    void onError(String error);
}
