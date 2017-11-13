-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-ignorewarnings
-verbose
-dontoptimize
-dontpreverify

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepattributes InnerClasses,LineNumberTable

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**
-dontwarn com.yixia.**

-keep class com.shell** { *; }
-dontwarn com.shell**

-keep class bolts.** { *; }
-keep class com.haiqiu.miaohi.bean.** { *; }
-keep class com.haiqiu.miaohi.net.** { *; }
-keep class com.haiqiu.miaohi.response.** { *; }
-keep class com.googlecode.** { *; }
-keep class com.yixia.** { *; }
-keep class tv.** { *; }
-keep class com.alipay.** { *; }
-keep class org.bytedeco.** { *; }
-keep class org.wysaid.** { *; }
-keep class com.haiqiu.miaohi.ffmpeg.FFmpegUtil.** { *; }
-keep class com.haiqiu.miaohi.utils.MiaoHiKeyUtils.** { *; }
-keep class com.enrique.stackblur.** { *; }

-keepclassmembers class com.haiqiu.miaohi.ffmpeg.FFmpegUtil {
 public *;
}


-keep class im.** { *; }
-keep class assets.** { *; }
-keep class org.json.** {*; }
-keep class **.R$* {*; }
-keep class **.R{*; }

-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class m.framework.**{*;}

-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

-keepclassmembers class * { public <init>(org.json.JSONObject);}
-keep public class com.idea.fifaalarmclock.app.R$*{public static final int *;}
-keep public class com.umeng.fb.ui.ThreadView {}
-keep class com.umeng.** { *; }

-dontwarn com.umeng.**
-dontwarn org.apache.**
-keep class org.apache.**{ *; }

-keep public class * extends com.umeng.**
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class * extends java.lang.annotation.Annotation

-keep class vi.com.gdi.bgl.android.**{*;}

-keepattributes Signature,InnerClasses
-keep public class * implements java.io.Serializable {
        public *;
}

#-dontwarn butterknife.internal.**
#-keep class **$$ViewInjector { *; }
#-keepnames class * { @butterknife.InjectView *;}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#talkingdata混淆
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

#jlog
-keepattributes SourceFile, LineNumberTable
-keep class com.jiongbull.jlog.** { *; }

#融云
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepattributes Exceptions,InnerClasses
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent{*;}
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**
-keep class com.ultrapower.** {*;}

#------
-keep public class com.apkfuns.logutils.*{*;}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class com.growingio.android.sdk.** {
    *;
}
-dontwarn com.growingio.android.sdk.**
-keepnames class * extends android.view.View
-keep class * extends android.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
-keep class android.support.v4.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
-keep class * extends android.support.v4.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}

