package com.supers.cleaner.phonemaster.ui

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCleanAnimBinding
import com.supers.cleaner.phonemaster.databinding.FragmentSplashScreenBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment

class FragmentCleanAnim() : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null
    private var _binding: FragmentCleanAnimBinding? = null
    private val binding get() = _binding!!
fun cleanAnim(){
    val adRequest = AdRequest.Builder().build()



    binding.animationView.playAnimation()
    binding.animationView.addAnimatorListener(object: Animator.AnimatorListener{
        override fun onAnimationStart(p0: Animator?) {

        }

        override fun onAnimationEnd(p0: Animator?) {
            (requireActivity() as MainActivity).selectTab("clean")
            (requireActivity() as MainActivity).binding.bnvNav.visibility= View.VISIBLE


            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }

        override fun onAnimationCancel(p0: Animator?) {

        }

        override fun onAnimationRepeat(p0: Animator?) {
        }
    })
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCleanAnimBinding.inflate(inflater, container, false)
        val view = binding.root
cleanAnim()
        var   adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
        return view
    }

}