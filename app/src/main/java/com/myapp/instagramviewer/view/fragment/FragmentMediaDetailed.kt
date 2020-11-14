/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.viewmodel.AppViewModel


class FragmentMediaDetailed : Fragment() {
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_detailed, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppViewModel::class.java)


    }
}
