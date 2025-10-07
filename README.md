# Custom AdMob Ads Manager Library

Easily integrate Google AdMob into your Android application with just a few lines of code.

---

## 🚀 Key Features

* Pre-load ads for a seamless user experience.
* **NEW in v3.0.0:** Built-in Lottie loading screen with customizable color.
* **NEW in v3.0.0:** No need to manually provide AdMob test ad units — just set `AdsStatus.TESTING` to use predefined test ads.
* **NEW in v3.0.0:** Build-in Lottie Loading Screen - shown automatically during the ad load.

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
implementation 'com.github.shehan-077:Admob-Ads-Manager:3.0.1'
implementation 'com.google.android.gms:play-services-ads:24.6.0'
implementation 'com.airbnb.android:lottie:6.6.9'
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
AdsManager.init(this, initializer, AdsStatus.ENABLED);
```

* `this`: your `Application`
* `initializer`: AdMob config instance
* `AdsStatus.ENABLED`: enabled ads (`AdsStatus.DISABLED` to disable and `AdsStatus.TESTING` to use test ads)

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

## Build-in Lottie Loading Screen (V 3.0.0)

```java
AdsManager.getInstance().setLoadingColor(ContextCompat.getColor(this, R.color.primary));
```

* `this`: your `Context`
* `R.color.primary`: Customize color you want to show
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

### Rewarded Ad

```java
AdsManager.getInstance().showRewardAds(this, 0, new RequestHandler() {
    @Override
    public void onSuccess() {
        // Reward ad showed.
    }

    @Override
    public void onError(String error) {
        // Handle error.
    }
});
```

### Rewarded Interstitial Ad

```java
AdsManager.getInstance().showRewardIntAds(this,0, new RequestHandler() {
    @Override
    public void onSuccess() {
        // Reward Interstitial ads showed.
    }

    @Override
    public void onError(String error) {
        // Handle error.
    }
});
```

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

---

## 🧾 Explanation

* **`AdsManagerInitializer`**: Holds your app and ad unit IDs
* **`AdsManager`**: Core class to preload, show, and manage ads
* **`AdsStatus`** Control whether ads are live, disabled, or in testing mode
* **`NativeAdsSize`** Enum for choosing native ad template size (SMALL or MEDIUM)
* **`LoadingOverlay`** Build-in loading animation (Lottie-based) shown automatically during ad loading

---

## 💡 Usage Tips

* Always preload ads to reduce display delay
* Handle all ad events for reliability
* Use index-based loading for region-based or fallback logic
* Follow AdMob policy strictly to avoid account issues
* Use `AdsStatus.TESTING` during development - no need to set up test ad Ids

---

## 🆕 What's New in 3.0.0

* ✅ **Built-in Lottie Loading Screen** – shown automatically during ad load
* 🎨 **Customizable Loading Color** – adapt to your app theme
* ⚙️ **`AdsStatus.TESTING` Mode** – automatically uses predefine AdMob test units
* 🧠 **Singleton Initialization** — initialize once globally, use anywhere
* 🧩 **Unified Native Ads API** — select size via NativeAdsSize.SMALL or NativeAdsSize.MEDIUM
* 💡 **Cleaner API** — consistent with Kotlin and Java apps

---

## 🧩 What's New in 3.0.1

* 🐞 Fixed: Missing `handle.onSuccess()` callback when ads were disabled for **App Open** and **Rewarded Interstitial** ads.
* ⚡  Improved stability when ads are disabled or unavailable.
* ✅ Compatible with existing integration — no API changes required.

---

## 🎉 Enjoy!

Feel free to use this library to simplify and streamline AdMob integration.
