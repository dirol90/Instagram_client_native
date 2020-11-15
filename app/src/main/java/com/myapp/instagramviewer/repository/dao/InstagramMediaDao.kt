/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity

@Dao
interface InstagramMediaDao {
    @get:Query("SELECT * FROM instagrammediainfoentity")
    val all: LiveData<List<InstagramMediaInfoEntity>>

    @Query("SELECT * FROM instagrammediainfoentity WHERE id = :id")
    fun getById(id: String): LiveData<List<InstagramMediaInfoEntity>>

    @Query("SELECT * FROM instagrammediainfoentity WHERE pageId = :pageId")
    fun getByPageId(pageId: String): LiveData<List<InstagramMediaInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(instagramMediaInfoEntity: InstagramMediaInfoEntity)

    @Update
    fun update(instagramMediaInfoEntity: InstagramMediaInfoEntity)

    @Delete
    fun delete(instagramMediaInfoEntity: InstagramMediaInfoEntity)

    @Query("DELETE FROM instagrammediainfoentity")
    fun deleteAll()
}