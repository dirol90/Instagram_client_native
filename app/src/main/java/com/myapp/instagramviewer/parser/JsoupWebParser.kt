/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.parser

import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.myapp.instagramviewer.model.InstagramMediaDataModel
import com.myapp.instagramviewer.repository.AppRepository.Companion.employeeDao
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.utils.IdGenerator
import com.myapp.instagramviewer.utils.ModelEntityConverter
import com.myapp.instagramviewer.view.fragment.FragmentMediaGrid.Companion.webView
import com.myapp.instagramviewer.viewmodel.AppViewModel.Companion.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.lang.Thread.sleep
import java.sql.Time
import java.util.*

class JsoupWebParser {

    private var prevScrollYPos = 0
    private var isEnd = false
    private var lastParsedCountElementsValue = 0
    private var webPagePath: String? = null
    private var timer: Timer? = null

    fun start(webPagePath: String) {
        if (timer == null) {
            this.webPagePath = webPagePath
            println("Running Parser")
            getChildrenFromWebPage()
        }
    }

    fun stop() {
        webPagePath = ""
        isEnd = true
    }

    private fun getChildrenFromWebPage() {
        prevScrollYPos = 0
        lastParsedCountElementsValue = 0

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        val set: WebSettings = webView.settings
        set.userAgentString =
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36"
        set.javaScriptEnabled = true
        set.domStorageEnabled = true
        set.loadsImagesAutomatically = false
        set.builtInZoomControls = false
        set.useWideViewPort = true
        set.loadWithOverviewMode = true
        set.blockNetworkImage = true

        webView.loadUrl("${webPagePath!!}/")
        println("Running ${webPagePath!!}/")

        var runFunOnce = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (!runFunOnce) {
                    runFunOnce = true
                    prepareFun()
                    println("Running Prepare fun")
                }
            }
        }
    }

    private fun prepareFun() {
        if (!isEnd) {
            Handler(Looper.getMainLooper()).post {
                webView.evaluateJavascript(
                    "(function(){ " +
                            "document.getElementsByClassName('tCibT qq7_A  z4xUb w5S7h')[0].click();})();"
                ) {}
                    scrollToBottom(webView)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                doStaff(webView)
            }, 1000)
        }
    }

    private fun scrollToBottom(view: WebView) {
        prevScrollYPos += 500
        view.scrollTo(1, prevScrollYPos)
        prevScrollYPos
    }

    private fun doStaff(view: WebView) {
        view.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") {
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

            Jsoup.parse(response)?.let {
                val element = it.getElementsByTag("img")

                if (element.hasAttr("alt") && element.hasClass("FFVAD")) {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (!isEnd) {
                            for (i in lastParsedCountElementsValue until element.size) {
                                if (element[i].attr("src").toString().isNotEmpty()
                                    && element[i].attr("class").toString().isNotEmpty()
                                    && element[i].attr("class").toString() == "FFVAD"
                                ) {
                                    println("Adding to DB $webPagePath ${element[i].attr("src")}")
                                    employeeDao?.insert(
                                        ModelEntityConverter.convertModelToEntity(
                                            InstagramMediaDataModel(
                                                IdGenerator(element[i].attr("src")).generateIdFromStringValue(),
                                                webPagePath!!,
                                                element[i].attr("src"),
                                                element[i].attr("alt"),
                                                0,
                                                0
                                            )
                                        )
                                    )
                                }
                            }
                            lastParsedCountElementsValue = element.size
                        }
                        if (!isEnd){
                            prepareFun()
                        }
                    }
                }
            }
        }
    }
}
