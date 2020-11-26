package com.ngmango.keyga.widget

import android.app.Dialog
import android.content.Context

import android.os.Handler
import android.view.Gravity
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ngmango.keyga.R
import kotlinx.android.synthetic.main.dialog_progress.*


class ProgressDialog : Dialog {

    private var onProgressListener: ProgressListener? = null

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_progress)
        Glide.with(context).asGif().load(R.mipmap.timg6).into(iv_image)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        show()
    }

    fun setListener(listener: ProgressListener) {
        onProgressListener = listener
        Handler().postDelayed({
            dismiss()
            onProgressListener?.onTimeOut()
        }, 4000)
    }

    fun setListenerLong(listener: ProgressListener) {
        onProgressListener = listener
        Handler().postDelayed({
            dismiss()
            onProgressListener?.onTimeOut()
        }, 1000)
    }


    interface ProgressListener {
        fun onTimeOut()
    }
}