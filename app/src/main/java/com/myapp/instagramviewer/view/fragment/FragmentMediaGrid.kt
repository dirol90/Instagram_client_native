/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import AppSharedPreferences
import AppSharedPreferences.get
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.adapter.MediaAdapter
import com.myapp.instagramviewer.listener.SnapOnScrollListener
import com.myapp.instagramviewer.listener.SnapOnScrollListener.Companion.NOTIFY_ON_SCROLL
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.utils.KeyBoardHider
import com.myapp.instagramviewer.view.custom.ObservableWebView
import com.myapp.instagramviewer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_media_grid.*


class FragmentMediaGrid(private val viewModel: AppViewModel) : Fragment() {

    private var data: LiveData<List<InstagramMediaInfoEntity>>? = null

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
            KeyBoardHider().hideKeyboard(view)
        }

        val mediaAdapter = MediaAdapter(viewModel)
        rv_grid.adapter = mediaAdapter

        iv_select.setOnClickListener {
            progressStart(mediaAdapter)
        }

        editTextTextPersonName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                progressStart(mediaAdapter)
                true
            } else false
        }

        rv_grid.layoutManager = GridLayoutManager(context, 2)

        val lastSearch: String? = AppSharedPreferences.customPrefs(
            MyApp.context,
            AppSharedPreferences.CUSTOM_PREF
        ).get("lastSearch")

        this.data = viewModel.getData(lastSearch?.toLowerCase())
        pr_bar.visibility = View.VISIBLE

        data?.observe(viewLifecycleOwner, {
            it?.size?.let {
                println("ELEMENTS IN LIST $it")
                if (it > 0) {
                    pr_bar.visibility = View.GONE
                }
            }

            mediaAdapter.setData(it)
            mediaAdapter.notifyDataSetChanged()
            recyclerScrollToPos()
        })


        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_grid)

        rv_grid.addOnScrollListener(SnapOnScrollListener(snapHelper, NOTIFY_ON_SCROLL) { position ->
            recyclerSavePos(position)
        })
    }

    private fun progressStart(mediaAdapter: MediaAdapter) {
        pr_bar.visibility = View.VISIBLE

        data?.removeObservers(viewLifecycleOwner)

        val shText = editTextTextPersonName.text.toString()

        data = viewModel.getData(shText.toLowerCase())
        data?.observe(viewLifecycleOwner, {

            AppSharedPreferences.customPrefs(
                MyApp.context,
                AppSharedPreferences.CUSTOM_PREF
            ).edit()?.putString(
                "lastSearch",
                shText
            )?.apply()

            it?.size?.let {
                println("ELEMENTS IN LIST $it")
                if (it > 0) {
                    pr_bar.visibility = View.GONE
                }
            }

            mediaAdapter.setData(it)
            mediaAdapter.notifyDataSetChanged()
            recyclerScrollToPos()

        })
        iv_close_clean_action.performClick()
        recyclerClearPos()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerClearPos()
    }

    override fun onResume() {
        super.onResume()
        recyclerScrollToPos()
    }

    private fun recyclerScrollToPos() {
        val lastPos: Int? = AppSharedPreferences.customPrefs(
            MyApp.context,
            AppSharedPreferences.CUSTOM_PREF
        ).getInt("lastPos", 0)
        lastPos?.let { rv_grid.scrollToPosition(it) }
    }

    private fun recyclerClearPos() {
        AppSharedPreferences.customPrefs(
            MyApp.context,
            AppSharedPreferences.CUSTOM_PREF
        ).edit()?.putInt(
            "lastPos",
            0
        )?.apply()
    }

    private fun recyclerSavePos(position: Int) {
        AppSharedPreferences.customPrefs(
            MyApp.context,
            AppSharedPreferences.CUSTOM_PREF
        ).edit()?.putInt(
            "lastPos",
            position
        )?.apply()
    }

}