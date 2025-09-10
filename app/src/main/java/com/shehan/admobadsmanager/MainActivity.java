package com.shehan.admobadsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.Class.AdsManagerInitializer;
import com.shehan.adsmanager.Class.AdsUnit;
import com.shehan.adsmanager.Class.RequestHandler;
import com.shehan.adsmanager.Modals.AdMobIds;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CardView btnAppOpen, btnInt, btnReward, btnRewardInt, btnBanner, btnNative, btnNativeMedium, btnNext;
    private AdsManager manager;

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

        AdsManagerInitializer initializer = AdsManagerInitializer.getInstance(
                new AdMobIds(
                        getString(R.string.admob_app_id),
                        Arrays.asList(getString(R.string.admob_interstitial_1), getString(R.string.admob_interstitial_2)),
                        Arrays.asList(getString(R.string.admob_banner), getString(R.string.admob_banner)),
                        Arrays.asList(getString(R.string.admob_app_open), getString(R.string.admob_app_open)),
                        Arrays.asList(getString(R.string.admob_reward), getString(R.string.admob_reward)),
                        Arrays.asList(getString(R.string.admob_native_1), getString(R.string.admob_native_2)),
                        List.of("")
                )
        );

        manager = AdsManager.getInstance(this, initializer, true);
        manager.preLoad(AdsUnit.APP_OPEN, 0);

        btnAppOpen.setOnClickListener(v-> {
            manager.showAppOpenAds(0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.APP_OPEN, 0);
                    Toast.makeText(MainActivity.this, "App Open Showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "App Open show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnInt.setOnClickListener(v-> {
            manager.showInterstitialAds(0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.INTERSTITIAL, 1);
                    Toast.makeText(MainActivity.this, "Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Interstitial ads 2nd loading...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(()-> {
                        manager.showInterstitialAds(1, new RequestHandler() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(MainActivity.this, "Interstitial ads 2 showed.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }, 5000);
                }
            });
        });

        btnReward.setOnClickListener(v-> {
            manager.showRewardAds(0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD, 0);
                    Toast.makeText(MainActivity.this, "Reward ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Reward ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRewardInt.setOnClickListener(v-> {
            manager.showRewardIntAds(0, new RequestHandler() {
                @Override
                public void onSuccess() {
                    manager.preLoad(AdsUnit.REWARD_INT, 0);
                    Toast.makeText(MainActivity.this, "Reward Interstitial ads showed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Reward Interstitial ads show failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBanner.setOnClickListener(v-> {
            manager.showBannerAds(0, findViewById(R.id.main_bannerContainer));
        });

        btnNative.setOnClickListener(v-> {
            manager.showNativeAds(0, findViewById(R.id.main_nativeContainer));
        });

        btnNativeMedium.setOnClickListener(v-> {
            manager.showNativeAdsMedium(0, findViewById(R.id.main_nativeMediumContainer));
        });

        btnNext.setOnClickListener(v-> {
            startActivity(new Intent(this, TestActivity.class));
        });
    }
}
