# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontoptimize
-dontskipnonpubliclibraryclasses
-keepattributes *Annotation*
-ignorewarnings
#admob
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
#facebook
-keep class com.facebook.ads.** { *; }
#R
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class com.facebook.** { *; }
-keep class com.samsung.** { *; }
-dontwarn com.samsung.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class android.content.pm.IPackageStatsObserver { *; }
-keep class com.android.internal.telephony.ITelephony { *; }
-keep class com.android.internal.telephony.ITelephony$* {
    public <fields>;
    public <methods>;
}
-keepattributes *Annotation*,EnclosingMethod,Signature

-keep class com.appsflyer.** { *; }