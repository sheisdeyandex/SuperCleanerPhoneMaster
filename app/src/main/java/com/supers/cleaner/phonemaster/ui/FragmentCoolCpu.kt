package com.supers.cleaner.phonemaster.ui
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCoolCpuBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception
import java.text.DecimalFormat

class FragmentCoolCpu() : Fragment() {
    private var _binding: FragmentCoolCpuBinding? = null
    private val binding get() = _binding!!

    var countapps=0
    private fun getFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }
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
    private fun getInstalledApps() {

        val packs = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)

countapps++
                val packages = p.applicationInfo.packageName
                val size: Long = File(requireContext().packageManager.getApplicationInfo(packages,0).publicSourceDir  //.publicSourceDir
                ).length()
                when (countapps) {
                    1 -> {
                        binding.ivApp1.setImageDrawable(icon)
                        MyApplication.animImageDrawable = icon
                        binding.tvAppSize1.text = getFileSize(size)
                    }
                    2 -> {
                        binding.ivApp2.setImageDrawable(icon)
                        MyApplication.animImageDrawableSecond = icon
                        binding.tvAppSize2.text = getFileSize(size)
                    }
                    3 -> {
                        binding.ivApp3.setImageDrawable(icon)
                        MyApplication.animImageDrawableThird = icon
                        binding.tvAppSize3.text = getFileSize(size)
                    }
                    4 -> {
                        binding.ivApp4.setImageDrawable(icon)
                        MyApplication.animImageDrawablefourth = icon
                        binding.tvAppSize4.text = getFileSize(size)
                    }
                    5-> {
                        binding.ivApp5.setImageDrawable(icon)
                        MyApplication.animImageDrawableFifth = icon
                        binding.tvAppSize5.text = getFileSize(size)

                    }
                }

            }
        }


    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){

        }
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoolCpuBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.tvCpuText.text = cpuTemperature().toString()+"℃"


                if(MyApplication.coolcpu){
                    binding.ivCpu.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_cpu_finished))
                    binding.tvOverheat.text = getString(R.string.normal)
                    binding.tvOverheat.setTextColor(Color.parseColor("#73EE48"))
                    binding.tvCpuText.text = ((cpuTemperature()-3.5f).toString())

                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)
                    binding.materialButtonDoIt.background = ContextCompat.getDrawable(requireContext(), R.drawable.optimize_button_optimized)
                    binding.tvUltrapowertext.text = getString(R.string.normal_cpu_temperature)
                    binding.tvUltrapowertext.setTextColor(Color.parseColor("#73EE48"))
                    binding.materialButtonDoIt.isClickable = false
                    binding.materialButtonDoIt.text = getString(R.string.cooled)
                    binding.vView.visibility = View.GONE
                    binding.clBottomApps.visibility = View.GONE
                }



        binding.materialButtonDoIt.setOnClickListener {

            (requireActivity() as MainActivity).selectTab("coolcpuanim")
            (requireActivity() as MainActivity).binding.bnvNav.visibility = View.GONE
            object :CountDownTimer(2000,1000){
                override fun onTick(p0: Long) {

                }

                override fun onFinish() {
                    binding.ivCpu.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_cpu_finished))
                    binding.tvOverheat.text = getString(R.string.normal)
                    binding.tvOverheat.setTextColor(Color.parseColor("#73EE48"))
                    binding.tvCpuText.text = ((cpuTemperature()-3.5f).toString())

                    binding.animationView.setAnimation(R.raw.blue_ellipse)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)
                    binding.materialButtonDoIt.background = ContextCompat.getDrawable(requireContext(), R.drawable.optimize_button_optimized)
                    binding.tvUltrapowertext.text = getString(R.string.normal_cpu_temperature)
                    binding.tvUltrapowertext.setTextColor(Color.parseColor("#73EE48"))
                    binding.materialButtonDoIt.isClickable = false
                    binding.materialButtonDoIt.text = getString(R.string.cooled)
                    binding.vView.visibility = View.GONE
                    binding.clBottomApps.visibility = View.GONE
                }
            }.start()

        }
getInstalledApps()
        return view
    }
}