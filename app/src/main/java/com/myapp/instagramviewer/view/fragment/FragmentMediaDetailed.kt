/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.databinding.FragmentMediaDetailedBinding
import com.myapp.instagramviewer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_media_detailed.*


class FragmentMediaDetailed(private val viewModel: AppViewModel) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMediaDetailedBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_media_detailed, container, false
        )

        binding.lifecycleOwner = activity
        binding.setVariable(1, viewModel.getSelectedMedia())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view.context)
            .load(viewModel.getSelectedMedia()?.imagePath)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(iv_media_detailed)
    }
}
