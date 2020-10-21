package com.loantec.instantloan

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.loantec.instantloan.adapter.AmountAdapter
import com.loantec.instantloan.adapter.DayAdapter
import com.loantec.instantloan.bean.ChooseData
import com.loantec.instantloan.util.SharedPreferencesUtils
import com.vector.update_app.HttpManager
import kotlinx.android.synthetic.main.activity_choose.*


class ChooseActivity : BaseActivity() {

    val jsonString: String = "{\"list1\":[10000,200000,300000,400000,500000,600000],\n" +
            "\"list2\":[90,180,200,300],\n" +
            "\"tex\":\"0.18\"\n" +
            "}"

    var chooseData = ChooseData(listOf(), listOf(), listOf(), "0.18")


    override fun initLayoutId(): Int {
        return R.layout.activity_choose
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val okGoUpdateHttpUtil = OkGoUpdateHttpUtil()
        okGoUpdateHttpUtil.asyncGet(
            "https://ng.dlocalpay.com/data?type=ng5",
            HashMap(),
            object : HttpManager.Callback {
                override fun onResponse(result: String?) {
                    if (!TextUtils.isEmpty(result)) {
                        chooseData = Gson().fromJson(result, ChooseData::class.java)
                        val gridLayoutManager = GridLayoutManager(this@ChooseActivity, 2)
                        recycle_amount.layoutManager = gridLayoutManager
                        val amountAdapter = AmountAdapter(R.layout.item_amount, chooseData.list1)
                        val gridLayoutManager2 = GridLayoutManager(this@ChooseActivity, 2)
                        recycle_day.layoutManager = gridLayoutManager2
                        val dayAdapter = DayAdapter(R.layout.item_amount, chooseData.list2)
                        amountAdapter.setOnItemClickListener { _, _, position ->
                            amountAdapter.pos = position
                            amountAdapter.notifyDataSetChanged()
                            money(chooseData, amountAdapter, dayAdapter)
                        }
                        recycle_amount.adapter = amountAdapter

                        dayAdapter.setOnItemClickListener { _, _, position ->
                            dayAdapter.pos = position
                            dayAdapter.notifyDataSetChanged()
                            money(chooseData, amountAdapter, dayAdapter)
                        }
                        recycle_day.adapter = dayAdapter
                        money(chooseData, amountAdapter, dayAdapter)
                        tv_rate.text = "(Annual interest rate ${chooseData.tex}%)"
                    }

                }

                override fun onError(error: String?) {

                }

            })


        bt_next.setOnClickListener {
            startActivity(Intent(this, BasicAuthenticationActivity::class.java))
            finish()
        }
    }

    private fun money(
        s: ChooseData,
        amountAdapter: AmountAdapter,
        dayAdapter: DayAdapter
    ) {
        tv_money.text =
            (s.list1[amountAdapter.pos] + s.list1[amountAdapter.pos] * (s.tex.toDouble() / 365 * s.list2[dayAdapter.pos])).toInt()
                .toString()
        SharedPreferencesUtils().saveData(this,"amount",s.list1[amountAdapter.pos].toString())
        SharedPreferencesUtils().saveData(this,"day",s.list2[dayAdapter.pos].toString())
        SharedPreferencesUtils().saveData(this,"amountMoney",tv_money.text.toString())
        SharedPreferencesUtils().saveData(this,"rate",s.tex)
    }

}


