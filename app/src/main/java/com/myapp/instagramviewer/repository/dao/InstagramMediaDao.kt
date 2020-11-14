/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity

@Dao
interface InstagramMediaDao {
    @get:Query("SELECT * FROM instagramediainfoentity")
    val all: LiveData<List<InstagraMediaInfoEntity>>

    @Query("SELECT * FROM instagramediainfoentity WHERE id = :id")
    fun getById(id: String): LiveData<List<InstagraMediaInfoEntity>>

    @Query("SELECT * FROM instagramediainfoentity WHERE pageId = :pageId")
    fun getByPageId(pageId: String): LiveData<List<InstagraMediaInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(employee: InstagraMediaInfoEntity)

    @Update
    fun update(employee: InstagraMediaInfoEntity)

    @Delete
    fun delete(employee: InstagraMediaInfoEntity)

    @Query("DELETE FROM instagramediainfoentity")
    fun deleteAll()
}