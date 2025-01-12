package com.shehan.adsmanager.Modals;

public class AdMobIds {

    private String appId;
    private String interstitialId;
    private String bannerId;
    private String appOpenId;
    private String rewardId;
    private String nativeId;
    private String rewardIntId;

    public AdMobIds(String appId, String interstitialId, String bannerId, String appOpenId, String rewardId, String nativeId, String rewardIntId) {
        this.appId = appId;
        this.interstitialId = interstitialId;
        this.bannerId = bannerId;
        this.appOpenId = appOpenId;
        this.rewardId = rewardId;
        this.nativeId = nativeId;
        this.rewardIntId = rewardIntId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getInterstitialId() {
        return interstitialId;
    }

    public void setInterstitialId(String interstitialId) {
        this.interstitialId = interstitialId;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getAppOpenId() {
        return appOpenId;
    }

    public void setAppOpenId(String appOpenId) {
        this.appOpenId = appOpenId;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getRewardIntId() {
        return rewardIntId;
    }

    public void setRewardIntId(String rewardIntId) {
        this.rewardIntId = rewardIntId;
    }
}
