/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.adapter.MediaAdapter
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity
import com.myapp.instagramviewer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_media_grid.*


class FragmentMediaGrid : Fragment() {
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_grid, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_grid.layoutManager = GridLayoutManager(context, 2)
        viewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        iv_open_ll_action.visibility = View.VISIBLE
        iv_open_ll_action.setOnClickListener {
            iv_open_ll_action.visibility = View.GONE
            ll_hidable_with_et.visibility = View.VISIBLE
        }

        ll_hidable_with_et.visibility = View.GONE
        iv_close_clean_action.setOnClickListener {
            iv_open_ll_action.visibility = View.VISIBLE
            ll_hidable_with_et.visibility = View.GONE
            editTextTextPersonName.text.clear()
        }

        iv_select.setOnClickListener {
            viewModel.getData(editTextTextPersonName.text.toString())
            pr_bar.visibility = View.VISIBLE
        }

        rv_grid.layoutManager = GridLayoutManager(context, 2)
        val mediaAdapter : MediaAdapter = MediaAdapter()
        rv_grid.adapter = mediaAdapter

        val data: LiveData<List<InstagraMediaInfoEntity>>? = viewModel.getData(null)
        pr_bar.visibility = View.VISIBLE

        data?.observe(viewLifecycleOwner, {
            println(it.toString())
            mediaAdapter.setData(it)
            pr_bar.visibility = View.GONE
        })


    }
}