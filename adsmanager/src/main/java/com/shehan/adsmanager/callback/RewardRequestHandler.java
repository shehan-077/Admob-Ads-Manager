package com.shehan.adsmanager.callback;

public interface RewardRequestHandler {

    void onShowed();
    void onDismissed();
    void onRewarded();
    void onFailedToShow(String error);
    void onError(String error);
}
