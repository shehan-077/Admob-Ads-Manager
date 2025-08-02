package com.shehan.adsmanager.Modals;


import java.util.List;

public class AdMobIds {

    private String appId;
    private List<String> interstitialIds;
    private List<String> bannerIds;
    private List<String> appOpenIds;
    private List<String> rewardIds;
    private List<String> nativeIds;
    private List<String> rewardIntIds;

    public AdMobIds(String appId,
                    List<String> interstitialIds,
                    List<String> bannerIds,
                    List<String> appOpenIds,
                    List<String> rewardIds,
                    List<String> nativeIds,
                    List<String> rewardIntIds) {
        this.appId = appId;
        this.interstitialIds = interstitialIds;
        this.bannerIds = bannerIds;
        this.appOpenIds = appOpenIds;
        this.rewardIds = rewardIds;
        this.nativeIds = nativeIds;
        this.rewardIntIds = rewardIntIds;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getInterstitialIds() {
        return interstitialIds;
    }

    public void setInterstitialIds(List<String> interstitialIds) {
        this.interstitialIds = interstitialIds;
    }

    public List<String> getBannerIds() {
        return bannerIds;
    }

    public void setBannerIds(List<String> bannerIds) {
        this.bannerIds = bannerIds;
    }

    public List<String> getAppOpenIds() {
        return appOpenIds;
    }

    public void setAppOpenIds(List<String> appOpenIds) {
        this.appOpenIds = appOpenIds;
    }

    public List<String> getRewardIds() {
        return rewardIds;
    }

    public void setRewardIds(List<String> rewardIds) {
        this.rewardIds = rewardIds;
    }

    public List<String> getNativeIds() {
        return nativeIds;
    }

    public void setNativeIds(List<String> nativeIds) {
        this.nativeIds = nativeIds;
    }

    public List<String> getRewardIntIds() {
        return rewardIntIds;
    }

    public void setRewardIntIds(List<String> rewardIntIds) {
        this.rewardIntIds = rewardIntIds;
    }

    public String getInterstitialId(int index) {
        return getByIndex(interstitialIds, index);
    }

    public String getBannerId(int index) {
        return getByIndex(bannerIds, index);
    }

    public String getAppOpenId(int index) {
        return getByIndex(appOpenIds, index);
    }

    public String getRewardId(int index) {
        return getByIndex(rewardIds, index);
    }

    public String getNativeId(int index) {
        return getByIndex(nativeIds, index);
    }

    public String getRewardIntId(int index) {
        return getByIndex(rewardIntIds, index);
    }

    private String getByIndex(List<String> list, int index) {
        if (list == null || list.isEmpty() || index < 0 || index >= list.size()) return null;
        return list.get(index);
    }
}