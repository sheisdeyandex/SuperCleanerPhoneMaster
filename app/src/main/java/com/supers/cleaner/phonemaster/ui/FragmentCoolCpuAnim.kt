package com.supers.cleaner.phonemaster.ui

import android.animation.Animator
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCoolCpuAnimBinding
import com.supers.cleaner.phonemaster.interfaces.IFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.interfaces.IBanner


class FragmentCoolCpuAnim(iFragment: IFragment, iBanner: IBanner) : Fragment() {
val iFragment:IFragment = iFragment
    val iBanner:IBanner = iBanner
    private var _binding: FragmentCoolCpuAnimBinding? = null
    private val binding get() = _binding!!

    private fun cpuTemperature(): Float {
        val process: Process
        return try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp")
            process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line = reader.readLine()
            if (line != null) {
                val temp = line.toFloat()
                temp / 1000.0f
            } else {
                51.0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0f
        }
    }
    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {

            binding.ivAnimFirst.setImageDrawable(MyApplication.animImageDrawable)
            binding.ivAnimSecond.setImageDrawable(MyApplication.animImageDrawableSecond)
            binding.ivAnimThird.setImageDrawable(MyApplication.animImageDrawableThird)
            binding.ivAnimFourth.setImageDrawable(MyApplication.animImageDrawablefourth)
            binding.ivAnimFifth.setImageDrawable(MyApplication.animImageDrawableFifth)
            binding.clAnimFirst.transitionToEnd()
            binding.animationView.addAnimatorListener(object:Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(p0: Long) {
                            binding.tvCooled.text =
                                getString(R.string.coolled) + " " + (cpuTemperature() - 3.5f).toString() + "℃"
                        }

                        override fun onFinish() {
                            iBanner.showInter()
                            iFragment.regulateCPUAnim(true, 1)
                        }
                    }.start()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
           object :CountDownTimer(1000,1000){
               override fun onTick(p0: Long) {

               }

               override fun onFinish() {
                   binding.clAnimSecond.transitionToEnd()
                   object :CountDownTimer(1000,1000){
                       override fun onTick(p0: Long) {

                       }

                       override fun onFinish() {
                       binding.clAnimThird.transitionToEnd()
                           object :CountDownTimer(1000,1000){
                               override fun onTick(p0: Long) {

                               }

                               override fun onFinish() {
                           binding.clAnimFourth.transitionToEnd()
                                   object :CountDownTimer(1000,1000){
                                       override fun onTick(p0: Long) {

                                       }

                                       override fun onFinish() {
                                           binding.clAnimFifth.transitionToEnd()


                                       }
                                   }.start()
                               }
                           }.start()
                       }

                   }.start()
               }

           }.start()

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoolCpuAnimBinding.inflate(inflater, container, false)
        val view = binding.root

        var   adRequest = AdRequest.Builder().build()
        if(!MyApplication.premiumUser){
            binding.avBanner.loadAd(adRequest)
        }

        return view
    }


}