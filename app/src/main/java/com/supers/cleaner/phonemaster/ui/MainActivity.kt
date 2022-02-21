package com.supers.cleaner.phonemaster.ui
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.supers.cleaner.phonemaster.databinding.ActivityMainBinding
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.interfaces.IFragment
import androidx.core.app.ActivityCompat

import android.widget.Toast

import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.util.Log

import androidx.core.content.ContextCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.interfaces.AskPermissions
import com.supers.cleaner.phonemaster.interfaces.IBanner


class MainActivity : AppCompatActivity(), IFragment, AskPermissions, IBanner{
    override fun requestBlueTooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            askForPermission(android.Manifest.permission.BLUETOOTH_CONNECT, 1)
        }
        else{
            MyApplication.bluetoothPermissionGranted = true
        }
    }

    private lateinit var binding:ActivityMainBinding
    val fm = supportFragmentManager
    private lateinit var activeFragment: Fragment
    private val fragmentOptimize = FragmentOptimize(this)
    private val webViewFragment = WebViewFragment(this)
    private val fragmentClean = FragmentClean(this  ,this)
    private val fragmentCleanAnim = FragmentCleanAnim(this,this)
    private val fragmentCoolCpu = FragmentCoolCpu(this,this)
    private val fragmentCoolCpuAnim = FragmentCoolCpuAnim(this, this)
    private val fragmentNormalMode = FragmentNormalMode(this,this)
    private lateinit var fragmentExtremeMode :Fragment
    private lateinit var splashScreen: SplashScreen
    private lateinit var fragmentExtremeModeAnim :Fragment
    private lateinit var fragmentUltraModeAnim :Fragment
    private lateinit var fragmentUltraMode :Fragment
    private val fragmentEnergySaving=FragmentEnergySaving(this, this,this)
    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(applicationContext, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    permission
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        else{
            MyApplication.bluetoothPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                //permission with request code 1 granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                //permission with request code 1 was not granted
                Toast.makeText(this, "Permission was not Granted", Toast.LENGTH_LONG).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun addFragments(){
       fm.beginTransaction().add(R.id.cl_main, webViewFragment).hide(webViewFragment).commit()
       fm.beginTransaction().add(R.id.cl_main, fragmentCleanAnim).hide(fragmentCleanAnim).commit()
       fm.beginTransaction().add(R.id.cl_main, fragmentClean).hide(fragmentClean).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentCoolCpuAnim).hide(fragmentCoolCpuAnim).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentCoolCpu).hide(fragmentCoolCpu).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentExtremeModeAnim).hide(fragmentExtremeModeAnim).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentExtremeMode).hide(fragmentExtremeMode).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentUltraModeAnim).hide(fragmentUltraModeAnim).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentUltraMode).hide(fragmentUltraMode).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentNormalMode).hide(fragmentNormalMode).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentEnergySaving).hide(fragmentEnergySaving).commit()
        fm.beginTransaction().add(R.id.cl_main, fragmentOptimize).hide(fragmentOptimize).commit()
        fm.beginTransaction().add(R.id.cl_main, splashScreen).commit()

    }

    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         adRequest = AdRequest.Builder().build()

        activeFragment   = fragmentOptimize


        MobileAds.initialize(this) {}

        InterstitialAd.load(this,getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (!MyApplication.showuserpolicy){
                    fm.beginTransaction().hide(splashScreen).show(fragmentOptimize).commit()
                    binding.bnvNav.visibility = View.VISIBLE
                    showInter()
                    mInterstitialAd = null
                }
MyApplication.adloaded=true
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
              if(!MyApplication.showuserpolicy){
                  fm.beginTransaction().hide(splashScreen).show(fragmentOptimize).commit()
                  binding.bnvNav.visibility = View.VISIBLE
                  interstitialAd.show(this@MainActivity)

              }
                MyApplication.adloaded=true
                mInterstitialAd = interstitialAd

            }
        })
        requestBlueTooth()
        val infielder = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, infielder)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val finalUltraModeUsageTime = String.format("%.02f", level*10/100F-0.5F+2).replace(".", " ч. ").replace(",", " ч. ")+" м."
        val finalExtremeModeUsageTime = String.format("%.02f", level*10/100F-0.5F+4).replace(".", " ч. ").replace(",", " ч. ")+" м."
        fragmentExtremeModeAnim = FragmentExtremePowerSavingAnim( this,this)
        fragmentExtremeMode = FragmentExtremePowerSaving( finalExtremeModeUsageTime,this, this)
        fragmentUltraMode  = FragmentUltraPowerSaving(finalUltraModeUsageTime, this,this)
        fragmentUltraModeAnim  = FragmentUltraPowerSavingAnim(this,this)
        splashScreen = SplashScreen(this)
     addFragments()
