/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.repository

import androidx.lifecycle.LiveData
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.parser.JsoupWebParser
import com.myapp.instagramviewer.repository.dao.InstagramMediaDao
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.viewmodel.AppViewModel.Companion.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppRepository {

    companion object {
        private var appRepository: AppRepository? = null
        private var db: AppDatabase? = MyApp.instance.getInstance()?.getDatabase()
        var employeeDao: InstagramMediaDao? = db?.instagraMediaInfoDao()
    }

    private var jsoup: JsoupWebParser? = null
    private val baseUrl: String = "https://www.instagram.com/"

    fun newInstance(): AppRepository? {
        if (appRepository == null) appRepository = AppRepository()
        return appRepository
    }

    fun parseWebPageWithEndpoint(endpoint: String): LiveData<List<InstagramMediaInfoEntity>>? {
        jsoup?.stop()
        jsoup = JsoupWebParser()
        jsoup?.start(baseUrl + endpoint)

        return employeeDao?.getByPageId("$baseUrl$endpoint")
    }

    fun removeAllMedia(endpoint: String): LiveData<List<InstagramMediaInfoEntity>>? {
        viewModelScope.launch(Dispatchers.Default) { employeeDao?.deleteAll() }
        return employeeDao?.getByPageId("$baseUrl$endpoint")
    }
}