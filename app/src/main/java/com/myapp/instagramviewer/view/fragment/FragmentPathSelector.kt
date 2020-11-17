/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.viewmodel.AppViewModel


class FragmentPathSelector(private val viewModel: AppViewModel) : Fragment() {


    private var button: Button? = null
    private var buttonSpy: Button? = null
    private var requestUrl: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_path_selector, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.btn_login)
        buttonSpy = view.findViewById(R.id.btn_login_spy)
        button?.setOnClickListener {
            requestUrl = view.context.resources.getString(R.string.base_url_api) +
                    "oauth/authorize/?client_id=${view.context.resources.getString(R.string.client_id)}&redirect_uri=${view.context.resources.getString(R.string.base_url)}&scope=user_profile,user_media&response_type=code"

            val webView = view.findViewById<WebView>(R.id.webView)
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.loadUrl(requestUrl!!)

            val webViewClient: WebViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    viewModel.detectUrl(
                        url = url,
                        redirectUrl = view.context.resources.getString(R.string.redirect_url),
                        userInfo = view.context.getString(R.string.get_user_info_url)
                    )
                    return false
                }
            }
            webView.webViewClient = webViewClient
        }

        buttonSpy?.setOnClickListener {
            val newFragment = FragmentMediaGrid(viewModel)
            val activity: AppCompatActivity = view.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fl_placeholder, newFragment).addToBackStack(null).commit()
        }
    }
}
