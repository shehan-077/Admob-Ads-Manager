package com.shehan.admobadsmanager;

import android.app.Application;

import androidx.core.content.ContextCompat;

import com.shehan.adsmanager.AdsManager;
import com.shehan.adsmanager.classes.AdsManagerInitializer;
import com.shehan.adsmanager.classes.AdsUnit;
import com.shehan.adsmanager.modules.AdMobIds;
import com.shehan.adsmanager.enums.AdsStatus;

import java.util.Arrays;
import java.util.List;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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

        AdsManager.init(this, initializer, AdsStatus.TESTING);
        AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.white));

        AdsManager.getInstance().preLoad(AdsUnit.INTERSTITIAL, 0);
    }
}
