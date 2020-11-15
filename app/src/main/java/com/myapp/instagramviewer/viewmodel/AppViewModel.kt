/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.instagramviewer.repository.AppRepository
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import kotlinx.coroutines.*


class AppViewModel : ViewModel() {

    companion object {
        private var viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    }

    private val appRepository = AppRepository().newInstance()
    private var selectedMedia: InstagramMediaInfoEntity? = null

    fun getData(str: String?): LiveData<List<InstagramMediaInfoEntity>>? {
        var searchPhrase = str
        if (searchPhrase == null || searchPhrase.isEmpty()) {
            searchPhrase = "instagram"
        }

        return loadData(searchPhrase)
    }

    fun setSelectedMedia(obj: InstagramMediaInfoEntity) {
        selectedMedia = obj
    }

    fun getSelectedMedia(): InstagramMediaInfoEntity? {
        return selectedMedia
    }

    private fun loadData(str: String): LiveData<List<InstagramMediaInfoEntity>>? {
        return appRepository?.parseWebPageWithEndpoint(str)
    }

    private fun deleteData(str: String): LiveData<List<InstagramMediaInfoEntity>>? {
        return appRepository?.removeAllMedia(str)
    }
}