package com.shehan.admobadsmanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.Class.AdsUnit;
import com.shehan.adsmanager.Class.RequestHandler;

public class TestActivity extends AppCompatActivity {

    AdsManager manager;
    CardView btnAppOpen, btnInt, btnReward, btnRewardInt, btnBanner, btnNative, btnNativeMedium;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        manager = AdsManager.getInstance(this);

        btnAppOpen = findViewById(R.id.main_btnAppOpen);
        btnInt = findViewById(R.id.main_btnInt);
        btnReward = findViewById(R.id.main_btnReward);
        btnRewardInt = findViewById(R.id.main_btnRewardInt);
        btnBanner = findViewById(R.id.main_btnBanner);
        btnNative = findViewById(R.id.main_btnNative);
        btnNativeMedium = findViewById(R.id.main_btnNativeMedium);

        btnAppOpen.setOnClickListener(v-> {
            manager.showAppOpenAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.APP_OPEN);
                    Toast.makeText(TestActivity.this, "App Open Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(TestActivity.this, "App Open show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnInt.setOnClickListener(v-> {
            manager.showInterstitialAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.INTERSTITIAL);
                    Toast.makeText(TestActivity.this, "Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(TestActivity.this, "Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnReward.setOnClickListener(v-> {
            manager.showRewardAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD);
                    Toast.makeText(TestActivity.this, "Reward ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(TestActivity.this, "Reward ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRewardInt.setOnClickListener(v-> {
            manager.showRewardIntAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD_INT);
                    Toast.makeText(TestActivity.this, "Reward Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(TestActivity.this, "Reward Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBanner.setOnClickListener(v-> {
            manager.showBannerAds(findViewById(R.id.main_bannerContainer));
        });

        btnNative.setOnClickListener(v-> {
            manager.showNativeAds(findViewById(R.id.main_nativeContainer));
        });

        btnNativeMedium.setOnClickListener(v-> {
            manager.showNativeAdsMedium(findViewById(R.id.main_nativeMediumContainer));
        });
    }
}
