package com.loantec.instantloan

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log


import com.bytedance.boost_multidex.BoostMultiDex
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior

import java.security.MessageDigest


class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        BoostMultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
       // val locale = resources.configuration.locale
       // AppEventsLogger.newLogger(this).logEvent(locale.toLanguageTag())

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.e("HashKey=========", KeyHash)
            }
        } catch (e: Exception) {

        }

    }


}