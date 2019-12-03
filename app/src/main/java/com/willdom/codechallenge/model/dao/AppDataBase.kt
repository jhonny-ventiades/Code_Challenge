package com.miasesor.myasesortributario.model.data

import android.content.Context
import android.util.Log.d
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.willdom.codechallenge.DataBaseVersion
import com.willdom.codechallenge.DatabaseName
import com.willdom.codechallenge.DateConverter
import com.willdom.codechallenge.model.entity.Store
import com.willdom.codechallenge.tag
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Store::class], version = DataBaseVersion, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun dStore(): DStore

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null


        fun getAppDataBase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DatabaseName)
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }

}

