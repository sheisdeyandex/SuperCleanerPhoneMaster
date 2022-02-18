package com.supers.cleaner.phonemaster.ui

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCleanAnimBinding
import com.supers.cleaner.phonemaster.databinding.FragmentSplashScreenBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment

class FragmentCleanAnim(iFragment: IFragment,iBanner: IBanner) : Fragment() {
    val iFragment:IFragment = iFragment
    val iBanner:IBanner = iBanner
    private var _binding: FragmentCleanAnimBinding? = null
    private val binding get() = _binding!!
    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){
            var   adRequest = AdRequest.Builder().build()
            if(!MyApplication.premiumUser){
                binding.avBanner.loadAd(adRequest)
            }


            binding.animationView.playAnimation()
            binding.animationView.addAnimatorListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    iFragment.regulate(true,6)

                    iBanner.showInter()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCleanAnimBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

}