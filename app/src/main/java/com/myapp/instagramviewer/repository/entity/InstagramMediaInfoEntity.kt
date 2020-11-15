/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.repository.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class InstagramMediaInfoEntity (@PrimaryKey val id : String, val pageId : String, val imagePath : String, val imageDescription: String, val commentCounter : Int, val likeCounter : Int) : Serializable