# Keep all public classes and methods in the library
-keep public class com.shehan.adsmanager.** { *; }

# Keep callback interfaces
-keep interface com.shehan.adsmanager.callback.** { *; }

# Keep enums
-keep enum com.shehan.adsmanager.enums.** { *; }

# Keep AdMob related classes
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Keep Lottie
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# Keep native ad template view
-keep class com.shehan.adsmanager.ads.TemplateView { *; }
-keep class com.shehan.adsmanager.ads.NativeTemplateStyle { *; }

# Preserve annotations
-keepattributes *Annotation*

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep generic signatures for reflection
-keepattributes Signature
