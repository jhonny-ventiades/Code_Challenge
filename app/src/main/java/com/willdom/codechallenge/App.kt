package com.willdom.codechallenge

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


const val tag = "CodeChallenge"
const val sharedPreferences = "SharedCC"
//database
const val DatabaseName = "CodeChallengeDB"
const val DataBaseVersion = 1

//webservices
//todo check servers
const val webServices = "http://sandbox.bottlerocketapps.com/BR_Android_CodingExam_2015_Server"//production;
const val webServiceUser = ""
const val webServicePassword = ""
const val webServiceToken = ""

//maps
const val latitude = -17.2120964774103
const val longitude = -64.51278954803944
const val zoom = 4.6f

//formats
const val formatDate = "yyyy-MM-dd'T'HH:mm:ss"
const val formatDateShort = "yyyy-MM-dd"
const val formatLatinMinutes = "mm"
const val formatLatinDateTime = "dd/MM/yyyy HH:mm"
const val databaseFormatDate = "yyyy-MM-dd'T'HH:mm:ss"
const val formatLatinDateShort = "dd/MM/yyyy"
const val formatLatinHour = "HH:mm"

//database name
const val table_store = "store"


fun isConnectedToWifi(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkInfo: NetworkInfo? = null
    networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    return networkInfo?.isConnected ?: false
}

fun isConnectedToMobile(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkInfo: NetworkInfo? = null
    networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    return networkInfo?.isConnected ?: false
}

fun isConnected(context: Context): Boolean {
    return if (isConnectedToWifi(context)) {
        true
    } else {
        isConnectedToMobile(context)
    }
}