object :CountDownTimer(3000,1000){
    override fun onTick(p0: Long) {

    }

    override fun onFinish() {


    }

}.start()
        binding.bnvNav.visibility = View.GONE
        binding.bnvNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.optimize -> {
                    fm.beginTransaction().hide(activeFragment).show(fragmentOptimize).commit()
activeFragment = fragmentOptimize
                   true
                }
                R.id.energy_saving -> {
                    fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                    activeFragment = fragmentEnergySaving
                    true
                }
                R.id.cool_cpu -> {
                    fm.beginTransaction().hide(activeFragment).show(fragmentCoolCpu).commit()
                    activeFragment = fragmentCoolCpu
                    true
                }
                R.id.clean_trash ->{
                    fm.beginTransaction().hide(activeFragment).show(fragmentClean).commit()
                    activeFragment = fragmentClean
                    true
                }
                else -> false
            }}
    //    hideSystemUI()
    }


    override fun regulate(finished:Boolean, fragment:Int) {
        if(fragment==1){
            if(finished){
binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                activeFragment = fragmentEnergySaving
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentNormalMode).commit()
                activeFragment= fragmentNormalMode
            }
        }
        else if(fragment==2){
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                activeFragment = fragmentEnergySaving
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentUltraMode).commit()
                activeFragment= fragmentUltraMode
            }
        }
        else if(fragment==3)
        {
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                activeFragment = fragmentEnergySaving
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentExtremeMode).commit()
                activeFragment= fragmentExtremeMode
            }
        }
        else if(fragment==4)
        {
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                activeFragment = fragmentEnergySaving
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentUltraModeAnim).commit()
                activeFragment= fragmentUltraModeAnim
            }
        }
        else if(fragment==5)
        {
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
                activeFragment = fragmentEnergySaving
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentExtremeModeAnim).commit()
                activeFragment= fragmentExtremeModeAnim
            }
        }
        else if(fragment==6)
        {
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentClean).commit()
                activeFragment = fragmentClean
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentCleanAnim).commit()
                activeFragment= fragmentCleanAnim
            }
        }
        else if(fragment==7)
        {
            if(finished){
                fm.beginTransaction().hide(webViewFragment).show(splashScreen).commit()
                object :CountDownTimer(2000,1000){
                    override fun onTick(p0: Long) {

                    }

                    override fun onFinish() {
                        fm.beginTransaction().hide(splashScreen).show(fragmentOptimize).commit()
                        binding.bnvNav.visibility = View.VISIBLE
                        mInterstitialAd!!.show(this@MainActivity)
                    }

                }.start()

            }
            else{
                fm.beginTransaction().hide(splashScreen).show(webViewFragment).commit()

            }

        }
    }
    override fun regulateCPUAnim(finished: Boolean, fragment: Int) {
        if(fragment==1){
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                fm.beginTransaction().hide(activeFragment).show(fragmentCoolCpu).commit()
                activeFragment = fragmentCoolCpu
            } else{
                binding.bnvNav.visibility = View.GONE
                fm.beginTransaction().hide(activeFragment).show(fragmentCoolCpuAnim).commit()
                activeFragment= fragmentCoolCpuAnim
            }
        }
    }

    override fun changeBanner() {

    }

    override fun showInter() {

    }
}