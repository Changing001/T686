-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-dontwarn com.mbridge.**
-keepclassmembers class **.R$* { public static final int mbridge*; }

-keep public class com.mbridge.* extends androidx.** { *; }
-keep public class androidx.viewpager.widget.PagerAdapter{*;}
-keep public class androidx.viewpager.widget.ViewPager.OnPageChangeListener{*;}
-keep interface androidx.annotation.IntDef{*;}
-keep interface androidx.annotation.Nullable{*;}
-keep interface androidx.annotation.CheckResult{*;}
-keep interface androidx.annotation.NonNull{*;}
-keep public class androidx.fragment.app.Fragment{*;}
-keep public class androidx.core.content.FileProvider{*;}
-keep public class androidx.core.app.NotificationCompat{*;}
-keep public class androidx.appcompat.widget.AppCompatImageView {*;}
-keep public class androidx.recyclerview.*{*;}
-keep class com.mbridge.msdk.foundation.tools.FastKV{*;}
-keep class com.mbridge.msdk.foundation.tools.FastKV$Builder{*;}


-keep class com.uak.kn.JhAdapter{*;}
-keep class com.announcement.comprehension.utils.Tm1{*;}
-keep class com.announcement.comprehension.utils.Ut2{*;}

-keepattributes !SourceFile
-dontshrink

-keep class com.bytedance.sdk.** { *; }
-keep class com.appsflyer.** { *; }
-keep class kotlin.jvm.internal.** { *; }
-keep public class com.android.installreferrer.** { *; }

-keep class kotlin.** { *; }
-dontwarn kotlin.**
-keep class kotlinx.** { *; }
-keep class android.**{*;}
-keep class com.android.**{*;}
-keep class androidx.** { *; }

-keep class com.google.**{*;}
-keep class com.google.**{*;}

-keep class com.tencent.mmkv.** { *; }
-keepclassmembers class * {
    native <methods>;
}