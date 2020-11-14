/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.repository

import androidx.lifecycle.LiveData
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.parser.JsoupWebParser
import com.myapp.instagramviewer.repository.dao.InstagramMediaDao
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity
import com.myapp.instagramviewer.viewmodel.AppViewModel.Companion.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class AppRepository {

    companion object {
        private var appRepository : AppRepository? = null
        var db: AppDatabase? = MyApp.instance.getInstance()?.getDatabase()
        var employeeDao: InstagramMediaDao? = db!!.instagraMediaInfoDao()
    }

    private val baseUrl : String = "https://www.instagram.com/"

    fun newInstance(): AppRepository ? {
        if (appRepository == null) appRepository = AppRepository()
        return appRepository
    }

    fun parseWebPageWithEndpoint(endpoint: String) : LiveData<List<InstagraMediaInfoEntity>>?{
        JsoupWebParser().newInstance(baseUrl + endpoint)?.run()
        return employeeDao?.let { it.all }
    }

    fun removeAllMedia() : LiveData<List<InstagraMediaInfoEntity>>?{
        viewModelScope.launch(Dispatchers.IO) { employeeDao?.deleteAll() }
        return employeeDao?.let { it.all }
    }
}