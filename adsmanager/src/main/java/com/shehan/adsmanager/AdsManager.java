package com.shehan.adsmanager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.shehan.adsmanager.ads.NativeTemplateStyle;
import com.shehan.adsmanager.ads.TemplateView;
import com.shehan.adsmanager.classes.AdUnitResolver;
import com.shehan.adsmanager.classes.AdsManagerInitializer;
import com.shehan.adsmanager.classes.AdsUnit;
import com.shehan.adsmanager.classes.PreLoad;
import com.shehan.adsmanager.callback.RequestHandler;
import com.shehan.adsmanager.enums.AdsStatus;
import com.shehan.adsmanager.enums.NativeAdsSize;
import com.shehan.adsmanager.loadingview.LoadingOverlay;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdsManager {

    private static volatile AdsManager adsManager;
    private static final AtomicBoolean mobileAdsInitialized = new AtomicBoolean(false);
    private final Application application;
    public AdsManagerInitializer initializer;
    private AdsStatus adsStatus = AdsStatus.ENABLED;
    private AdUnitResolver adUnitResolver;
    private final PreLoad preLoad;
    private final LoadingOverlay loading = new LoadingOverlay();
    @Nullable private Integer loaderTintColor = null;

    private AdsManager(@NonNull Application app) {
        this.application = app;
        preLoad = new PreLoad(this);
    }

    public static AdsManager init (@NonNull Context context, @NonNull AdsManagerInitializer initializer, @NonNull AdsStatus status) {
        if (adsManager == null) {
            synchronized (AdsManager.class) {
                if (adsManager == null) {
                    Application app = (Application) context.getApplicationContext();
                    adsManager = new AdsManager(app);
                }
            }
        }
        adsManager.initializer = Objects.requireNonNull(initializer, "Initializer cannot be null.");
        adsManager.adsStatus = Objects.requireNonNull(status, "Status cannot be null.");
        adsManager.adUnitResolver = new AdUnitResolver(initializer.getAdMobIds(), status);

        if (mobileAdsInitialized.compareAndSet(false, true)) {
            MobileAds.initialize(adsManager.application);
        }
        return adsManager;
    }

    public static AdsManager getInstance() {
        AdsManager manager = adsManager;
        if (manager == null) {
            throw new IllegalStateException("AdsManager not initialized. Call getInstance(context, initializer, status) first.");
        }
        return manager;
    }

    public void setAdsStatus(@NonNull AdsStatus status) {
        this.adsStatus = status;
        if (initializer != null) {
            this.adUnitResolver = new AdUnitResolver(initializer.getAdMobIds(), status);
        }
    }

    public void preLoad(AdsUnit adsUnit, int index) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) return;
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

    public Application application() {return application;}

    public AdUnitResolver resolver() {return adUnitResolver;}

    public void setLoadingColor(@ColorInt int color) {this.loaderTintColor = color;}

    public void destroyAds() {
        preLoad.destroyAds();
    }

    public void showInterstitialAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {handler.onSuccess(); return;}

        loading.show(activity, loaderTintColor);

        if (preLoad.mInterstitial != null) {
            InterstitialAd ad = preLoad.mInterstitial;
            preLoad.mInterstitial = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    preLoad.Load_Int_Ads(index);
                    handler.onSuccess();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    preLoad.Load_Int_Ads(index);
                    handler.onError(adError.getMessage());
                }
            });
            ad.show(activity);
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(application, resolver().interstitialId(index), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loading.dismiss();
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                preLoad.Load_Int_Ads(index);
                                handler.onSuccess();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                preLoad.Load_Int_Ads(index);
                                handler.onError(adError.getMessage());
                            }
                        });
                        interstitialAd.show(activity);
                    }
                });
    }

    public void showRewardAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {handler.onSuccess(); return;}

        loading.show(activity, loaderTintColor);

        if (preLoad.mReward != null) {
            RewardedAd ad = preLoad.mReward;
            preLoad.mReward = null;
            ad.show(activity, rewardItem -> {
                loading.dismiss();
                preLoad.Load_Reward_Ads(index);
                handler.onSuccess();
            });
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(application, adUnitResolver.rewardedId(index), adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loading.dismiss();
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewardedAd.show(activity, rewardItem -> {
                            loading.dismiss();
                            preLoad.Load_Reward_Ads(index);
                            handler.onSuccess();
                        });
                    }
                });
    }

    public void showRewardIntAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) { handler.onSuccess(); return;}

        loading.show(activity, loaderTintColor);

        if (preLoad.mRewardInt != null) {
            RewardedInterstitialAd ad = preLoad.mRewardInt;
            preLoad.mRewardInt = null;
            ad.show(activity, rewardItem -> {
                loading.dismiss();
                preLoad.Load_Reward_Int(index);
                handler.onSuccess();
            });
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(application, adUnitResolver.rewardedInterId(index), adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loading.dismiss();
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        rewardedInterstitialAd.show(activity, rewardItem -> {
                            loading.dismiss();
                            preLoad.Load_Reward_Int(index);
                            handler.onSuccess();
                        });
                    }
                });
    }

    public void showAppOpenAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {handler.onSuccess(); return;}

        loading.show(activity, loaderTintColor);

        if (preLoad.mAppOpen != null) {
            AppOpenAd ad = preLoad.mAppOpen;
            preLoad.mAppOpen = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    preLoad.Load_App_Open(index);
                    handler.onSuccess();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    preLoad.Load_App_Open(index);
                    handler.onError(adError.getMessage());
                }
            });
            ad.show(activity);
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(application, adUnitResolver.appOpenId(index), adRequest,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loading.dismiss();
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                preLoad.Load_App_Open(index);
                                handler.onSuccess();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                preLoad.Load_App_Open(index);
                                handler.onError(adError.getMessage());
                            }
                        });
                        appOpenAd.show(activity);
                    }
                });
    }

    public void showBannerAds(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) return;

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(container.getContext());
        container.removeAllViews();
        container.addView(adView);

        container.post(() -> {
            int width = container.getWidth();
            if (width == 0) width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int adWidth = Math.round(width / Resources.getSystem().getDisplayMetrics().density);
            AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(application, adWidth);

            adView.setAdUnitId(adUnitResolver.bannerId(index));
            adView.setAdSize(adSize);
            adView.loadAd(adRequest);
        });
    }

    public void showNativeAds(int index, @NonNull LinearLayout container, @NonNull NativeAdsSize size) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) return;

        if (size == NativeAdsSize.SMALL) {
            showNativeAdsSmall(index, container);
        } else if (size == NativeAdsSize.MEDIUM) {
            showNativeAdsMedium(index, container);
        }
    }

    private void showNativeAdsSmall(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) return;

        try {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.small_native_ad_layout, container, false);
            container.removeAllViews();
            container.addView(layout);
            TemplateView templateView = layout.findViewById(R.id.my_template);

            AdLoader loader = new AdLoader.Builder(container.getContext(), adUnitResolver.nativeId(index))
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        templateView.setVisibility(View.VISIBLE);
                        templateView.setStyles(style);
                        templateView.setNativeAd(nativeAd);

                        templateView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(@NonNull View view) {

                            }

                            @Override
                            public void onViewDetachedFromWindow(@NonNull View view) {
                                nativeAd.destroy();
                            }
                        });
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            templateView.setVisibility(View.GONE);
                            Log.w("AdsManager", "Native ad small failed to load: " + loadAdError.getMessage());
                        }
                    })
                    .build();
            loader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            Log.e("AdsManager", "Error showing native ads: " + e.getMessage());
        }
    }

    private void showNativeAdsMedium(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) return;

        try {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.medium_native_ad_layout, container, false);
            container.removeAllViews();
            container.addView(layout);
            TemplateView templateView = layout.findViewById(R.id.my_template_medium);

            AdLoader loader = new AdLoader.Builder(container.getContext(), adUnitResolver.nativeId(index))
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        templateView.setVisibility(View.VISIBLE);
                        templateView.setStyles(style);
                        templateView.setNativeAd(nativeAd);

                        templateView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(@NonNull View view) {

                            }

                            @Override
                            public void onViewDetachedFromWindow(@NonNull View view) {
                                nativeAd.destroy();
                            }
                        });
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            templateView.setVisibility(View.GONE);
                            Log.w("AdsManager", "Native ad Medium failed to load: " + loadAdError.getMessage());
                        }
                    })
                    .build();
            loader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            Log.e("AdsManager", "Error showing native ads: " + e.getMessage());
        }
    }
}
