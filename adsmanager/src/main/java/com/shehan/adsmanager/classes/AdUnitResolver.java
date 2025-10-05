package com.shehan.adsmanager.classes;

import static com.shehan.adsmanager.ads.TestAdUnits.TEST_APP_OPEN;
import static com.shehan.adsmanager.ads.TestAdUnits.TEST_BANNER;
import static com.shehan.adsmanager.ads.TestAdUnits.TEST_INTERSTITIAL;
import static com.shehan.adsmanager.ads.TestAdUnits.TEST_NATIVE;
import static com.shehan.adsmanager.ads.TestAdUnits.TEST_REWARDED;
import static com.shehan.adsmanager.ads.TestAdUnits.TEST_REWARDED_INTER;

import com.shehan.adsmanager.modules.AdMobIds;
import com.shehan.adsmanager.enums.AdsStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdUnitResolver {

    private final AdMobIds ids;
    private final AdsStatus status;

    public AdUnitResolver (AdMobIds ids, AdsStatus status) {
        this.ids = Objects.requireNonNull(ids, "AdMobIds cannot be null.");
        this.status = Objects.requireNonNull(status, "AdsStatus cannot be null.");
    }

    public String bannerId(int index) {
        return status == AdsStatus.TESTING ? TEST_BANNER : ids.getBannerId(index);
    }

    public String interstitialId(int index) {
        return status == AdsStatus.TESTING ? TEST_INTERSTITIAL : ids.getInterstitialId(index);
    }

    public String rewardedId(int index) {
        return status == AdsStatus.TESTING ? TEST_REWARDED : ids.getRewardId(index);
    }

    public String rewardedInterId(int index) {
        return status == AdsStatus.TESTING ? TEST_REWARDED_INTER : ids.getRewardIntId(index);
    }

    public String appOpenId(int index) {
        return status == AdsStatus.TESTING ? TEST_APP_OPEN : ids.getAppOpenId(index);
    }

    public String nativeId(int index) {
        return status == AdsStatus.TESTING ? TEST_NATIVE : ids.getNativeId(index);
    }

    public boolean isDisabled() { return status == AdsStatus.DISABLED; }
    public boolean isTesting()  { return status == AdsStatus.TESTING; }
    public AdsStatus status()   { return status; }

    public List<String> testUnits() {
        return Arrays.asList(TEST_BANNER, TEST_INTERSTITIAL, TEST_REWARDED, TEST_REWARDED_INTER, TEST_APP_OPEN, TEST_NATIVE);
    }
}
