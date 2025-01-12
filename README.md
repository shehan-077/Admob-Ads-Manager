* # AdsSystem Library
* Easily integrate Google AdMob into your Android application with just a few lines of code.

* ## Key Features
* Pre-loading ads for a seamless user experience.

* ## Supported Ad Types
* - Interstitial Ads
* - App Open Ads
* - Reward Ads
* - Reward Interstitial Ads
* - Native Ads
* - Banner Ads

* ## Getting Started

* ### Step 1: Add JitPack Repository
* Add the following to your `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://www.jitpack.io") }
        mavenCentral()
    }
}
```

* ### Step 2: Add Dependencies
* Add the following dependencies to your `build.gradle` file:

```gradle
implementation 'com.github.shehan-077:Admob-Ads-Manager:-SNAPSHOT'
implementation "com.google.android.gms:play-services-ads:23.5.0"
```

* ### Step 3: Configure Permissions and App ID

* #### Add Permissions
* Include these permissions in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

* #### Set Your AdMob App ID
* Add your AdMob app ID in the `AndroidManifest.xml`:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID" />
```

* ### Step 4: Initialize AdMob
* Initialize the AdsManager with your AdMob app IDs:

```java
AdsManagerInitializer initializer = AdsManagerInitializer.getInstance(
     new AdMobIds(
         "ADMOB_APP_ID", 
         "ADMOB_INTERSTITIAL_AD_ID", 
         "ADMOB_BANNER_AD_ID", 
         "ADMOB_APP_OPEN_AD_ID",
         "ADMOB_REWARD_AD_ID", 
         "ADMOB_NATIVE_AD_ID", 
         "ADMOB_REWARD_INTERSTITIAL_AD_ID"
     )
 );
```

* ### Step 5: Create AdsManager
* Create an `AdsManager` instance to manage ads in your app:

```java
AdsManager manager = AdsManager.getInstance(this, initializer, true);
```

* **`this`**: Pass your activity or context.
* **`initializer`**: The `AdsManagerInitializer` instance created earlier.
* **`true`**: Enables ads. Use `false` to disable all ads.

* ### Step 6: Pre-load Ads
* Pre-load ads to ensure smooth display:

```java
// Preload all ads
manager.preLoad(AdsUnit.ALL);

// Preload only interstitial ads
manager.preLoad(AdsUnit.INTERSTITIAL);

// Preload specific ad units
manager.preLoad(AdsUnit."AD_UNIT_NAME");
```

* ### Step 7: Show Ads

* #### Show App Open Ads
```java
manager.showAppOpenAds(new RequestHandler() {
    @Override
    public void onSuccess() {
        manager.preLoad(AdsUnit.APP_OPEN); // Preload after showing
        // Handle success logic
    }

    @Override
    public void onError() {
        // Handle error logic
    }
});
```

* #### Show Banner Ads
```java
manager.showBannerAds(findViewById(R.id."LINEARLAYOUT_ID"));
```

* #### Show Native Ads
```java
manager.showNativeAds(findViewById(R.id."LINEARLAYOUT_ID"));
```

* ## Explanation
* **`AdsManagerInitializer`**: Initializes the AdMob app and ad unit IDs.
* **`AdsManager`**: Manages ads, including pre-loading, showing, and handling requests.
* **`true/false` in AdsManager**:
*   `true`: Enables ads.
*   `false`: Disables ads for specific use cases.

* ## Usage Tips
* Always pre-load ads to minimize latency.
* Implement error handling in all ad-related callbacks to ensure a smooth user experience.
* Ensure ad placement complies with AdMob policies to avoid account suspension.

* ## Enjoy! 🎉
* Feel free to use this library to simplify ad integration in your Android apps.
* If you encounter any issues or have feature requests, please open an issue or contribute to the repository.
