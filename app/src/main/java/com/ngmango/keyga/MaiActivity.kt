package com.ngmango.keyga

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger
import com.just.agentweb.AgentWeb
import com.ngmango.keyga.common.AndroidInterface
import com.ngmango.keyga.common.JsCallBackInterface
import com.ngmango.keyga.util.SharedPreferencesUtils
import com.ngmango.keyga.R


import kotlinx.android.synthetic.main.activity_pay.*


class MaiActivity : AppCompatActivity(), JsCallBackInterface {


    override fun Close() {
        AppEventsLogger.newLogger(this).logEvent("error")
        val intent = Intent()
        intent.putExtra("txStatus","Error")
        setResult(1,intent)
        finish()
    }

    override fun Success() {
        AppEventsLogger.newLogger(this).logEvent("success")
        val intent = Intent()
        intent.putExtra("txStatus","SUCCESS")
        setResult(1,intent)
    }

    override fun Url(url: String) {
        SharedPreferencesUtils().saveData(this,"url",url)
    }

    override fun Payment(money: String, rate: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        val mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(ll_content, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setMainFrameErrorView(View.inflate(this, R.layout.error,null))
            .createAgentWeb()
            .ready()
            .go(intent.getStringExtra("url"))
        mAgentWeb.jsInterfaceHolder.addJavaObject("android",
            AndroidInterface(mAgentWeb, this, this)
        )
    }

}