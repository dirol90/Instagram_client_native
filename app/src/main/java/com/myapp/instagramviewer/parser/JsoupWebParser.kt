/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.parser

import android.graphics.Bitmap
import android.os.Handler
import android.webkit.*
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.repository.AppRepository.Companion.employeeDao
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity
import com.myapp.instagramviewer.utils.IdGenerator
import com.myapp.instagramviewer.viewmodel.AppViewModel.Companion.viewModelScope
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupWebParser {

    companion object {
        private var jsoupWebParser: JsoupWebParser? = null
        private var webPagePath: String? = null
    }

    fun newInstance(path: String): JsoupWebParser? {
        if (jsoupWebParser == null) jsoupWebParser = JsoupWebParser()
        webPagePath = path
        return jsoupWebParser
    }

    fun run(): JsoupWebParser? {
        getChildrenFromWebPage()
        return jsoupWebParser
    }

    private fun getChildrenFromWebPage() {

        val webView = WebView(MyApp.context)
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }
        val set: WebSettings = webView.settings
        set.userAgentString =
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36"
        set.javaScriptEnabled = true
        set.domStorageEnabled = true
        set.loadsImagesAutomatically = true
        set.builtInZoomControls = false
        set.useWideViewPort = true
        set.loadWithOverviewMode = true

        webView.loadUrl("${webPagePath!!}/")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
//                view.loadUrl("javascript:(function(){"+
//                        "l=document.getElementById('tCibT qq7_A  z4xUb w5S7h');"+
//                        "e=document.createEvent('HTMLEvents');"+
//                        "e.initEvent('click',true,true);"+
//                        "l.dispatchEvent(e);"+
//                        "})()")
                Handler().postDelayed(
                    {
                        view.evaluateJavascript(
                            "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();"
                        ) {
                            val response = it.replace("\\u003C", "<")
                                .replaceFirst("\"", "")
                                .replace("\\\"", "\"")
                                .replace("\\n", "")
                                .replace("\\\\u0302", "^")
                                .replace("\\\\u0026", "&")
                                .replace("\\\\", "")
                                .replace("        ", " ")
                                .replace(" \"", "")
                                .dropLast(1)
                            val responseInString = Jsoup.parse(response)

                            getMediaFromChildren(responseInString)

                        }
                    },
                    2000 // value in milliseconds
                )

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                println(url)
            }
        }
    }


    private fun getMediaFromChildren(baseChildrenDocument: Document?) {
        baseChildrenDocument?.let {
            var element = it.getElementsByTag("img")

            if(element.hasAttr("alt")) {
                println(element.toString())
                viewModelScope.launch(Dispatchers.IO) {
                    element.forEach {
                        if (it.attr("src").isNotEmpty())
                            employeeDao?.insert(InstagraMediaInfoEntity(IdGenerator(it.attr("src")).generateIdFromStringValue(),
                                webPagePath!!,
                                it.attr("src"),
                                10,
                                10
                            ))
                    }
                }
            }
        }
    }
}
