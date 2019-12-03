package com.willdom.codechallenge.model.entity

import android.util.Log
import androidx.annotation.NonNull
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

open class BaseEntity@Ignore constructor():Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0




}
