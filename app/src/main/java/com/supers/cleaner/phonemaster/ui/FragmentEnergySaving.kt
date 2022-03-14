package com.supers.cleaner.phonemaster.ui
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.content.Intent

import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentEnergySavingBinding
import com.supers.cleaner.phonemaster.interfaces.AskPermissions
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment
class FragmentEnergySaving : Fragment() {
    private var _binding: FragmentEnergySavingBinding? = null
    private val binding get() = _binding!!
    val handler = Handler(Looper.getMainLooper())
    var mInterstitialAd: InterstitialAd? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnergySavingBinding.inflate(inflater, container, false)
        val view = binding.root
        val infielder = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = requireActivity().registerReceiver(null, infielder)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)

        val finalUsageTimeNormalMode = String.format("%.02f", level*10/100F-0.5F).replace(".", " ч. ").replace(",", " ч. ")+" м."
        MyApplication.worktime= finalUsageTimeNormalMode
        val updateTask: Runnable = object : Runnable {
            override fun run() {
                binding.tvWorkTime.text = MyApplication.worktime
                handler.postDelayed(this, 10)
            }
        }
        var adRequest = AdRequest.Builder().build()
        handler.postDelayed(updateTask, 10)
        binding.tvEnergyPercent.text = "$level%"
        binding.ywView.progress = level
        binding.ywView.setFrontWaveColor(Color.parseColor("#772499DF"))
        binding.ywView.setBehindWaveColor(Color.parseColor("#4DBDFC"))
        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd

            }
        })
binding.vNormalMode.setOnClickListener {
    binding.vNormalMode.isClickable = false
    if(MyApplication.bluetoothPermissionGranted){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            (requireActivity() as MainActivity).selectTab("normalmode")



            binding.animationView.setAnimation(R.raw.blue_ellipse)
            binding.animationView.playAnimation()
            binding.animationView.loop(true)
        }
        else{

            binding.animationView.setAnimation(R.raw.blue_ellipse)
            binding.animationView.playAnimation()
            binding.animationView.loop(true)
            binding.vNormalMode.isClickable =false

            (requireActivity() as MainActivity).selectTab("normalmode")
        }
    }
    else{
      //  askPermissions.requestBlueTooth()
    }
}
        binding.vUltraMode.setOnClickListener {
            binding.vUltraMode.isClickable = false
            if(MyApplication.bluetoothPermissionGranted){


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        binding.vUltraMode.isClickable =false
                      //  iFragment.regulate(false,2)
                    (requireActivity() as MainActivity).selectTab("ultramode")
                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)

                }
                else{

                    binding.vUltraMode.isClickable =false
                    (requireActivity() as MainActivity).selectTab("ultramode")
                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)
                }
            }
            else{
              //  askPermissions.requestBlueTooth()
            }
        }
        binding.vExtremeMode.setOnClickListener {
            binding.vExtremeMode.isClickable = false
            if(MyApplication.bluetoothPermissionGranted){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        binding.vExtremeMode.isClickable =false
                    (requireActivity() as MainActivity).selectTab("extrememode")

                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)

                }
                else{

                    binding.vExtremeMode.isClickable =false
                    (requireActivity() as MainActivity).selectTab("extrememode")

                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)
                }
            }
            else{
             //   askPermissions.requestBlueTooth()
            }
        }

        return view
    }
}