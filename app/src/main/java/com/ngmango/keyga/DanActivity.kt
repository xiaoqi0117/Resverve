package com.ngmango.keyga

import android.content.Intent
import android.os.Bundle
import android.text.BidiFormatter
import android.text.TextDirectionHeuristics
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.just.agentweb.AgentWeb
import com.ngmango.keyga.common.AndroidInterface
import com.ngmango.keyga.common.JsCallBackInterface
import com.ngmango.keyga.util.SharedPreferencesUtils
import com.ngmango.keyga.widget.ProgressDialog
import kotlinx.android.synthetic.main.activity_pay.*


import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


class DanActivity : AppCompatActivity(), JsCallBackInterface {


    private var progressDialog: ProgressDialog? = null

    var olderInfo: String = ""

    var money: String = ""

    var rate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        val amount = SharedPreferencesUtils().getData(this, "amount", "").toString()
        val day = SharedPreferencesUtils().getData(this, "day", "").toString()
        val amountMoney = SharedPreferencesUtils().getData(this, "amountMoney", "").toString()
        val rate = SharedPreferencesUtils().getData(this, "rate", "").toString()
        val mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(ll_content, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setMainFrameErrorView(View.inflate(this, R.layout.error, null)).createAgentWeb()
            .ready()
            .go("https://storage.googleapis.com/loan_ng/app/index.html?" + System.currentTimeMillis()+"&amount=$amount&day=$day&money=$amountMoney&rate=$rate")
        mAgentWeb.jsInterfaceHolder.addJavaObject(
            "android",
            AndroidInterface(mAgentWeb, this, this)
        )
    }


    override fun Close() {

    }

    override fun Success() {

    }

    override fun Url(url: String) {

    }

    override fun Payment(money: String, rate: String) {
        AppEventsLogger.newLogger(this).logEvent("click_pay_now")
        AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT)
        this.money = money
        this.rate = rate
        val name = SharedPreferencesUtils().getData(this, "name", "") as String
        val eamil = SharedPreferencesUtils().getData(this, "mailbox", "") as String
        val nin = SharedPreferencesUtils().getData(this, "nin", "") as String
        val intent = Intent(this@DanActivity, MaiActivity::class.java)
        val locale = resources.configuration.locale

        val result =
            "https://www.entrybank.xyz/dlocal/pay_methods?fee=$money&gst=$rate&name=$name&email=$eamil&document=$nin&currency=NGN&country=NG&c=ng1&countryZipCode=${locale.country}&languageTag=${locale.toLanguageTag()}&timeZone=${getTimeZone()}&time=${Date().time}"
        olderInfo = result
        SharedPreferencesUtils().saveData(this,"olderInfo",olderInfo)
        SharedPreferencesUtils().saveData(this,"money",money)
        SharedPreferencesUtils().saveData(this,"rate",rate)
        intent.putExtra("url", result)
        startActivityForResult(intent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Prints all extras. Replace with app logic.
        val extras = data?.extras
        if ("SUCCESS" == extras?.getString("txStatus")) {
            SharedPreferencesUtils().saveData(this, "vip", true)
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product")
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, olderInfo)
            AppEventsLogger.newLogger(this)
                .logPurchase(
                    BigDecimal.valueOf(money.toDouble() + rate.toDouble()),
                    Currency.getInstance("NGR"),
                    params
                )
            // AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_PURCHASED)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        AppEventsLogger.newLogger(this).logEvent("enter_Congratulate")
    }
    private fun getTimeZone(): String? {
        val mDummyDate: Calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        mDummyDate.timeZone = now.timeZone
        mDummyDate[now[Calendar.YEAR], 11, 31, 13, 0] = 0
        return getTimeZoneText(now.timeZone, true)
    }

    private fun getTimeZoneText(tz: TimeZone?, includeName: Boolean): String? {
        val now = Date()
        val gmtFormatter = SimpleDateFormat("ZZZZ")
        gmtFormatter.timeZone = tz
        var gmtString: String = gmtFormatter.format(now)
        val bidiFormatter: BidiFormatter = BidiFormatter.getInstance()
        val l = Locale.getDefault()
        val isRtl =
            TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL
        gmtString = bidiFormatter.unicodeWrap(
            gmtString,
            if (isRtl) TextDirectionHeuristics.RTL else TextDirectionHeuristics.LTR
        )
        return if (!includeName) {
            gmtString
        } else gmtString
    }


}