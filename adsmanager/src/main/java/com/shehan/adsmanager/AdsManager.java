package com.shehan.adsmanager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ads.mediation.admob.AdMobAdapter;
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
import com.shehan.adsmanager.callback.RewardRequestHandler;
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
    private volatile AdsStatus adsStatus = AdsStatus.ENABLED;
    private volatile AdUnitResolver adUnitResolver;
    private final PreLoad preLoad;
    private final LoadingOverlay loading = new LoadingOverlay();
    @Nullable
    private Integer loaderTintColor = null;
    private final boolean debugBuild;
    private final String TAG = "ADS MANAGER";

    private AdsManager(@NonNull Application app) {
        this.application = app;
        preLoad = new PreLoad(this);
        this.debugBuild = isDebugBuild(app);
    }

    public static AdsManager init(@NonNull Context context,
            @NonNull AdsManagerInitializer initializer,
            @NonNull AdsStatus status) {
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
        adsManager.rebuildResolver();

        if (mobileAdsInitialized.compareAndSet(false, true)) {
            MobileAds.initialize(adsManager.application);
        }
        Log.d("ADS MANAGER", "Ads manager initialized complete.");
        return adsManager;
    }

    public static AdsManager getInstance() {
        AdsManager manager = adsManager;
        if (manager == null) {
            throw new IllegalStateException(
                    "AdsManager not initialized. Call getInstance(context, initializer, status) first.");
        }
        return manager;
    }

    private void rebuildResolver() {
        if (initializer == null)
            return;
        AdsStatus status = computeEffectiveStatus(adsStatus, debugBuild);
        this.adUnitResolver = new AdUnitResolver(initializer.getAdMobIds(), status);
    }

    private static AdsStatus computeEffectiveStatus(AdsStatus status,
            boolean debugBuild) {
        if (status == AdsStatus.DISABLED)
            return AdsStatus.DISABLED;
        if (status == AdsStatus.TESTING)
            return AdsStatus.TESTING;
        if (status == AdsStatus.HYBRID)
            return (debugBuild) ? AdsStatus.TESTING : AdsStatus.ENABLED;
        return AdsStatus.ENABLED;
    }

    public void preLoad(AdsUnit adsUnit, int index) {
        if (adUnitResolver == null || adUnitResolver.isDisabled())
            return;
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

    public Application application() {
        return application;
    }

    public AdUnitResolver resolver() {
        return adUnitResolver;
    }

    public void setLoadingColor(@ColorInt int color) {
        this.loaderTintColor = color;
    }

    public void destroyAds() {
        preLoad.destroyAds();
    }

    private boolean isDebugBuild(@NonNull Application app) {
        return (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public void showInterstitialAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {
            handler.onSuccess();
            return;
        }

        loading.show(activity, loaderTintColor);

        if (preLoad.mInterstitial != null) {
            InterstitialAd ad = preLoad.mInterstitial;
            preLoad.mInterstitial = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    preLoad.Load_Int_Ads(index);
                    Log.d(TAG, "Interstitial ads dismissed.");
                    handler.onSuccess();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    preLoad.Load_Int_Ads(index);
                    Log.e(TAG, "Interstitial ads failed to show. Error : " + adError.getMessage());
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
                        Log.e(TAG, "Interstitial ads failed to load. Error : " + loadAdError.getMessage());
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                preLoad.Load_Int_Ads(index);
                                Log.d(TAG, "Interstitial ads loaded.");
                                handler.onSuccess();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                preLoad.Load_Int_Ads(index);
                                Log.e(TAG, "Interstitial ads failed to show. Error : " + adError.getMessage());
                                handler.onError(adError.getMessage());
                            }
                        });
                        interstitialAd.show(activity);
                    }
                });
    }

    public void showRewardAds(@NonNull Activity activity, int index, @NonNull RewardRequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {
            handler.onShowed();
            handler.onDismissed();
            return;
        }

        loading.show(activity, loaderTintColor);

        if (preLoad.mReward != null) {
            RewardedAd ad = preLoad.mReward;
            preLoad.mReward = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    Log.e(TAG, "Rewarded ads failed to show. Error : " + adError.getMessage());
                    handler.onFailedToShow(adError.getMessage());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    loading.dismiss();
                    handler.onShowed();
                    Log.d(TAG, "Rewarded ads showed.");
                    preLoad.Load_Reward_Ads(index);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    handler.onDismissed();
                    Log.d(TAG, "Rewarded ads dismissed.");
                    preLoad.Load_Reward_Ads(index);
                }
            });

            ad.show(activity, rewardItem -> {
                loading.dismiss();
                preLoad.Load_Reward_Ads(index);
                handler.onRewarded();
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
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                Log.e(TAG, "Rewarded ads failed to show. Error : " + adError.getMessage());
                                handler.onFailedToShow(adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                loading.dismiss();
                                handler.onShowed();
                                Log.d(TAG, "Rewarded ads showed.");
                                preLoad.Load_Reward_Ads(index);
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                handler.onDismissed();
                                Log.d(TAG, "Rewarded ads dismissed.");
                                preLoad.Load_Reward_Ads(index);
                            }
                        });

                        ad.show(activity, rewardItem -> {
                            loading.dismiss();
                            preLoad.Load_Reward_Ads(index);
                            handler.onRewarded();
                        });
                    }
                });
    }

    public void showRewardIntAds(@NonNull Activity activity, int index, @NonNull RewardRequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {
            handler.onShowed();
            handler.onDismissed();
            return;
        }

        loading.show(activity, loaderTintColor);

        if (preLoad.mRewardInt != null) {
            RewardedInterstitialAd ad = preLoad.mRewardInt;
            preLoad.mRewardInt = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    Log.e(TAG, "Rewarded Int ads failed to show. Error : " + adError.getMessage());
                    handler.onFailedToShow(adError.getMessage());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    loading.dismiss();
                    handler.onShowed();
                    Log.d(TAG, "Rewarded Int ads showed.");
                    preLoad.Load_Reward_Int(index);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    handler.onDismissed();
                    Log.d(TAG, "Rewarded Int ads dismissed.");
                    preLoad.Load_Reward_Int(index);
                }
            });

            ad.show(activity, rewardItem -> {
                loading.dismiss();
                preLoad.Load_Reward_Int(index);
                handler.onRewarded();
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
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                Log.e(TAG, "Rewarded Int ads failed to show. Error : " + adError.getMessage());
                                handler.onFailedToShow(adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                loading.dismiss();
                                handler.onShowed();
                                Log.d(TAG, "Rewarded Int ads showed.");
                                preLoad.Load_Reward_Int(index);
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                handler.onDismissed();
                                Log.d(TAG, "Rewarded Int ads dismissed.");
                                preLoad.Load_Reward_Int(index);
                            }
                        });

                        ad.show(activity, rewardItem -> {
                            loading.dismiss();
                            preLoad.Load_Reward_Int(index);
                            handler.onRewarded();
                        });
                    }
                });
    }

    public void showAppOpenAds(@NonNull Activity activity, int index, @NonNull RequestHandler handler) {
        if (adUnitResolver == null || adUnitResolver.isDisabled()) {
            handler.onSuccess();
            return;
        }

        loading.show(activity, loaderTintColor);

        if (preLoad.mAppOpen != null) {
            AppOpenAd ad = preLoad.mAppOpen;
            preLoad.mAppOpen = null;
            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loading.dismiss();
                    preLoad.Load_App_Open(index);
                    Log.d(TAG, "App Open ads dismissed.");
                    handler.onSuccess();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loading.dismiss();
                    preLoad.Load_App_Open(index);
                    Log.e(TAG, "App Open ads failed to show. Error : " + adError.getMessage());
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
                        Log.e(TAG, "App Open ads failed to load. Error : " + loadAdError.getMessage());
                        handler.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loading.dismiss();
                                preLoad.Load_App_Open(index);
                                Log.d(TAG, "App Open ads dismissed.");
                                handler.onSuccess();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                loading.dismiss();
                                preLoad.Load_App_Open(index);
                                Log.e(TAG, "App Open ads failed to show. Error : " + adError.getMessage());
                                handler.onError(adError.getMessage());
                            }
                        });
                        appOpenAd.show(activity);
                    }
                });
    }

    public void showBannerAds(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled())
            return;

        if (container.getChildCount() > 0) {
            View v = container.getChildAt(0);
            if (v instanceof AdView)
                ((AdView) v).destroy();
            container.removeAllViews();
            container.setVisibility(View.GONE);
        }

        AdView adView = new AdView(container.getContext());
        container.addView(adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Banner ad failed to load: " + loadAdError.getMessage());
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdImpression() {
                Log.i(TAG, "Banner ad impressed.");
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                Log.i(TAG, "Banner ad loaded.");
                container.setVisibility(View.VISIBLE);
                MakeAnim(container.getContext(), container, AnimType.IN);
                super.onAdLoaded();
            }
        });

        container.post(() -> {
            int width = container.getWidth();
            if (width == 0)
                width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int adWidth = Math.round(width / Resources.getSystem().getDisplayMetrics().density);
            AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(application, adWidth);

            adView.setAdUnitId(adUnitResolver.bannerId(index));
            adView.setAdSize(adSize);
            adView.loadAd(new AdRequest.Builder().build());
        });
    }

    public void showNativeAds(int index, @NonNull LinearLayout container, @NonNull NativeAdsSize size) {
        if (adUnitResolver == null || adUnitResolver.isDisabled())
            return;

        if (size == NativeAdsSize.SMALL) {
            showNativeAdsSmall(index, container);
        } else if (size == NativeAdsSize.MEDIUM) {
            showNativeAdsMedium(index, container);
        }
    }

    private void showNativeAdsSmall(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled())
            return;

        try {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(container.getContext())
                    .inflate(R.layout.small_native_ad_layout, container, false);
            container.removeAllViews();
            container.addView(layout);
            TemplateView templateView = layout.findViewById(R.id.my_template);

            AdLoader loader = new AdLoader.Builder(container.getContext(), adUnitResolver.nativeId(index))
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        templateView.setVisibility(View.VISIBLE);
                        MakeAnim(container.getContext(), templateView, AnimType.IN);
                        templateView.setStyles(style);
                        templateView.setNativeAd(nativeAd);

                        templateView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(@NonNull View view) {

                            }

                            @Override
                            public void onViewDetachedFromWindow(@NonNull View view) {
                                nativeAd.destroy();
                                Log.d(TAG, "Native ad view detached. Native destroyed.");
                            }
                        });
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            templateView.setVisibility(View.GONE);
                            Log.e(TAG, "Native ad small failed to load: " + loadAdError.getMessage());
                        }
                    })
                    .build();
            loader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            Log.e(TAG, "Error showing native ads: " + e.getMessage());
        }
    }

    private void showNativeAdsMedium(int index, @NonNull LinearLayout container) {
        if (adUnitResolver == null || adUnitResolver.isDisabled())
            return;

        try {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(container.getContext())
                    .inflate(R.layout.medium_native_ad_layout, container, false);
            container.removeAllViews();
            container.addView(layout);
            TemplateView templateView = layout.findViewById(R.id.my_template_medium);

            AdLoader loader = new AdLoader.Builder(container.getContext(), adUnitResolver.nativeId(index))
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder().build();
                        templateView.setVisibility(View.VISIBLE);
                        MakeAnim(container.getContext(), templateView, AnimType.IN);
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
                            Log.e(TAG, "Native ad Medium failed to load: " + loadAdError.getMessage());
                        }
                    })
                    .build();
            loader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            Log.e(TAG, "Error showing native ads: " + e.getMessage());
        }
    }

    private void MakeAnim(@NonNull Context context, @NonNull View view, @NonNull AnimType type) {
        Animation anim;
        if (type == AnimType.IN) {
            anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        } else if (type == AnimType.OUT) {
            anim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        } else {
            Log.e(TAG, "Animation type is incorrect.");
            return;
        }

        anim.setDuration(1000);
        view.setAnimation(anim);
    }

    private enum AnimType {
        IN, OUT;
    }
}
