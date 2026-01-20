package com.shehan.adsmanager.classes;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    private static final int MAX_RETRY = 2;
    private static final long RETRY_DELAY = 1500;

    private final AdsManager adsManager;
    private final Application application;
    private final Handler mainHandler = new android.os.Handler(Looper.getMainLooper());
    public volatile InterstitialAd mInterstitial;
    public volatile AppOpenAd mAppOpen;
    public volatile RewardedAd mReward;
    public volatile RewardedInterstitialAd mRewardInt;
    private final String TAG = "PRELOAD";

    public PreLoad(@NonNull AdsManager adsManager) {
        this.adsManager = Objects.requireNonNull(adsManager, "AdsManager cannot be null.");
        this.application = adsManager.application();
    }

    /// ---------------- Interstitial ----------------
    public void Load_Int_Ads(int index) {
        if (isDisabled()) return;

        AdRequest adRequest = new AdRequest.Builder().build();
        String unitId = adsManager.resolver().interstitialId(index);
        loadInterstitial(unitId, adRequest, 0);
    }

    private void loadInterstitial(@NonNull String unitId, @NonNull AdRequest request, int attempt) {
        InterstitialAd.load(application, unitId, request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Interstitial ads preload failed : " + loadAdError.getMessage());
                retry(() -> loadInterstitial(unitId, request, attempt + 1), attempt, loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitial = interstitialAd;
                Log.d(TAG, "Interstitial ads preloaded.");
            }
        });
    }

    /// ---------------- App Open ----------------
    public void Load_App_Open(int index) {
        if (isDisabled()) return;

        String unitId = adsManager.resolver().appOpenId(index);
        AdRequest adRequest = new AdRequest.Builder().build();
        loadAppOpen(unitId, adRequest, 0);
    }

    private void loadAppOpen(@NonNull String unitId, @NonNull AdRequest request, int attempt) {
        AppOpenAd.load(application, unitId, request, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "App Open ads preload failed : " + loadAdError.getMessage());
                retry(() -> loadAppOpen(unitId, request, attempt + 1), attempt, loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                mAppOpen = appOpenAd;
                Log.d(TAG, "App Open ads preloaded.");
            }
        });
    }

    /// ---------------- Reward Ads ----------------
    public void Load_Reward_Ads(int index) {
        if (isDisabled()) return;

        String unitId = adsManager.resolver().rewardedId(index);
        AdRequest adRequest = new AdRequest.Builder().build();
        loadReward(unitId, adRequest, 0);
    }

    private void loadReward(@NonNull String unitId, @NonNull AdRequest request, int attempt) {
        RewardedAd.load(application, unitId, request, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Rewarded ads preload failed (" + attempt + "): " + loadAdError.getMessage());
                retry(() -> loadReward(unitId, request, attempt + 1), attempt, loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mReward = rewardedAd;
                Log.d(TAG, "Rewarded ads preloaded.");
            }
        });
    }

    /// ---------------- Reward Int Ads ----------------
    public void Load_Reward_Int(int index) {
        if (isDisabled()) return;

        String unitId = adsManager.resolver().rewardedInterId(index);
        AdRequest adRequest = new AdRequest.Builder().build();
        loadRewardInt(unitId, adRequest, 0);
    }

    private void loadRewardInt(@NonNull String unitId, @NonNull AdRequest request, int attempt) {
        RewardedInterstitialAd.load(application, unitId, request, new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Rewarded int ads preload failed (" + attempt + "): " + loadAdError.getMessage());
                retry(() -> loadRewardInt(unitId, request, attempt + 1), attempt, loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                mRewardInt = rewardedInterstitialAd;
                Log.d(TAG, "Rewarded int ads preloaded.");
            }
        });
    }

    /// ---------------- Destroy all ads ----------------
    public void destroyAds() {
        mAppOpen = null;
        mInterstitial = null;
        mReward = null;
        mRewardInt = null;
        Log.d(TAG, "Preloaded ads destroyed.");
    }

    private boolean isDisabled() {
        AdUnitResolver resolver = adsManager.resolver();
        Log.d(TAG, "Ads are disabled");
        return resolver == null || resolver.isDisabled();
    }

    /// ---------------- Retry when ads failed --------------------------
    private void retry(@NonNull Runnable again, int retryCount, LoadAdError errorCode) {
        if (retryCount >= MAX_RETRY) return;
        if (errorCode.getCode() == AdRequest.ERROR_CODE_INVALID_REQUEST) return;

        Log.d(TAG, "Ads preload retrying");
        mainHandler.postDelayed(again, RETRY_DELAY);
    }
}
