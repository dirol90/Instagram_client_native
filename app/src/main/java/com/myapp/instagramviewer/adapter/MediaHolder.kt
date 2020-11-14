/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity


class MediaHolder(itemView: View, var width: Int) : RecyclerView.ViewHolder(itemView) {
    private val articleImageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_media)
//    private val titleTextView: TextView = itemView.findViewById<TextView>(R.id.tv_extra_info)

    private var currentPage: InstagraMediaInfoEntity? = null

    fun updateWithMedia(media: InstagraMediaInfoEntity){
        currentPage = media

//        titleTextView.text = media.pageId

        if(media.imagePath != null)
            Glide.with(itemView.context)
                .load(media.imagePath)
                .apply(RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(width-48, width-48))
                .into(articleImageView)
    }
}