package com.shehan.adsmanager.modules;


import java.util.List;
import java.util.Objects;

public class AdMobIds {

    private final String appId;
    private final List<String> interstitialIds;
    private final List<String> bannerIds;
    private final List<String> appOpenIds;
    private final List<String> rewardIds;
    private final List<String> nativeIds;
    private final List<String> rewardIntIds;

    public AdMobIds(String appId,
                    List<String> interstitialIds,
                    List<String> bannerIds,
                    List<String> appOpenIds,
                    List<String> rewardIds,
                    List<String> nativeIds,
                    List<String> rewardIntIds) {

        this.appId = Objects.requireNonNull(appId, "App ID cannot be null");
        this.interstitialIds = Objects.requireNonNull(interstitialIds, "Interstitial list cannot be null");
        this.bannerIds = Objects.requireNonNull(bannerIds, "Banner list cannot be null");
        this.appOpenIds = Objects.requireNonNull(appOpenIds, "App Open list cannot be null");
        this.rewardIds = Objects.requireNonNull(rewardIds, "Reward list cannot be null");
        this.nativeIds = Objects.requireNonNull(nativeIds, "Native list cannot be null");
        this.rewardIntIds = Objects.requireNonNull(rewardIntIds, "Reward Interstitial list cannot be null");
    }

    public String getAppId() { return appId; }

    public List<String> getInterstitialIds() { return interstitialIds; }

    public List<String> getBannerIds() { return bannerIds; }

    public List<String> getAppOpenIds() { return appOpenIds; }

    public List<String> getRewardIds() { return rewardIds; }

    public List<String> getNativeIds() { return nativeIds; }

    public List<String> getRewardIntIds() { return rewardIntIds; }


    public String getInterstitialId(int index) { return getByIndex(interstitialIds, index); }

    public String getBannerId(int index) { return getByIndex(bannerIds, index); }

    public String getAppOpenId(int index) { return getByIndex(appOpenIds, index); }

    public String getRewardId(int index) { return getByIndex(rewardIds, index); }

    public String getNativeId(int index) { return getByIndex(nativeIds, index); }

    public String getRewardIntId(int index) { return getByIndex(rewardIntIds, index); }

    private String getByIndex(List<String> list, int index) {
        if (list == null || list.isEmpty()) return "";
        if (index < 0 || index >= list.size()) return list.get(0);
        return list.get(index);
    }
}