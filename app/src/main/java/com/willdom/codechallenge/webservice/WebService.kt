package com.willdom.codechallenge.webservice

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.reflect.TypeToken
import com.willdom.codechallenge.formatDateShort
import com.willdom.codechallenge.model.entity.Store
import com.willdom.codechallenge.model.entity.StoresWrapper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WebService @SuppressLint("SimpleDateFormat")
constructor(private val context: Context) : Web(context) {

    @Throws(Exception::class)
    fun stores(): StoresWrapper{
        return super.get("/stores.json", StoresWrapper::class.java)
    }


}
