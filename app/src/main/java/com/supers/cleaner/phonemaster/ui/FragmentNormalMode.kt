package com.supers.cleaner.phonemaster.ui
import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentNormalModeBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment
import com.yandex.metrica.impl.ob.Ib

class FragmentNormalMode : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null
fun openAndroidPermissionsMenu() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + requireActivity().packageName)
        startActivity(intent)
    }
    var showSettings:Boolean= false
    fun setBrightness(brightness: Int) {
        var brightness = brightness
        if (brightness < 0) brightness = 0 else if (brightness > 255) brightness = 255
        val cResolver = requireActivity().applicationContext.contentResolver
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
    }
    val handler = Handler(Looper.getMainLooper())

    private var _binding: FragmentNormalModeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNormalModeBinding.inflate(inflater, container, false)
        val view = binding.root

        (requireActivity() as MainActivity).binding.bnvNav.visibility = View.GONE
        var   adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
        val updateTask: Runnable = object : Runnable {
            override fun run() {
                binding.tvNormalModePercent.text = binding.crpvProgress.percent.toInt().toString()+"%"
                if(binding.crpvProgress.percent.toInt() == 24){
                    binding.ivDone.visibility= View.VISIBLE
                    binding.tvFirstText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 42){
                    binding.ivDoneSecond.visibility= View.VISIBLE
                    binding.tvSecondText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 72){
                    binding.ivDoneThird.visibility= View.VISIBLE
                    binding.tvThirdText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 98){
                    binding.ivDoneFourth.visibility= View.VISIBLE
                    binding.tvFourthText.setTextColor(Color.WHITE)

                }
                handler.postDelayed(this, 10)
            }
        }

        handler.postDelayed(updateTask, 10)
        val animation = ObjectAnimator.ofFloat(binding.crpvProgress, "percent", 0f, 100f)
        animation.duration = 3000
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
            }
            override fun onAnimationEnd(animator: Animator) {

                (requireActivity() as MainActivity).binding.bnvNav.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showSettings = Settings.System.canWrite(requireContext())
                    if(showSettings){
                        (requireActivity() as MainActivity).selectTab("energysaving")
                        setBrightness(200)
                        if (mInterstitialAd != null) {
                            if(!MyApplication.premiumUser)
                            {

                                mInterstitialAd?.show(requireActivity())
                            }
                        }
                    }
                    else{
                        if(!MyApplication.premiumUser)
                        {

                            mInterstitialAd?.show(requireActivity())
                        }
                        (requireActivity() as MainActivity).selectTab("energysaving")

                        openAndroidPermissionsMenu()
                    }
                }
                else{
                    if(!MyApplication.premiumUser)
                    {

                        mInterstitialAd?.show(requireActivity())
                    }
                    (requireActivity() as MainActivity).selectTab("energysaving")


                }

            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animation.start()




        return view
    }


}