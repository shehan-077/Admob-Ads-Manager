package com.shehan.adsmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.shehan.adsmanager.Ads.NativeTemplateStyle;
import com.shehan.adsmanager.Ads.TemplateView;
import com.shehan.adsmanager.Class.AdsManagerInitializer;
import com.shehan.adsmanager.Class.AdsUnit;
import com.shehan.adsmanager.Class.PreLoad;
import com.shehan.adsmanager.Class.RequestHandler;

import java.util.Objects;

public class AdsManager {

    private static AdsManager adsManager;
    public AdsManagerInitializer initializer;
    private static Boolean isEnabled;
    public AppCompatActivity activity;
    private PreLoad preLoad;

    private AdsManager() {
        preLoad = new PreLoad(this);
    }

    public static AdsManager getInstance(AppCompatActivity activity, AdsManagerInitializer initializer, boolean enabled) {
        init(activity, initializer, enabled);
        return  adsManager;
    }

    public static AdsManager getInstance(AppCompatActivity activity) {
        init(activity, null, isEnabled);
        return adsManager;
    }

    private static void init(AppCompatActivity activity, AdsManagerInitializer initializer, boolean enabled) {
        if (adsManager == null) {
            adsManager = new AdsManager();
        }
        if (initializer != null) {
            adsManager.initializer = initializer;
        }
        adsManager.activity = activity;
        MobileAds.initialize(activity);
        isEnabled = enabled;
    }

    public void preLoad(AdsUnit adsUnit, int index) {
        if (isEnabled) {
            switch (adsUnit) {
                case INTERSTITIAL:
                    preLoad.Load_Int_Ads(index);
                    break;
                case REWARD:
                    preLoad.Load_Reward_Ads(index);
                    break;
                case APP_OPEN:
                    preLoad.Load_App_Open(index);
                    break;
                case REWARD_INT:
                    preLoad.Load_Reward_Int(index);
                    break;
            }
        }
    }

    public void destroyAds() {
        preLoad.destroyAds();
    }

    public void showInterstitialAds(int index, RequestHandler handler) {
        if (isEnabled) {
            if (preLoad.mInterstitial != null) {
                preLoad.mInterstitial.show(activity);
                preLoad.mInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        preLoad.Load_Int_Ads(index);
                        handler.onSuccess();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        handler.onError();
                    }
                });
            } else {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, initializer.getAdMobIds().getInterstitialId(index), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                handler.onError();
                            }

                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                interstitialAd.show(activity);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        preLoad.Load_Int_Ads(index);
                                        handler.onSuccess();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        handler.onError();
                                    }
                                });
                            }
                        });
            }
        } else {
            handler.onSuccess();
        }
    }

    public void showRewardAds(int index, RequestHandler handler) {
        if (isEnabled) {
            if (preLoad.mReward != null) {
                preLoad.mReward.show(activity, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        preLoad.Load_Reward_Ads(index);
                        handler.onSuccess();
                    }
                });
            } else {
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(activity, initializer.getAdMobIds().getRewardId(index), adRequest,
                        new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                handler.onError();
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        handler.onSuccess();
                                        preLoad.Load_Reward_Ads(index);
                                    }
                                });
                            }
                        });
            }
        } else {
            handler.onSuccess();
        }
    }

    public void showRewardIntAds(int index, RequestHandler handler) {
        if (isEnabled) {
            if (preLoad.mRewardInt != null) {
                preLoad.mRewardInt.show(activity, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        preLoad.Load_Reward_Int(index);
                        handler.onSuccess();
                    }
                });
            } else {
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedInterstitialAd.load(activity, initializer.getAdMobIds().getRewardIntId(index), adRequest,
                        new RewardedInterstitialAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                handler.onError();
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                                rewardedInterstitialAd.show(activity, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        handler.onSuccess();
                                        preLoad.Load_Reward_Int(index);
                                    }
                                });
                            }
                        });
            }
        } else {
            handler.onSuccess();
        }
    }

    public void showAppOpenAds(int index, RequestHandler handler) {
        if (isEnabled) {
            if (preLoad.mAppOpen != null) {
                preLoad.mAppOpen.show(activity);
                preLoad.mAppOpen.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        preLoad.destroyAds();
                        handler.onSuccess();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        handler.onError();
                    }
                });
            } else {
                AdRequest adRequest = new AdRequest.Builder().build();
                AppOpenAd.load(activity, initializer.getAdMobIds().getAppOpenId(index), adRequest,
                        new AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                handler.onError();
                            }

                            @Override
                            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                                appOpenAd.show(activity);
                                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        handler.onSuccess();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        handler.onError();
                                    }
                                });
                            }
                        });
            }
        } else {
            handler.onSuccess();
        }
    }

    public void showBannerAds(int index, LinearLayout container) {
        if (isEnabled) {
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                AdView adView = new AdView(activity);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                adView.setLayoutParams(layoutParams);
                adView.setAdUnitId(initializer.getAdMobIds().getBannerId(index));
                adView.setAdSize(AdSize.BANNER);
                container.removeAllViews();
                container.addView(adView);
                adView.loadAd(adRequest);
            } catch (Exception e) {
                System.out.println("Banner load error : " + Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public void showNativeAds(int index, LinearLayout container) {
        if (isEnabled) {
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.small_native_ad_layout, null, false);
                container.removeAllViews();
                container.addView(layout);
                TemplateView nativeAdView = layout.findViewById(R.id.my_template);
                AdLoader loader = new AdLoader.Builder(activity, initializer.getAdMobIds().getNativeId(index))
                        .forNativeAd(nativeAd -> {
                            NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                            nativeAdView.setVisibility(View.VISIBLE);
                            nativeAdView.setStyles(style);
                            nativeAdView.setNativeAd(nativeAd);
                        }).withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                nativeAdView.setVisibility(View.GONE);
                                System.out.println("Native ad load failed.");
                            }
                        }).build();
                loader.loadAd(adRequest);
            } catch (Exception e) {
                System.out.println("Native ad show process failed : " + Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public void showNativeAdsMedium(int index, LinearLayout container) {
        if (isEnabled) {
            try{
                AdRequest adRequest = new AdRequest.Builder().build();
                LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.medium_native_ad_layout, null, false);
                container.removeAllViews();
                container.addView(layout);
                TemplateView templateView = layout.findViewById(R.id.my_template_medium);
                AdLoader loader = new AdLoader.Builder(activity, initializer.getAdMobIds().getNativeId(index))
                        .forNativeAd(nativeAd -> {
                            NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                            templateView.setVisibility(View.VISIBLE);
                            templateView.setStyles(style);
                            templateView.setNativeAd(nativeAd);
                        }).withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                templateView.setVisibility(View.GONE);
                                System.out.println("Medium Native load failed : " + loadAdError.getMessage());
                            }
                        }).build();
                loader.loadAd(adRequest);
            } catch (Exception e) {
                System.out.println("Medium Native ad show process failed :" + Objects.requireNonNull(e.getMessage()));
            }
        }
    }
}
