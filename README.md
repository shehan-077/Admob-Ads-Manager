# Custom AdMob Ads Manager Library

Easily integrate Google AdMob into your Android application with just a few lines of code.

---

## 🚀 Key Features

* Pre-load ads for a seamless user experience.
* Retry automatically when ads fail to load.
* Built-in Lottie loading screen with customizable color - shown automatically during the ad load.
* No need to manually provide AdMob test ad units — just set `AdsStatus.TESTING` to use predefined test ads.
* Automatically shows **test ads in debug builds** and **real ads in release builds** with `AdsStatus.HYBRID`

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
implementation 'com.github.shehan-077:Admob-Ads-Manager:3.1.2'
implementation 'com.google.android.gms:play-services-ads:24.9.0'
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
    }
}
```

---

## 🧠 Create AdsManager

```java
AdsManager.init(this, initializer, AdsStatus.HYBRID);
```

* `this`: your `Application`
* `initializer`: AdMob config instance
* `AdsStatus.HYBRID`: enabled test ads when debugging and switch to real ads when released. (`AdsStatus.DISABLED` to disable and `AdsStatus.TESTING` to use test ads.)

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

## Built-in Lottie Loading Screen (V 3.0.0)

```java
AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.primary));
```

* `this`: your `Context`
* `R.color.primary`: Customize the color you want to show
* Optional – if not defined, the default color (white) will be used.

---
## 🧩 Complete Example – Application Class

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

        AdsManager.getInstance().preLoad(AdsUnit.INTERSTITIAL, 0);
    }
}
```

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

* **`AdsManagerInitializer`**: Holds your app and ad unit IDs
* **`AdsManager`**: Core class to preload, show, and manage ads
* **`AdsStatus`**: Control whether ads are live, disabled, testing, or in hybrid mode
* **`NativeAdsSize`**: Enum for choosing native ad template size (SMALL or MEDIUM)
* **`LoadingOverlay`**: Built-in loading animation (Lottie-based) shown automatically during ad loading

---

## 💡 Usage Tips

* Always preload ads to reduce display delay (Interstitial, Reward, and Reward Interstitial Ads only).
* Handle all ad events for reliability.
* Use index-based loading for region-based or fallback logic.
* Follow AdMob policy strictly to avoid account issues.
* Use `AdsStatus.HYBRID` during development - Ads manager shows testing ads and after production, ads will automatically switch to real ad units defined by you.

---

## 🆕 What's New in 3.0.0 (Above)

* ✅ **Built-in Lottie Loading Screen** – shown automatically during ad load
* 🎨 **Customizable Loading Color** – adapt to your app theme
* ⚙️ **`AdsStatus.TESTING` Mode** – automatically uses predefined AdMob test units
* ⚙️ **`AdsStatus.HYBRID` Mode** – automatically uses predefined AdMob test units during development period and after release automatically switches to real ad units defined by you.
* 🧠 **Singleton Initialization** – initialize once globally, use anywhere
* 🧩 **Unified Native Ads API** – select size via NativeAdsSize.SMALL or NativeAdsSize.MEDIUM
* 💡 **Cleaner API** – consistent with Kotlin and Java apps

---


## 🎉 Enjoy!

Feel free to use this library to simplify and streamline AdMob integration.
