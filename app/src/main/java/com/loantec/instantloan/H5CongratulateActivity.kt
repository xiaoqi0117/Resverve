package com.loantec.instantloan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.just.agentweb.AgentWeb
import com.loantec.instantloan.common.AndroidInterface
import com.loantec.instantloan.common.JsCallBackInterface
import com.loantec.instantloan.util.SharedPreferencesUtils
import com.loantec.instantloan.widget.ProgressDialog
import kotlinx.android.synthetic.main.activity_pay.*


import java.math.BigDecimal
import java.util.*


class H5CongratulateActivity : AppCompatActivity(), JsCallBackInterface {


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
            .go("https://storage.googleapis.com/kingcash/abroad_dc_lrly5/index.html?" + System.currentTimeMillis()+"&amount=$amount&day=$day&money=$amountMoney&rate=$rate")
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
        FirebaseAnalytics.getInstance(this).logEvent("click_pay_now", Bundle())
        FirebaseAnalytics.getInstance(this)
            .logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, Bundle())
        this.money = money
        this.rate = rate
        //https://ng.dlocalpay.com/dlocal/pay_methods?fee=1&gst=1&name=xiao&email=thiago@example.com&document=13482420190&currency=NGN&country=NG
        val name = SharedPreferencesUtils().getData(this, "name", "") as String
        val eamil = SharedPreferencesUtils().getData(this, "mailbox", "") as String
        val nin = SharedPreferencesUtils().getData(this, "nin", "") as String
        val intent = Intent(this@H5CongratulateActivity, H5PayActivity::class.java)
        val result =
            "https://ng.dlocalpay.com/dlocal/pay_methods?fee=$money&gst=$rate&name=$name&email=$eamil&document=$nin&currency=NGN&country=NG&c=ng5"
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
                    Currency.getInstance("INR"),
                    params
                )
            // AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_PURCHASED)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "NGN")
            bundle.putString(FirebaseAnalytics.Param.AFFILIATION, "")
            bundle.putString(FirebaseAnalytics.Param.ITEMS, "1")
            bundle.putString(FirebaseAnalytics.Param.SHIPPING, "vip")
            bundle.putString(FirebaseAnalytics.Param.TAX, rate)
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, olderInfo)
            bundle.putString(FirebaseAnalytics.Param.VALUE, money)
            FirebaseAnalytics.getInstance(this)
                .logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        AppEventsLogger.newLogger(this).logEvent("enter_Congratulate")
    }


}