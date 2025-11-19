package com.shehan.admobadsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.callback.RequestHandler;
import com.shehan.adsmanager.callback.RewardRequestHandler;
import com.shehan.adsmanager.enums.NativeAdsSize;

public class MainActivity extends AppCompatActivity {

    CardView btnAppOpen, btnInt, btnReward, btnRewardInt, btnBanner, btnNative, btnNativeMedium, btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        btnAppOpen = findViewById(R.id.main_btnAppOpen);
        btnInt = findViewById(R.id.main_btnInt);
        btnReward = findViewById(R.id.main_btnReward);
        btnRewardInt = findViewById(R.id.main_btnRewardInt);
        btnBanner = findViewById(R.id.main_btnBanner);
        btnNative = findViewById(R.id.main_btnNative);
        btnNativeMedium = findViewById(R.id.main_btnNativeMedium);
        btnNext = findViewById(R.id.main_btnNext);

        btnAppOpen.setOnClickListener(v-> {
            AdsManager.getInstance().showAppOpenAds(this, 0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "App Open Ad Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, "App Open Ad Show Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnInt.setOnClickListener(v-> {
            AdsManager.getInstance().showInterstitialAds(this, 0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Interstitial Ad Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, "Interstitial ad show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnReward.setOnClickListener(v-> {
            AdsManager.getInstance().showRewardAds(this, 0, new RewardRequestHandler() {
                @Override
                public void onShowed() {
                    Toast.makeText(MainActivity.this, "Reward Ads Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDismissed() {
                    Toast.makeText(MainActivity.this, "Reward Ads Dismissed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded() {
                    Toast.makeText(MainActivity.this, "User Rewarded.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedToShow(String error) {
                    Toast.makeText(MainActivity.this, "Reward Ads Failed to Show.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, "Reward Ads Show Error.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRewardInt.setOnClickListener(v-> {
            AdsManager.getInstance().showRewardIntAds(this, 0, new RewardRequestHandler() {
                @Override
                public void onShowed() {
                    Toast.makeText(MainActivity.this, "Rewarded Int Ads Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDismissed() {
                    Toast.makeText(MainActivity.this, "Rewarded Int Ads Dismissed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded() {
                    Toast.makeText(MainActivity.this, "User Rewarded.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedToShow(String error) {
                    Toast.makeText(MainActivity.this, "Rewarded Int Failed to Show.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, "Rewarded Int Ads Error.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBanner.setOnClickListener(v-> {
            AdsManager.getInstance().showBannerAds(0, findViewById(R.id.main_bannerContainer));
        });

        btnNative.setOnClickListener(v-> {
            AdsManager.getInstance().showNativeAds(0, findViewById(R.id.main_nativeContainer), NativeAdsSize.SMALL);
        });

        btnNativeMedium.setOnClickListener(v-> {
            AdsManager.getInstance().showNativeAds(0, findViewById(R.id.main_nativeMediumContainer), NativeAdsSize.MEDIUM);
        });

        btnNext.setOnClickListener(v-> {
            startActivity(new Intent(this, TestActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        AdsManager.getInstance().destroyAds();
        super.onDestroy();
    }
}
