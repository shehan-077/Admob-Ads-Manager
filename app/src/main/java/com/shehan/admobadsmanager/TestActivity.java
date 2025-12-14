package com.shehan.admobadsmanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.callback.RequestHandler;
import com.shehan.adsmanager.callback.RewardRequestHandler;
import com.shehan.adsmanager.enums.NativeAdsSize;

public class TestActivity extends AppCompatActivity {

    CardView btnAppOpen, btnInt, btnReward, btnRewardInt, btnBanner, btnNative, btnNativeMedium;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnAppOpen = findViewById(R.id.main_btnAppOpen);
        btnInt = findViewById(R.id.main_btnInt);
        btnReward = findViewById(R.id.main_btnReward);
        btnRewardInt = findViewById(R.id.main_btnRewardInt);
        btnBanner = findViewById(R.id.main_btnBanner);
        btnNative = findViewById(R.id.main_btnNative);
        btnNativeMedium = findViewById(R.id.main_btnNativeMedium);

        btnAppOpen.setOnClickListener(v -> {
            AdsManager.getInstance().showAppOpenAds(this, 1, new RequestHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(TestActivity.this, "App Open Ad Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(TestActivity.this, "App Open Ad Show Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnInt.setOnClickListener(v -> {
            AdsManager.getInstance().showInterstitialAds(this, 1, new RequestHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(TestActivity.this, "Interstitial Ad Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(TestActivity.this, "Interstitial ad show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnReward.setOnClickListener(v -> {
            AdsManager.getInstance().showRewardAds(this, 1, new RewardRequestHandler() {
                @Override
                public void onShowed() {
                    Toast.makeText(TestActivity.this, "Reward Ads Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDismissed() {
                    Toast.makeText(TestActivity.this, "Reward Ads Dismissed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded() {
                    Toast.makeText(TestActivity.this, "User Rewarded.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedToShow(String error) {
                    Toast.makeText(TestActivity.this, "Reward Ads Failed to Show.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(TestActivity.this, "Reward Ads Show Error.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRewardInt.setOnClickListener(v -> {
            AdsManager.getInstance().showRewardIntAds(this, 0, new RewardRequestHandler() {
                @Override
                public void onShowed() {
                    Toast.makeText(TestActivity.this, "Rewarded Int Ads Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDismissed() {
                    Toast.makeText(TestActivity.this, "Rewarded Int Ads Dismissed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded() {
                    Toast.makeText(TestActivity.this, "User Rewarded.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedToShow(String error) {
                    Toast.makeText(TestActivity.this, "Rewarded Int Failed to Show.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(TestActivity.this, "Rewarded Int Ads Error.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBanner.setOnClickListener(v -> {
            AdsManager.getInstance().showBannerAds(1, findViewById(R.id.main_bannerContainer));
        });

        btnNative.setOnClickListener(v -> {
            AdsManager.getInstance().showNativeAds(1, findViewById(R.id.main_nativeContainer), NativeAdsSize.SMALL);
        });

        btnNativeMedium.setOnClickListener(v -> {
            AdsManager.getInstance().showNativeAds(1, findViewById(R.id.main_nativeMediumContainer),
                    NativeAdsSize.MEDIUM);
        });
    }

    @Override
    protected void onDestroy() {
        AdsManager.getInstance().destroyAds();
        super.onDestroy();
    }
}
