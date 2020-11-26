package com.ngmango.keyga

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.multidex.MultiDex


import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior

import java.security.MessageDigest


class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
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