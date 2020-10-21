package com.loantec.instantloan.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import com.just.agentweb.AgentWeb


class AndroidInterface constructor(
    private var agent: AgentWeb, private var context: Context, private var jsCallBackInterface: JsCallBackInterface
) {

    private val deliver = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun Close() {
        deliver.post {
            jsCallBackInterface.Close()
        }
    }

    @JavascriptInterface
    fun Success() {
        deliver.post {
            jsCallBackInterface.Success()
        }
    }

    @JavascriptInterface
    fun Url(url:String){
        deliver.post {
            jsCallBackInterface.Url(url)
        }
    }

    @JavascriptInterface
    fun payment(amount: String, rate: String) {
        deliver.post {
            jsCallBackInterface.Payment(amount,rate)
        }
    }
}