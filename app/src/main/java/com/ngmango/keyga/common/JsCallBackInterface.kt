package com.ngmango.keyga.common

interface JsCallBackInterface {

    fun Close()

    fun Success()

    fun Url(url:String)

    fun Payment(money:String,rate:String)

}