package com.miasesor.myasesortributario.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willdom.codechallenge.model.entity.Store
import com.willdom.codechallenge.table_store

@Dao
interface DStore {
//
//    @Query("select * from $article order by sequence asc")
//    fun list(): List<Article>
    @Query("select * from $table_store order by name asc")
    fun list(): List<Store>

    @Query("select count(id) from $table_store")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: List<Store>): List<Long>

    @Query("DELETE FROM $table_store")
    fun clean()
}