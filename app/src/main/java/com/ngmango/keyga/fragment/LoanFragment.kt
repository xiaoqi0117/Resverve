package com.ngmango.keyga.fragment

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
import com.ngmango.keyga.XuanActivity
import com.ngmango.keyga.MaiActivity
import com.ngmango.keyga.R
import com.ngmango.keyga.util.SharedPreferencesUtils
import kotlinx.android.synthetic.main.fragment_loan.*
import java.math.BigDecimal
import java.util.*

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
//        val okGoUpdateHttpUtil = OkHttpUtil()
//        okGoUpdateHttpUtil.asyncGet(
//            "https://www.entrybank.xyz/show?type=ng8a",
//            HashMap(),
//            object : HttpManager.Callback {
//                override fun onResponse(result: String?) {
//                    if (!TextUtils.isEmpty(result)) {
//                        tv_amount.text = "₦ $result"
//                    }
//
//                }
//
//                override fun onError(error: String?) {
//                    tv_amount.text = "₦ 50,000"
//                }
//
//            })
        //tv_amount.text = "₦ 75,000"

        bt_next.setOnClickListener {
            val url = SharedPreferencesUtils().getData(activity as Context, "url", "").toString()
            if (!TextUtils.isEmpty(url)) {
                val intent = Intent(activity, MaiActivity::class.java)
                intent.putExtra("url", url)
                startActivityForResult(intent, 1)
            } else {
                startActivity(Intent(activity, XuanActivity::class.java))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Prints all extras. Replace with app logic.
        val extras = data?.extras
        if ("SUCCESS" == extras?.getString("txStatus")) {
            val olderInfo = SharedPreferencesUtils()
                .getData(activity as Context, "olderInfo", "").toString()
            val money = SharedPreferencesUtils().getData(activity as Context, "money", "").toString()
            val rate = SharedPreferencesUtils().getData(activity as Context, "rate", "").toString()
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product")
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, olderInfo)
            AppEventsLogger.newLogger(activity as Context)
                .logPurchase(
                    BigDecimal.valueOf(money.toDouble() + rate.toDouble()),
                    Currency.getInstance("NGN"),
                    params
                )
            // AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_PURCHASED)
        }

        "else if (\"Error\" == extras?.getString(\"txStatus\")) {\n" +
                "            \n" +
                "        }"
    }

}