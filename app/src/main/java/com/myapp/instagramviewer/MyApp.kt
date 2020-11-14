/**
 * Created by Tsymbalyuk Konstantin from TestApp on 13.11.2020.
 */
package com.myapp.instagramviewer

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.myapp.instagramviewer.repository.AppDatabase


class MyApp : Application() {
    companion object {
        lateinit var context : Context
        lateinit var instance : MyApp
    }

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        instance = this

        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

    fun getInstance(): MyApp? {
        return instance
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

}
