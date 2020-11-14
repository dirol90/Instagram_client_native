/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.instagramviewer.repository.AppRepository
import com.myapp.instagramviewer.repository.entity.InstagraMediaInfoEntity
import kotlinx.coroutines.*
import org.w3c.dom.Document


class AppViewModel : ViewModel() {

    companion object {
        var viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    }

    private val appRepository = AppRepository().newInstance()
    private var data: LiveData<List<InstagraMediaInfoEntity>>? = null

    fun getData(str: String?): LiveData<List<InstagraMediaInfoEntity>>? {
        var searchPhrase = str
        if (data == null) {
            data = MutableLiveData()
        }
        if (searchPhrase == null || searchPhrase.isEmpty()){searchPhrase = "instagram"}
        deleteData()
        loadData(searchPhrase)
        return data
    }

    private fun loadData(str: String) {
        data = appRepository?.parseWebPageWithEndpoint(str)
    }

    private fun deleteData() {
        data = appRepository?.removeAllMedia()
    }
}