package com.shehan.adsmanager.callback;

public interface ConsentRequestHandler {

    void onConsentReady(boolean canRequestAds);
    void onConsentError(String error);
}
