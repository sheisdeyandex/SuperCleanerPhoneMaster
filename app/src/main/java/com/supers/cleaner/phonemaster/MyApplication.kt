package com.supers.cleaner.phonemaster

import android.app.Activity
import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Cicerone
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.onesignal.OneSignal
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class MyApplication : Application() {
     val ONESIGNAL_APP_ID = "ebfe226a-58ee-4caa-8b48-08938c1db416"
    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        activateAppMetrica()

    }
    companion object {
        lateinit var sInstance: MyApplication
            private set
        fun getInstance(): MyApplication {
            return sInstance
        }
        fun getInstance(activity: Activity): MyApplication {
            return activity.application as MyApplication
        }
        @JvmField
        var worktime: String = ""
        var finalUltraModeUsageTime: String = ""
        var finalExtremeModeUsageTime: String = ""
        var optimize:Boolean= false
        var energysaving:Boolean= false
        var coolcpu:Boolean= false
        var clean:Boolean= false
        var others:Boolean= false
        var notificationClicked:Boolean= false
        var premiumUser: Boolean = false
        var showuserpolicy: Boolean = false
        var bluetoothPermissionGranted: Boolean = false
        var animImageDrawable: Drawable? = null
        var animImageDrawableSecond: Drawable? = null
        var animImageDrawableThird: Drawable? = null
        var animImageDrawablefourth: Drawable? = null
        var animImageDrawableFifth: Drawable? = null
    }
    private fun activateAppMetrica() {
        val appMetricaConfig: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder("f9587771-e2e6-4158-ba4b-9b252f17d5f5")
                .withLocationTracking(true)
                .withStatisticsSending(true)
                .build()
        YandexMetrica.activate(applicationContext, appMetricaConfig)
    }

    private fun isFirstActivationAsUpdate(): Boolean {

    return true}
}