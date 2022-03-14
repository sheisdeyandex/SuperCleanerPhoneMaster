package com.supers.cleaner.phonemaster.ui

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentAppsBinding
import com.supers.cleaner.phonemaster.interfaces.DeleteAppsI
import com.supers.cleaner.phonemaster.models.AppsModel
import com.supers.cleaner.phonemaster.ui.containers.AppsAdapter
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10


class FragmentApps : Fragment() , DeleteAppsI{

    var uniqueId = 0

    @JvmName("getUniqueId1")
    fun getUniqueId(): Int {
        return uniqueId++
    }
    fun getFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }
    private var _binding: FragmentAppsBinding? = null
    val binding get() = _binding!!
    var appsAdapter: AppsAdapter? = null
    lateinit var appsModelList: ArrayList<AppsModel>
    lateinit var appname: ArrayList<String>
    lateinit var appsize: ArrayList<String>
    lateinit var appicon: ArrayList<Drawable>
    lateinit var appsModel: AppsModel
    lateinit var ids: ArrayList<Int>
    lateinit var integers1: ArrayList<Int>

    private var mInterstitialAd: InterstitialAd? = null
    var activityResultLaunch: ActivityResultLauncher<Intent>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        val view = binding.root
        activityResultLaunch = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    // There are no request codes
                    val data: Intent? = result.data
                    for (i in integers1) {
                        for (i1 in ids.indices) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if(appsAdapter!!.checked>0) {
                                    appsAdapter!!.checked -= 1
                                }
                                if (mInterstitialAd != null) {
                                    mInterstitialAd?.show(requireActivity())
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                                }
                                appsAdapter!!.deleteAppsI.checked(appsAdapter!!.checked)
                                appsModelList.removeIf { imagesModel: AppsModel ->
                                    return@removeIf imagesModel.id == ids[i1]
                                }
                            }
                        }
                        appsAdapter!!.notifyItemRemoved(i)
                        appsAdapter!!.notifyItemRangeChanged(i, appsModelList.size)
                    }
                    /*          for(int i1:integers1){
                                                    appsModelList.remove(i1);
                                                    appsAdapter.notifyItemRemoved(i1);
                                                }
                                                appsAdapter.notifyItemRangeChanged(findMinIndex(integers1), appsModelList.size());*/
                }
                RESULT_CANCELED -> {
                }
                Activity.RESULT_FIRST_USER -> {
                }
            }
        }
        appname = ArrayList()
        appicon = ArrayList()
        appsize = ArrayList()
        appsModelList = ArrayList()
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd
            }
        })
        val packList = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packList.indices) {
            val packInfo = packList[i]
            if (packInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val appName =
                    packInfo.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                val packageName = packInfo.applicationInfo.packageName
                if (packageName != "com.supers.cleaner.phonemaster") {
                    appname.add(appName)
                    try {
                        val size: Long = File(
                            requireContext().packageManager.getApplicationInfo(
                                packageName,
                                0
                            ).publicSourceDir
                        ).length()
                        appsize.add(getFileSize(size))
                        try {
                            val icon = requireContext().packageManager.getApplicationIcon(packageName)
                            appicon.add(icon)
                            appsModel = AppsModel()
                            appsModel.name = appName
                            appsModel.icon = icon
                            appsModel.size = getFileSize(size)
                            appsModel.id = getUniqueId()
                            appsModel.packagename = packageName
                            appsModelList.add(appsModel)
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
            } else if (isSystemPackage(packList[i])) {
                val packageName = packInfo.applicationInfo.packageName
                var size: Long = 0
                try {
                    size = File(
                        requireContext().packageManager.getApplicationInfo(
                            packageName,
                            0
                        ).publicSourceDir
                    ).length()
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }


        binding.rvAppsRecycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        appsAdapter = AppsAdapter(requireContext(), appsModelList, this, this)
        binding.rvAppsRecycler.adapter = appsAdapter
binding.back.setOnClickListener {
    (requireActivity() as MainActivity).selectTab("noads")
    (requireActivity() as MainActivity).binding.bnvNav.visibility = View.VISIBLE

}
        return view
    }
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    override fun delete(integers: ArrayList<Int>, packages: ArrayList<Uri>, ids: ArrayList<Int>) {
        integers1 = ArrayList()
        integers1.addAll(integers)
        this.ids = ArrayList()
        this.ids.addAll(ids)
        for (i in packages) {
                val uninstallIntent = Intent(Intent.ACTION_DELETE, i)
            uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
            activityResultLaunch!!.launch(uninstallIntent)

//            val intent = Intent(Intent.ACTION_DELETE)
//            val sender = PendingIntent.getActivity(activity, 0, intent, 0)
//            val mPackageInstaller = requireActivity().packageManager.packageInstaller
//            mPackageInstaller.uninstall(i, sender.intentSender)
        }
    }

    override fun checked(i: Int) {
        Log.d("sukachecked", i.toString())
        if(i>=1){
            binding.mbDelete.visibility = View.VISIBLE
        }
        else{
            binding.mbDelete.visibility = View.GONE
        }
    }


}