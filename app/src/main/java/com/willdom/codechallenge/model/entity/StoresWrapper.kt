package com.willdom.codechallenge.model.entity

import androidx.room.Entity
import com.willdom.codechallenge.table_store


data class StoresWrapper(
    var stores: List<Store>
): BaseEntity()