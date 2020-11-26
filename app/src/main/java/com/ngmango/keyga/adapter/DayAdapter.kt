package com.ngmango.keyga.adapter

import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ngmango.keyga.R


class DayAdapter(int: Int, data: List<Int>) : BaseQuickAdapter<Int, BaseViewHolder>(int, data) {
    var pos: Int = 0
    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.setText(R.id.tv_amount, item.toString())
        val view = helper.getView<RelativeLayout>(R.id.rl_bg)
        if (helper.layoutPosition == pos){
            view.background = mContext.resources.getDrawable(R.drawable.item_blue)
            helper.setTextColor(R.id.tv_amount,mContext.resources.getColor(android.R.color.white))
        }else{
            view.background = mContext.resources.getDrawable(R.drawable.item_grey)
            helper.setTextColor(R.id.tv_amount,mContext.resources.getColor(R.color.grey))
        }
    }
}