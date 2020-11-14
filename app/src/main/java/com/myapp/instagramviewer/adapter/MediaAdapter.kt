/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.adapter

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.instagramviewer.MyApp.Companion.context
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity


class MediaAdapter() : RecyclerView.Adapter<MediaHolder>() {
    var currentResults: List<InstagraMediaInfoEntity> = listOf()

    fun setData(currentResults: List<InstagraMediaInfoEntity>){
        this.currentResults = currentResults
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        var page = currentResults.get(position)
        page.let { holder.updateWithMedia(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x

        var cardItem = LayoutInflater.from(parent.context).inflate(
            R.layout.card_media,
            parent,
            false
        )
        return MediaHolder(cardItem, width/2)
    }
}