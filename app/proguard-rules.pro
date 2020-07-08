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

-keepattributes Signature
-keepattributes *Annotation*

-keep class az.siftoshka.habitube.entities.** {*;}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

## Crashlytics
-keep class com.crashlytics.** { *; }

## ButterKnife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

## LIBRARY: OkHttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

## LIBRARY: Retrofit
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn com.google.appengine.**

## LIBRARY: GSON
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-dontwarn com.google.gson.**

# Library: glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn java.util.concurrent.Flow*

-optimizationpasses 5
#-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose

#your package path where your gson models are stored
#-keep class your.package.name.here.** { *; }

# The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't handle.
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!method/propagation/parameter
-keepattributes SourceFile,LineNumberTable,InnerClasses

#Google play services proguard rules:
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
## because google play service gets stripped of unnessecary methods and code dont warn
-dontwarn com.google.android.gms.**

# Ignore overridden Android classes
-dontwarn android.**
-keep class android.**

# Ignore imported library references
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep all public constructors of all public classes, but still obfuscate+optimize their content.
# This is necessary because optimization removes constructors which are called through XML.
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context);
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep serializable objects
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-assumenosideeffects class android.util.Log {
    public static *** i(...);
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
    public static *** w(...);
    public static *** wtf(...);
}