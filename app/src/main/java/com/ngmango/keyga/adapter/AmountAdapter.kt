package com.ngmango.keyga.adapter




import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ngmango.keyga.R


class AmountAdapter(int: Int,data: List<Int>) : BaseQuickAdapter<Int, BaseViewHolder>(int,data) {

    var pos : Int = 0

    override fun convert(holder: BaseViewHolder, item: Int) {
        holder.setText(R.id.tv_amount, item.toString())
        val view = holder.getView<RelativeLayout>(R.id.rl_bg)
        if (holder.layoutPosition == pos){
            view.background = mContext.resources.getDrawable(R.drawable.item_blue)
            holder.setTextColor(R.id.tv_amount,mContext.resources.getColor(android.R.color.white))
        }else{
            view.background = mContext.resources.getDrawable(R.drawable.item_grey)
            holder.setTextColor(R.id.tv_amount,mContext.resources.getColor(R.color.grey))
        }

    }
}