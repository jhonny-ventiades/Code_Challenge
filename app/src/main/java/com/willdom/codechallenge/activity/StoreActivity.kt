package com.willdom.codechallenge.activity

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.willdom.codechallenge.R
import com.willdom.codechallenge.model.entity.Store
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.store_item.view.*
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.core.app.ActivityCompat
import java.util.*


class StoreActivity : AppCompatActivity() {
    companion object {
        const val KEY_STORE: String = "store"
    }
    private val REQUEST_PERMISION_CALL = 11
    
    private lateinit var store: Store
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)


        if (intent.hasExtra(KEY_STORE)) {
            store = intent.getSerializableExtra(KEY_STORE) as Store
        } else{
            this.finish()
        }

        displayInfo()

        phone_number.setOnClickListener { call() }
        go_maps.setOnClickListener{
            val uri = String.format(Locale.ENGLISH, "geo:%s,%s", store.latitude, store.longitude)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISION_CALL && resultCode == Activity.RESULT_OK) {
            call()
        }
    }

    private fun displayInfo(){
        phone_number.text = setUnderline(store.phone)
        address.text = store.address +"\n ${store.city}, ${store.state}  ${store.zipcode}"

        Glide.with(this)
            .load(store.storeLogoURL)
            .apply(RequestOptions().placeholder(R.drawable.ic_photo))
            .into(image)
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${store.phone}")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PERMISION_CALL)
            }
            return
        }
        startActivity(intent)
    }

    private fun setUnderline(value: String): SpannableString {
        val content = SpannableString(value)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }
}

