package com.supers.cleaner.phonemaster.ui
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
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
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

import androidx.core.content.ContextCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.AlarmBroadCastReceiver
import com.supers.cleaner.phonemaster.AlarmReceiver
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.Utils.PreferencesProvider
import com.supers.cleaner.phonemaster.interfaces.AskPermissions
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.services.CleanService
import java.util.*


class MainActivity : AppCompatActivity(), IFragment, AskPermissions, IBanner{
    override fun requestBlueTooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            askForPermission(android.Manifest.permission.BLUETOOTH_CONNECT, 1)
        }
        else{
            MyApplication.bluetoothPermissionGranted = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("sukawtf", requestCode.toString())
    }

    lateinit var binding:ActivityMainBinding
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
private fun setCustomNotification(){
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.custom_notification_layout)
//    val notificationIntent = Intent()
//  //  intent.putExtra("whattodo","boost")
//    val stackBuilder = TaskStackBuilder.create(applicationContext)
//stackBuilder.addNextIntent(notificationIntent)
////
//    val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//////
//    notificationLayout.setOnClickPendingIntent(R.id.ll_boost, pendingIntent)
    val customNotification = NotificationCompat.Builder(applicationContext, AlarmReceiver.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
        .setOngoing(true)
        .build()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            AlarmReceiver.CHANNEL_ID,
            resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        channel.setSound(alarmSound, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }


    notificationManager.notify(0, customNotification)
}
    private fun setNotification() {
        PreferencesProvider.getInstance().edit().putString("state_Head", resources.getString(R.string.notif_head))
            .putString("state_Body", resources.getString(R.string.notif_body))
            .apply()

        val calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 20)
        calendar.set(Calendar.MINUTE,43)
        calendar.set(Calendar.SECOND, 0)

        //if user sets the alarm after their preferred time has already passed that day
        if (now.after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
Log.d("sukatime", calendar.time.toString())
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
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

        fm.beginTransaction().add(R.id.cl_main, fragmentOptimize).commit()
        fm.beginTransaction().hide(splashScreen).commit()

    }

    private var handler: Handler? = null
    var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    lateinit var finalUltraModeUsageTime:String
    lateinit var finalExtremeModeUsageTime:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
setCustomNotification()
        setNotification()
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //TODO: if android <= Nougat startService
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                    val i = Intent(this@MainActivity, CleanService::class.java)
                    i.putExtra("junk", "sa")
                    this@MainActivity.startService(i)
                } else {
                }

            }
        }
        val myIntent = Intent(this@MainActivity, AlarmBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val firingCal = Calendar.getInstance()
        val currentCal = Calendar.getInstance()
        val randomNum = 6 + (Math.random() * 18).toInt()

        val randomNum2 = 6 + (Math.random() * 18).toInt()
        firingCal.set(Calendar.HOUR_OF_DAY, randomNum) // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, randomNum2) // Particular minute
        firingCal.set(Calendar.SECOND, 0) // particular second

        var intendedTime = firingCal.timeInMillis
        val currentTime = currentCal.timeInMillis

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1)
            intendedTime = firingCal.timeInMillis

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        }


        adRequest = AdRequest.Builder().build()

        activeFragment   = fragmentOptimize

        val infielder = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, infielder)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        splashScreen = SplashScreen(this)

        fm.beginTransaction().add(R.id.cl_main, splashScreen).commit()
         finalUltraModeUsageTime = String.format("%.02f", level*10/100F-0.5F+2).replace(".", " ч. ").replace(",", " ч. ")+" м."
         finalExtremeModeUsageTime = String.format("%.02f", level*10/100F-0.5F+4).replace(".", " ч. ").replace(",", " ч. ")+" м."
        fragmentExtremeModeAnim = FragmentExtremePowerSavingAnim( this,this)
        fragmentExtremeMode = FragmentExtremePowerSaving( finalExtremeModeUsageTime,this, this)
        fragmentUltraMode  = FragmentUltraPowerSaving(finalUltraModeUsageTime, this,this)
        fragmentUltraModeAnim  = FragmentUltraPowerSavingAnim(this,this)


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


        MobileAds.initialize(this) {}

        InterstitialAd.load(this,getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (!MyApplication.showuserpolicy){
                    addFragments()

                    showInter()
                    mInterstitialAd = null

                }
                MyApplication.adloaded=true
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                if(!MyApplication.showuserpolicy){

                    interstitialAd.show(this@MainActivity)
                    addFragments()


                }
                MyApplication.adloaded=true
                mInterstitialAd = interstitialAd

            }
        })
        requestBlueTooth()
    }


    override fun regulate(finished:Boolean, fragment:Int) {
        if(fragment==1){
            if(finished){
binding.bnvNav.visibility = View.VISIBLE
              fm.beginTransaction().hide(activeFragment).show(fragmentEnergySaving).commit()
               activeFragment = fragmentEnergySaving
                object :CountDownTimer(100,1000){
                    override fun onTick(p0: Long) {

                    }

                    override fun onFinish() {
                        if (  MyApplication.mInterstitialAd != null) {
                            MyApplication.mInterstitialAd?.show(this@MainActivity)
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                        }
                    }

                }.start()

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
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }

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
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
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
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
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
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
            } else{
                binding.bnvNav.visibility = View.GONE
                supportFragmentManager.beginTransaction().detach(activeFragment).attach( fragmentExtremeModeAnim).commit()
                fm.beginTransaction().hide(activeFragment).show(fragmentExtremeModeAnim).commit()
            activeFragment= fragmentExtremeModeAnim
            }
        }
        else if(fragment==6)
        {
            if(finished){
                binding.bnvNav.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().detach(activeFragment).attach( fragmentEnergySaving).commit()
                fm.beginTransaction().hide(activeFragment).show(fragmentClean).commit()
              activeFragment = fragmentClean
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
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
                if (  MyApplication.mInterstitialAd != null) {
                    MyApplication.mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
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