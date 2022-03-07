package com.supers.cleaner.phonemaster.ui
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.supers.cleaner.phonemaster.databinding.FragmentOptimizeBinding
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.animation.Animator

import android.view.animation.DecelerateInterpolator

import com.yandex.metrica.impl.ob.pb

import android.animation.ObjectAnimator
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.interfaces.IBanner

class FragmentOptimize(iBanner:IBanner) : Fragment() {
    val iBanner:IBanner = iBanner
    private var _binding: FragmentOptimizeBinding? = null
    private val binding get() = _binding!!

    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptimizeBinding.inflate(inflater, container, false)
        val view = binding.root
        getRamUsageInfo()
        var   adRequest = AdRequest.Builder().build()
        if(!MyApplication.premiumUser){

            (requireActivity()as MainActivity).binding.bnvNav.visibility = View.VISIBLE
            binding.avBanner.loadAd(adRequest)
        }
        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd

            }
        })
        binding.materialButton.setOnClickListener {
        optimize()
        }
        return view
    }
    fun optimize(){
        val pm: PackageManager =requireActivity().packageManager

        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            if(!isSystemPackage(packageInfo)){
                val mActivityManager =
                    requireActivity().getSystemService(ACTIVITY_SERVICE) as ActivityManager
                mActivityManager.killBackgroundProcesses(packageInfo.packageName)
            } }
        val animation = ObjectAnimator.ofFloat(binding.crpvProgress, "percent", 0f, 100f)
        animation.duration = 5000
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                binding.materialButton.isEnabled = false
                binding.tvRam.visibility = View.GONE
                binding.tvRamUsageFound.visibility = View.GONE
                binding.tvFoundUsedRam.textSize = 18f
                binding.tvFoundUsedRam.text = resources.getString(R.string.optimizing)
            }
            override fun onAnimationEnd(animator: Animator) {
                binding.materialButton.isEnabled= false
                getRamUsageInfo()
                binding.animationView.setAnimation(R.raw.blue_ellipse)
                binding.animationView.playAnimation()
                binding.animationView.loop(true)
                binding.tvRam.visibility = View.VISIBLE
                binding.tvFoundUsedRam.textSize = 36f
                binding.tvRamUsageFound.visibility = View.VISIBLE
                binding.tvMbText.text = getString(R.string.optimized)
                binding.clOptiomize.transitionToEnd()
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animation.start()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){

        }
    }

    private fun isSystemPackage(pkgInfo: ApplicationInfo): Boolean {
        return pkgInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
    private  fun getRamUsageInfo(){
        val memoryInfo = ActivityManager.MemoryInfo()
        (requireActivity().getSystemService(ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        val nativeHeapSize = memoryInfo.totalMem
        val nativeHeapFreeSize = memoryInfo.availMem
        val usedMemInBytes = nativeHeapSize - nativeHeapFreeSize
        val usedMemInPercentage = usedMemInBytes * 100 / nativeHeapSize
       binding.tvRamUsed.text = "$usedMemInPercentage%"
        binding.tvFoundUsedRam.text = Formatter.formatFileSize(requireContext(), usedMemInBytes)
val totalsize = Formatter.formatFileSize(requireContext(), nativeHeapSize)
    val usedsize = Formatter.formatFileSize(requireContext(), usedMemInBytes)
        binding.tvRamUsedFromTo.text = "$usedsize / $totalsize"
        val animation = ObjectAnimator.ofFloat(binding.crpvProgress, "percent", 0f, usedMemInPercentage.toFloat())
        animation.duration = 2000
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }
            override fun onAnimationEnd(animator: Animator) {

            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animation.start()
    }
}