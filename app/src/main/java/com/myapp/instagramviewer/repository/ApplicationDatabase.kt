/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.instagramviewer.repository.dao.InstagramMediaDao
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity


@Database(entities = [InstagramMediaInfoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun instagraMediaInfoDao(): InstagramMediaDao?
}