# Custom AdMob Ads Manager Library

Easily integrate Google AdMob into your Android application with just a few lines of code.

---

## 🚀 Key Features

* Pre-load ads for a seamless user experience
* **NEW in v2.0.1:** Use `Context` instead of `AppCompatActivity`

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
implementation 'com.github.shehan-077:Admob-Ads-Manager:2.0.1'
implementation 'com.google.android.gms:play-services-ads:24.5.0'
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

```java
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
```

### With Single Ad Unit ID

```java
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
```

---

## 🧠 Create AdsManager

```java
AdsManager manager = AdsManager.getInstance(this, initializer, true);
```

* `this`: your `Context`
* `initializer`: AdMob config instance
* `true`: enable ads (`false` to disable)

---

## ⚡ Pre-load Ads

```java
// Pre-load Interstitial Ad at index 0
manager.preLoad(AdsUnit.INTERSTITIAL, 0);

// Pre-load any ad unit type
manager.preLoad(AdsUnit.REWARD, 1); // index 1 for second ad unit
```

---

## ▶️ Show Ads

### App Open Ad

```java
manager.showAppOpenAds(0, new RequestHandler() {
    @Override
    public void onSuccess() {
        manager.preLoad(AdsUnit.APP_OPEN, 0);
    }
    @Override
    public void onError() {
        // handle error
    }
});
```

### Banner Ad

```java
manager.showBannerAds(0, findViewById(R.id.banner_container));
```

### Native Ad (Small)

```java
manager.showNativeAds(0, findViewById(R.id.native_ad_container));
```

### Native Ad (Medium)

```java
manager.showNativeAdsMedium(0, findViewById(R.id.medium_native_container));
```

---

## 🧾 Explanation

* **`AdsManagerInitializer`**: Holds your app and ad unit IDs
* **`AdsManager`**: Core class to preload, show, and manage ads
* **`true/false` flag**: Enable or disable ads globally

---

## 💡 Usage Tips

* Always preload ads to reduce display delay
* Handle all ad events for reliability
* Use index-based loading for region-based or fallback logic
* Follow AdMob policy strictly to avoid account issues

---

## 🆕 What's New in 2.0.1

* ✅ **Kotlin Friendly** – Use Context instead of AppCompatActivity (V 2.0.1)
* ✅ **Multiple Ad Unit ID Support** – Add and select multiple ad unit IDs per ad type (V 2.0.0)
* 🧹 **Simplified Preload** – Removed separate preload methods for each unit (V 2.0.0)

---

## 🎉 Enjoy!

Feel free to use this library to simplify and streamline AdMob integration.
