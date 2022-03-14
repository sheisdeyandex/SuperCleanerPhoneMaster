package com.supers.cleaner.phonemaster.ui
import android.animation.Animator
import android.animation.ObjectAnimator
import android.bluetooth.BluetoothManager
import android.content.ContentResolver
import android.content.Context
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
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentExtremePowerSavingAnimBinding

class FragmentExtremePowerSavingAnim : Fragment() {
    private var _binding: FragmentExtremePowerSavingAnimBinding? = null
    private val binding get() = _binding!!
    var mInterstitialAd: InterstitialAd? = null
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
    fun initAnim(){
        val updateTask: Runnable = object : Runnable {
            override fun run() {
                binding.tvNormalModePercent.text = binding.crpvProgress.percent.toInt().toString()+"%"
                if(binding.crpvProgress.percent.toInt() == 12){
                    binding.ivDone.visibility= View.VISIBLE
                    binding.tvFirstText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 24){
                    binding.ivDoneSecond.visibility= View.VISIBLE
                    binding.tvSecondText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 48){
                    binding.ivDoneThird.visibility= View.VISIBLE
                    binding.tvThirdText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 72){
                    binding.ivDoneFourth.visibility= View.VISIBLE
                    binding.tvFourthText.setTextColor(Color.WHITE)

                }
                else   if(binding.crpvProgress.percent.toInt() == 98){
                    binding.ivDoneFifth.visibility= View.VISIBLE
                    binding.tvFifthText.setTextColor(Color.WHITE)

                }
                handler.postDelayed(this, 10)
            }
        }

        handler.postDelayed(updateTask, 10)
        val animation = ObjectAnimator.ofFloat(binding.crpvProgress, "percent", 0f,100f)
        animation.duration = 5000
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                val bluetoothAdapter = (requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
                if (bluetoothAdapter!=null&&bluetoothAdapter.isEnabled){
                    bluetoothAdapter.disable()}
                ContentResolver.setMasterSyncAutomatically(false)

            }
            override fun onAnimationEnd(animator: Animator) {

                (requireActivity() as MainActivity).binding.bnvNav.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showSettings = Settings.System.canWrite(requireContext())
                    if(showSettings){

                        setBrightness(25)
                        if (mInterstitialAd != null) {
                            if(!MyApplication.premiumUser){

                                mInterstitialAd?.show(requireActivity())
                            }
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                        }
                        (requireActivity() as MainActivity).selectTab("energysaving") }
                    else{
                        if (mInterstitialAd != null) {
                            if(!MyApplication.premiumUser){

                                mInterstitialAd?.show(requireActivity())
                            }
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                        }
                        (requireActivity() as MainActivity).selectTab("energysaving")
                        openAndroidPermissionsMenu()
                    }
                }
                else{
                    if (mInterstitialAd != null) {
                        if(!MyApplication.premiumUser){

                            mInterstitialAd?.show(requireActivity())
                        }
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                    (requireActivity() as MainActivity).selectTab("energysaving") }

            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animation.start()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtremePowerSavingAnimBinding.inflate(inflater, container, false)
        val view = binding.root
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
//        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                Log.d("sukagdereklama", "addismissed")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                Log.d("sukagdereklama", "Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Log.d("sukagdereklama", "Ad showed fullscreen content.")
//                mInterstitialAd = null
//            }
//        }
        initAnim()
        return view
    }
}