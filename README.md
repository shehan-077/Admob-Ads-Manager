# Custom AdMob Ads Manager Library

Easily integrate Google AdMob into your Android application with just a few lines of code.

---

## 🚀 Key Features

* Pre-load ads for a seamless user experience.
* Retry automatically when ads fail to load.
* Built-in **Lottie loading screen** during ad loading.
* Customizable loading color.
* Automatic **test ads in debug builds.**
* Automatic **real ads in release builds.**
* Built-in **GDPR Consent (UMP SDK)** support.
* Privacy options form support.
* Singleton-based global initialization.

---

## 📺 Supported Ad Types

* Interstitial Ads
* App Open Ads
* Rewarded Ads
* Rewarded Interstitial Ads
* Native Ads (Small & Medium)
* Banner Ads

---

## 🧩 Getting Started

### Step 1: Add JitPack Repository

Add the following to your `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
    }
}
```

### Step 2: Add Dependencies

```gradle
implementation 'com.github.shehan-077:Admob-Ads-Manager:4.0.0'
implementation 'com.google.android.gms:play-services-ads:25.0.0'
implementation 'com.airbnb.android:lottie:6.7.1'
```

### Step 3: Configure Permissions & App ID

#### Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### App ID in Manifest

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID" />
```

---

## 🔧 Initialize AdMob

### With Multiple Ad Unit IDs

Now (since v3.0.0) you initialize AdMob in your Application class:

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AdsManagerInitializer initializer = AdsManagerInitializer.getInstance(
                new AdMobIds(
                        "ADMOB_APP_ID",
                        Arrays.asList("INTERSTITIAL_AD_1", "INTERSTITIAL_AD_2"),
                        Arrays.asList("BANNER_AD_1", "BANNER_AD_2"),
                        Arrays.asList("APP_OPEN_AD_1", "APP_OPEN_AD_2"),
                        Arrays.asList("REWARD_AD_1", "REWARD_AD_2"),
                        Arrays.asList("NATIVE_AD_1", "NATIVE_AD_2"),
                        Arrays.asList("REWARD_INTERSTITIAL_AD_1", "REWARD_INTERSTITIAL_AD_2")
                )
        );

        AdsManager.init(this, initializer, AdsStatus.ENABLED);
        AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.primary));
    }
}
```

### With Single Ad Unit ID

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AdsManagerInitializer initializer = AdsManagerInitializer.getInstance(
                new AdMobIds(
                        "ADMOB_APP_ID",
                        List.of("INTERSTITIAL_AD_ID"),
                        List.of("BANNER_AD_ID"),
                        List.of("APP_OPEN_AD_ID"),
                        List.of("REWARD_AD_ID"),
                        List.of("NATIVE_AD_ID"),
                        List.of("REWARD_INTERSTITIAL_AD_ID")
                )
        );

        AdsManager.init(this, initializer, AdsStatus.ENABLED);
        AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.primary));
    }
}
```

---

## 🔐 GDPR Consent Flow (NEW in 4.0.0)

Before loading ads, you must request user consent.

Call this in your Launcher Activity (SplashActivity / MainActivity).

```java
AdsManager.getInstance().startConsentFlow(
        SplashScreen.this,
        true,
        null,
        new ConsentRequestHandler() {
            @Override
            public void onConsentReady(boolean canRequestAds) {

                if (canRequestAds) {

                    AdsManager.getInstance().preLoad(AdsUnit.INTERSTITIAL,0);
                    AdsManager.getInstance().preLoad(AdsUnit.REWARD,0);
                    AdsManager.getInstance().preLoad(AdsUnit.APP_OPEN,0);
                    AdsManager.getInstance().preLoad(AdsUnit.REWARD_INT,0);

                }

            }

            @Override
            public void onConsentError(String error) {

            }
        }
);
```

* `SplashScreen.this` – Your Activity context.
* `true` – Enable test mode (use `false` in production).
* `null` – Optional test device hashed ID for AdMob testing.

Ads will only load after user consent is ready.

---

## 🔐 Privacy Options Form

If required, you can allow users to update their consent.

```java
if (AdsManager.getInstance().isPrivacyOptionRequired()) {

    AdsManager.getInstance().showPrivacyOptionForm(this, new ConsentRequestHandler() {
        @Override
        public void onConsentReady(boolean canRequestAds) {
    
        }
    
        @Override
        public void onConsentError(String error) {
    
        }
    });

}
```

---

## ⚡ Pre-load Ads

```java
// Pre-load Interstitial Ad at index 0
AdsManager.getInstance().preLoad(AdsUnit.INTERSTITIAL, 0);

