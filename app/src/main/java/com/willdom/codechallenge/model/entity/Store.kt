package com.willdom.codechallenge.model.entity

import androidx.room.Entity
import com.willdom.codechallenge.table_store


@Entity(tableName = table_store)
data class Store(
    var address: String ="",
    var city: String ="",
    var name: String ="",
    var latitude: String ="",
    var zipcode: String ="",
    var storeLogoURL: String ="",
    var phone: String ="",
    var longitude: String ="",
    var storeID: String ="",
    var state: String =""
): BaseEntity()