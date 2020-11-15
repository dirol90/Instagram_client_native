/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.adapter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.view.fragment.FragmentMediaDetailed
import com.myapp.instagramviewer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_media_grid.*


class MediaHolder(itemView: View, private val imageWidth: Int, private val viewModel: AppViewModel) : RecyclerView.ViewHolder(itemView) {
    private val articleImageView: ImageView = itemView.findViewById(R.id.iv_media)
    private var currentPage: InstagramMediaInfoEntity? = null

    fun updateWithMedia(media: InstagramMediaInfoEntity, position: Int ){
        currentPage = media

        Glide.with(itemView.context)
            .load(media.imagePath)
            .override(imageWidth, imageWidth)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(articleImageView)

        val newFragment = FragmentMediaDetailed(viewModel)

        articleImageView.setOnClickListener {
            val activity: AppCompatActivity = itemView.context as AppCompatActivity

            viewModel.setSelectedMedia(currentPage!!)

            activity.supportFragmentManager.beginTransaction().replace(R.id.fl_placeholder, newFragment).addToBackStack(null).commit()

            recyclerSavePos(position)
        }
    }

    private fun recyclerSavePos(position: Int){
        AppSharedPreferences.customPrefs(
            MyApp.context,
            AppSharedPreferences.CUSTOM_PREF
        ).edit()?.putInt(
            "lastPos",
            position
        )?.apply()
    }
}