// Pre-load App Open Ad at index 0
AdsManager.getInstance().preLoad(AdsUnit.APP_OPEN, 0);

// Pre-load Rewarded Ad at index 0
AdsManager.getInstance().preLoad(AdsUnit.REWARD, 0);

// Pre-load Rewarded Interstitial Ad at index 0
AdsManager.getInstance().preLoad(AdsUnit.REWARD_INT, 0);
```

---

## Built-in Lottie Loading Screen (V 3.0.0 above)

```java
AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.primary));
```

* `this`: your `Context`
* `R.color.primary`: Customize the color you want to show
* Optional – if not defined, the default color (white) will be used.

---
## 🧩 Complete Example – Application Class



* `this`: your `Context`

---

## ▶️ Show Ads

### App Open Ad

```java
AdsManager.getInstance().showAppOpenAds(this, 0, new RequestHandler() {
    @Override
    public void onSuccess() {
        // App Open Ad Showed.
    }

    @Override
    public void onError(String error) {
        // Handle error.
    }
});
```
* `this`: your `Context`
* `0`: Index number of AdMob ad unit.

### Interstitial Ad

```java
AdsManager.getInstance().showInterstitialAds(this, 0, new RequestHandler() {
    @Override
    public void onSuccess() {
        // Interstitial Ad Showed.
    }

    @Override
    public void onError(String error) {
        // Handle error.
    }
});
```

* `this`: your `Context`
* `0`: Index number of AdMob ad unit.

### Rewarded Ad

```java
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
```

* `this`: your `Context`
* `0`: Index number of AdMob ad unit.

### Rewarded Interstitial Ad

```java
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
```
* `this`: your `Context`
* `0`: Index number of AdMob ad unit.

### Banner Ad

```java
AdsManager.getInstance().showBannerAds(0, findViewById(R.id.main_bannerContainer));
```

### Native Ad (Small)

```java
AdsManager.getInstance().showNativeAds(0, findViewById(R.id.main_nativeContainer), NativeAdsSize.SMALL);
```

### Native Ad (Medium)

```java
AdsManager.getInstance().showNativeAds(0, findViewById(R.id.main_nativeMediumContainer), NativeAdsSize.MEDIUM);
```

* `0`: Index number of AdMob ad unit.
* `findViewById(R.id.main_bannerContainer)`: Banner container view
* `findViewById(R.id.main_nativeContainer)`: Native small ad container view
* `findViewById(R.id.main_nativeMediumContainer)`: Native medium ad container view

---

## 🧾 Explanation

* **`AdsManagerInitializer`**: Stores all AdMob Ad Unit IDs.
* **`AdsManager`**: Core class to preloading, ad loading, showing ads, GDPR consent, and privacy options.
* **`AdsStatus`**: Control whether ads are live, disabled, testing, or in hybrid mode.
* **`NativeAdsSize`**: Enum for choosing native ad template size (SMALL or MEDIUM).
* **`LoadingOverlay`**: Built-in loading animation (Lottie-based) shown automatically during ad loading.

---

## 💡 Usage Tips

* Always preload ads to reduce delay.
* Call consent flow before requesting ads.
* Handle all ad callbacks.
* Use multiple ad unit IDs for fallback logic.
* Follow AdMob policy strictly.

---

## 🆕 What's New in 4.0.0

* ✅ **Built-in GDPR Consent (UMP SDK)**
* 🔐 **Privacy Options Form Support**
* ⚡  **Ads load only after user consent**
* 🎨️ **Improved Lottie Loading Screen**
* ⚙️ **Better initialization flow**
* 🧠 **Improved preload management**

---


## 🎉 Enjoy!

Feel free to use this library to simplify and streamline AdMob integration.
