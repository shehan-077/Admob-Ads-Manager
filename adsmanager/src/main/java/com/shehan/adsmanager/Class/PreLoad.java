package com.shehan.adsmanager.Class;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.shehan.adsmanager.AdsManager;

import java.util.Objects;

public class PreLoad {

    private final AdsManager adsManager;
    public InterstitialAd mInterstitial;
    public AppOpenAd mAppOpen;
    public RewardedAd mReward;
    public RewardedInterstitialAd mRewardInt;

    public PreLoad(AdsManager adsManager) {
        this.adsManager = adsManager;
    }

    public void Load_Int_Ads() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(adsManager.activity, adsManager.initializer.getAdMobIds().getInterstitialId(),
                adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                System.out.println("Interstitial ads preload failed");
                System.out.println("Error : " + Objects.requireNonNull(loadAdError.getMessage()));
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitial = interstitialAd;
            }
        });
    }

    public void Load_App_Open() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(adsManager.activity, adsManager.initializer.getAdMobIds().getAppOpenId(),
                adRequest, new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println("App Open ads preload failed");
                        System.out.println("Error : " + Objects.requireNonNull(loadAdError.getMessage()));
                    }

                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        mAppOpen = appOpenAd;
                    }
                });
    }

    public void Load_Reward_Ads() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(adsManager.activity, adsManager.initializer.getAdMobIds().getRewardId(),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println("Reward ads preload failed");
                        System.out.println("Error : " + Objects.requireNonNull(loadAdError.getMessage()));
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mReward = rewardedAd;
                    }
                });
    }

    public void Load_Reward_Int() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(adsManager.activity, adsManager.initializer.getAdMobIds().getRewardIntId(),
                adRequest, new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println("Reward Interstitial ads preload failed");
                        System.out.println("Error : " + Objects.requireNonNull(loadAdError.getMessage()));
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        mRewardInt = rewardedInterstitialAd;
                    }
                });
    }

    public void destroyAds() {
        try {
            if (mAppOpen != null) {
                mAppOpen = null;
            }
            if (mInterstitial != null) {
                mInterstitial = null;
            }
            if (mReward != null) {
                mReward = null;
            }
            if (mRewardInt != null) {
                mRewardInt = null;
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
