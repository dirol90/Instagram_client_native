/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.adapter.MediaAdapter
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.view.custom.ObservableWebView
import com.myapp.instagramviewer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_media_grid.*


class FragmentMediaGrid(private val viewModel: AppViewModel) : Fragment() {

    private var data: LiveData<List<InstagramMediaInfoEntity>>? = null
    private var recyclerViewState: Parcelable? = null

    companion object {
        lateinit var webView: ObservableWebView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_grid, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = web_view

        rv_grid.layoutManager = GridLayoutManager(context, 2)

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

        val mediaAdapter = MediaAdapter(viewModel)
        rv_grid.adapter = mediaAdapter

        iv_select.setOnClickListener {
            pr_bar.visibility = View.VISIBLE

            data?.removeObservers(viewLifecycleOwner)

            data = viewModel.getData(editTextTextPersonName.text.toString())
                data?.observe(viewLifecycleOwner, {
                    it?.size?.let {
                        if (it > 0) {
                            pr_bar.visibility = View.GONE
                        }
                    }
                    recyclerViewState = rv_grid.layoutManager?.onSaveInstanceState()

                    mediaAdapter.setData(it)
                    mediaAdapter.notifyDataSetChanged()

                    rv_grid.layoutManager?.onRestoreInstanceState(recyclerViewState)
                })
            iv_close_clean_action.performClick()
        }

        rv_grid.layoutManager = GridLayoutManager(context, 2)
        this.data = viewModel.getData(null)
        pr_bar.visibility = View.VISIBLE

        data = viewModel.getData(null)
        data?.observe(viewLifecycleOwner, {
            it?.size?.let {
                if (it > 0) {
                    pr_bar.visibility = View.GONE
                }
            }
            recyclerViewState = rv_grid.layoutManager?.onSaveInstanceState()

            mediaAdapter.setData(it)
            mediaAdapter.notifyDataSetChanged()

            rv_grid.layoutManager?.onRestoreInstanceState(recyclerViewState)
        })
    }

    override fun onPause() {
        super.onPause()
        recyclerViewState = rv_grid.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        rv_grid.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}