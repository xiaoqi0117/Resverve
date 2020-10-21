package com.loantec.instantloan.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.loantec.instantloan.ChooseActivity
import com.loantec.instantloan.H5PayActivity
import com.loantec.instantloan.OkGoUpdateHttpUtil
import com.loantec.instantloan.R
import com.loantec.instantloan.util.SharedPreferencesUtils
import com.vector.update_app.HttpManager
import kotlinx.android.synthetic.main.fragment_loan.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

class LoanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loan,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val okGoUpdateHttpUtil = OkGoUpdateHttpUtil()
        okGoUpdateHttpUtil.asyncGet(
            "https://ng.dlocalpay.com/show?type=ng5",
            HashMap(),
            object : HttpManager.Callback {
                override fun onResponse(result: String?) {
                    if (!TextUtils.isEmpty(result)) {
                        tv_amount.text = "₦ $result"
                    }

                }

                override fun onError(error: String?) {
                    tv_amount.text = "₦ 50,000"
                }

            })

        bt_next.setOnClickListener {
            val url = SharedPreferencesUtils().getData(activity as Context, "url", "").toString()
            if (!TextUtils.isEmpty(url)) {
                val intent = Intent(activity, H5PayActivity::class.java)
                intent.putExtra("url", url)
                startActivityForResult(intent, 1)
            } else {
                startActivity(Intent(activity, ChooseActivity::class.java))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Prints all extras. Replace with app logic.
        val extras = data?.extras
        if ("SUCCESS" == extras?.getString("txStatus")) {
            val olderInfo = SharedPreferencesUtils().getData(activity as Context, "olderInfo", "").toString()
            val money = SharedPreferencesUtils().getData(activity as Context, "money", "").toString()
            val rate = SharedPreferencesUtils().getData(activity as Context, "rate", "").toString()
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product")
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, olderInfo)
            AppEventsLogger.newLogger(activity as Context)
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
            FirebaseAnalytics.getInstance(activity as Context)
                .logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
        }

        "else if (\"Error\" == extras?.getString(\"txStatus\")) {\n" +
                "            \n" +
                "        }"
    }

}