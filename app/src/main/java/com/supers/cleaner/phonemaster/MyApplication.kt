package com.supers.cleaner.phonemaster

import android.app.Activity
import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        var adRequest = AdRequest.Builder().build()
        sInstance = this
        InterstitialAd.load(this,getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                   mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd
            }
        })


//        adchnage.observable.subscribe({
//            adview!!.loadAd(adRequest)
//            Log.d("sukaloaded","yes")
//        },{throwable -> Log.d("sukaloaded",throwable.message.toString())}){
//
//        }


        activateAppMetrica()
    }
    companion object {
        private lateinit var sInstance: MyApplication

        fun getInstance(): MyApplication {
            return sInstance
        }
        fun getInstance(activity: Activity): MyApplication {
            return activity.application as MyApplication
        }
        @JvmField
        var worktime: String = ""
         var mInterstitialAd: InterstitialAd? = null
        var premiumUser: Boolean = false
        var showuserpolicy: Boolean = false
        var adloaded: Boolean = false
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
                .handleFirstActivationAsUpdate(isFirstActivationAsUpdate())
                .withLocationTracking(true)
                .withStatisticsSending(true)
                .build()
        YandexMetrica.activate(applicationContext, appMetricaConfig)
    }

    private fun isFirstActivationAsUpdate(): Boolean {

    return true}
}