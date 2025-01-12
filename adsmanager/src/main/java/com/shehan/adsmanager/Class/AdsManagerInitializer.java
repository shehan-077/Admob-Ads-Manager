package com.shehan.adsmanager.Class;

import com.shehan.adsmanager.Modals.AdMobIds;

public class AdsManagerInitializer {

    private static AdsManagerInitializer initializer;
    private AdMobIds adMobIds;

    private AdsManagerInitializer(AdMobIds adMobIds) {
        this.adMobIds = adMobIds;
    }

    public static AdsManagerInitializer getInstance(AdMobIds adMobIds) {
        if (initializer == null) {
            initializer = new AdsManagerInitializer(adMobIds);
        }

        return initializer;
    }

    public static AdsManagerInitializer getInstance() {
        return initializer;
    }

    public AdMobIds getAdMobIds() {
        return adMobIds;
    }

    public void setAdMobIds(AdMobIds adMobIds) {
        this.adMobIds = adMobIds;
    }
}
