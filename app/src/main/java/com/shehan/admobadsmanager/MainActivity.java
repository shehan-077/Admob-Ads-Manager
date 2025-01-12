package com.shehan.admobadsmanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.Class.AdsManagerInitializer;
import com.shehan.adsmanager.Class.AdsUnit;
import com.shehan.adsmanager.Class.RequestHandler;
import com.shehan.adsmanager.Modals.AdMobIds;

public class MainActivity extends AppCompatActivity {

    CardView btnAppOpen, btnInt, btnReward, btnRewardInt, btnBanner, btnNative;
    private AdsManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAppOpen = findViewById(R.id.main_btnAppOpen);
        btnInt = findViewById(R.id.main_btnInt);
        btnReward = findViewById(R.id.main_btnReward);
        btnRewardInt = findViewById(R.id.main_btnRewardInt);
        btnBanner = findViewById(R.id.main_btnBanner);
        btnNative = findViewById(R.id.main_btnNative);

        AdsManagerInitializer initializer = AdsManagerInitializer.getInstance(
                new AdMobIds(
                        getString(R.string.admob_app_id),
                        getString(R.string.admob_interstitial),
                        getString(R.string.admob_banner),
                        getString(R.string.admob_app_open),
                        getString(R.string.admob_reward),
                        getString(R.string.admob_native),
                        ""
                )
        );

        manager = AdsManager.getInstance(this, initializer, true);
        manager.preLoad(AdsUnit.ALL);

        btnAppOpen.setOnClickListener(v-> {
            manager.showAppOpenAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.APP_OPEN);
                    Toast.makeText(MainActivity.this, "App Open Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "App Open show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnInt.setOnClickListener(v-> {
            manager.showInterstitialAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.INTERSTITIAL);
                    Toast.makeText(MainActivity.this, "Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnReward.setOnClickListener(v-> {
            manager.showRewardAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD);
                    Toast.makeText(MainActivity.this, "Reward ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Reward ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRewardInt.setOnClickListener(v-> {
            manager.showRewardIntAds(new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD_INT);
                    Toast.makeText(MainActivity.this, "Reward Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Reward Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBanner.setOnClickListener(v-> {
            manager.showBannerAds(findViewById(R.id.main_bannerContainer));
        });

        btnNative.setOnClickListener(v-> {
            manager.showNativeAds(findViewById(R.id.main_nativeContainer));
        });
    }
}
