package com.supers.cleaner.phonemaster

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()



//        adchnage.observable.subscribe({
//            adview!!.loadAd(adRequest)
//            Log.d("sukaloaded","yes")
//        },{throwable -> Log.d("sukaloaded",throwable.message.toString())}){
//
//        }


        activateAppMetrica()
    }
    companion object {
        @JvmField
        var worktime: String = ""
        var premiumUser: Boolean = false
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