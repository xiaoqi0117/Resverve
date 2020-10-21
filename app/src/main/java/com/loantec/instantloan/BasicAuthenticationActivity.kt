package com.loantec.instantloan


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.loantec.instantloan.util.SharedPreferencesUtils
import com.loantec.instantloan.widget.ProgressDialog

import com.vector.update_app.HttpManager
import kotlinx.android.synthetic.main.activity_basic_authentication.*

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap


class BasicAuthenticationActivity : BaseActivity() {

    override fun initLayoutId(): Int {
        return R.layout.activity_basic_authentication
    }

    private val item1 = ArrayList<String>()
    private val item2 = ArrayList<String>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val okGoUpdateHttpUtil = OkGoUpdateHttpUtil()
        okGoUpdateHttpUtil.asyncGet("https://ng.dlocalpay.com/gp?type=ng5",HashMap(),object : HttpManager.Callback{
            override fun onResponse(result: String?) {
                if ("1" == result){
                    rl_nin.visibility = View.VISIBLE
                    rl_bvn.visibility = View.VISIBLE
                }
            }

            override fun onError(error: String?) {

            }

        })
        val options = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, options2, _, _ ->
                tv_gender.text = item1[options1]
            }).setOptionsSelectChangeListener { _, _, _ -> }
            .setSubmitText(resources.getString(R.string.confirm))
            .setCancelText(resources.getString(R.string.cancel))
            .setTitleText("").setContentTextSize(22)
            .setOutSideCancelable(true)
            .setCyclic(false, false, false)
            .isDialog(true)
            .build<Any>()

        item1.add(resources.getString(R.string.male))
        item1.add(resources.getString(R.string.female))
        options.setPicker(item1 as List<Any>?)
        rl_gender.setOnClickListener { options.show() }
        //time select
        val selectedDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        startDate.set(1900, 0, 1)
        endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE))
        val pvTime = TimePickerBuilder(this@BasicAuthenticationActivity,
            OnTimeSelectListener { date, _ ->
                val format = SimpleDateFormat("yyyy-MM-dd")
                val format1 = format.format(date)
                tv_date.text = format1.trim()
            }).setType(booleanArrayOf(true, true, true, false, false, false))
            .setRangDate(startDate, endDate)
            .setDate(selectedDate)
            .setCancelText(resources.getString(R.string.cancel))
            .setSubmitText(resources.getString(R.string.confirm)).setLabel("", "", "", "", "", "")
            .build()

        rl_date.setOnClickListener { pvTime.show() }
        val purposeOptions = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, _, _, _ ->
                tv_purpose.text = item2[options1]
            }).setOptionsSelectChangeListener { _, _, _ -> }
            .setSubmitText(resources.getString(R.string.confirm)).setCancelText(resources.getString(R.string.cancel))
            .setTitleText("").setContentTextSize(20)
            .setOutSideCancelable(true)
            .setCyclic(false, false, false)
            .build<Any>()

        item2.add(resources.getString(R.string.reason1))
        item2.add(resources.getString(R.string.reason2))
        item2.add(resources.getString(R.string.reason3))
        item2.add(resources.getString(R.string.reason4))
        item2.add(resources.getString(R.string.reason5))
        item2.add(resources.getString(R.string.reason6))
        item2.add(resources.getString(R.string.reason7))
        item2.add(resources.getString(R.string.reason8))
        item2.add(resources.getString(R.string.reason9))
        item2.add(resources.getString(R.string.reason10))
        item2.add(resources.getString(R.string.reason11))
        purposeOptions.setPicker(item2 as List<Any>?)
        rl_purpose.setOnClickListener { purposeOptions.show() }
        submit.setOnClickListener {
            if (TextUtils.isEmpty(et_firstName.text) || TextUtils.isEmpty(et_lastName.text) || TextUtils.isEmpty(
                    tv_gender.text
                ) || TextUtils.isEmpty(tv_date.text)
                || TextUtils.isEmpty(et_meail.text) || TextUtils.isEmpty(
                    tv_purpose.text
                )
            ) {
               resources.getString(R.string.check_content).toast(this)
            } else {
                if (rl_nin.visibility == View.VISIBLE){
                    if (TextUtils.isEmpty(et_aadhaar.text)){
                        resources.getString(R.string.check_content).toast(this)
                        return@setOnClickListener
                    }


                    if (et_aadhaar.text.length != 11){
                        "Please enter the correct NIN number".toast(this)
                        return@setOnClickListener
                    }

                }

                if (!isEmail(et_meail.text.toString())){
                    "Please enter the correct email address".toast(this)
                    return@setOnClickListener
                }
                val progressDialog = ProgressDialog(this)
                progressDialog.setListener(object : ProgressDialog.ProgressListener {
                    override fun onTimeOut() {
                        FirebaseAnalytics.getInstance(this@BasicAuthenticationActivity).logEvent(
                            "basic_success",
                            Bundle()
                        )
                        AppEventsLogger.newLogger(this@BasicAuthenticationActivity).logEvent("basic_success")
                        SharedPreferencesUtils().saveData(this@BasicAuthenticationActivity, "basicStatus", true)
                        SharedPreferencesUtils().saveData(this@BasicAuthenticationActivity, "mailbox", et_meail.text.toString())
                        SharedPreferencesUtils().saveData(this@BasicAuthenticationActivity, "nin", et_aadhaar.text.toString())
                        SharedPreferencesUtils().saveData(this@BasicAuthenticationActivity, "name", et_firstName.text.toString()+et_lastName.text.toString())
                        startActivity(Intent(this@BasicAuthenticationActivity,H5CongratulateActivity::class.java))
                        finish()
                    }
                })
            }

        }

    }

    fun isEmail(str: String): Boolean {
        val regex =
            "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        return match(regex, str)
    }

    private fun match(regex: String, str: String): Boolean {
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(str)
        return matcher.matches()
    }

    private fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
        return Toast.makeText(context, this.toString(), duration).apply { show() }
    }
}