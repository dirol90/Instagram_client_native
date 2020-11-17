/**
 * Created by Tsymbalyuk Konstantin from  on 13.11.2020.
 */
package com.myapp.instagramviewer.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.instagramviewer.MyApp
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.interfaces.AuthenticationListener
import com.myapp.instagramviewer.repository.AppRepository
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity
import com.myapp.instagramviewer.utils.AppPreferences
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.URL

class AppViewModel : ViewModel(), AuthenticationListener {

    companion object {
        private var viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    }

    private val appRepository = AppRepository().newInstance()
    private var selectedMedia: InstagramMediaInfoEntity? = null
    private var token: String? = null
    private var appPreferences: AppPreferences? = AppPreferences(MyApp.context)

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

    fun detectUrl (url: String, redirectUrl : String, userInfo: String)  {
        val listener: AuthenticationListener = this
        viewModelScope.launch{
            token = appPreferences?.getString(AppPreferences.TOKEN)
//            if (token != null) {
//                getUserInfoByAccessToken(token!!, userInfo)
//            }
            if (url.startsWith(redirectUrl)) {
                if (url.contains("code")) {
//                                https://l.instagram.com/?u=https://instagram.com/?code=AQCTArd5aHh6gUWUdziCCOBtPpS3Bk6PLqYNtfYJQMNLG-FuMrtDqKWBqwp4m8ibNcEYoFxN3oDKagQ-BLiZAacgYUsfaIqwKPcT5vyON9JaZbJZIrEm1UwVyUQmYMp7HvnLBlgDcJBhiIO6tr3IcCoAuu4itAwEftDzvqzM0SrCohYLiyW8zxXdT_IEvZSj2KY1tsVEWS1eGUkWckpBP1QV8LF6PJxwvh9ygpzpVcBMNQ#_&e=ATObyalkV5-FoDZLB_cm2-9HMc9YUJoBQp-MwvkQsPtqvLE79wItPl_bYe7a8PFz3os9svdqe78dyWTC9KupSg&s=1
                    val uri = Uri.decode(url)
                    var accessToken = ""
                    val firstIndex = uri.lastIndexOf("code=") + 5
                    val lastIndex = uri.indexOf("&")
                    accessToken = uri!!.substring(firstIndex, lastIndex)

                    listener.onTokenReceived(accessToken, userInfo)
                } else if (url.contains("?error")) {
                    Log.e("CODE", "getting error fetching access token")
                }
            }
        }
    }

    // TODO finish
    private suspend fun getUserInfoByAccessToken(token: String, userInfo : String) {
        withContext(Dispatchers.IO) {
            // TODO remove from try/catch
            try {
                val jsonStr = URL(userInfo + token).readText()
                try {

                    if (jsonStr != null) {
                        try {
                            val jsonObject = JSONObject(jsonStr.toString())
                            Log.e("response", jsonObject.toString())
                            val jsonData = jsonObject.getJSONObject("data")
                            if (jsonData.has("id")) {

                                appPreferences?.putString(
                                    AppPreferences.USER_ID,
                                    jsonData.getString("id")
                                )
                                appPreferences?.putString(
                                    AppPreferences.USER_NAME,
                                    jsonData.getString("username")
                                )
                                appPreferences?.putString(
                                    AppPreferences.PROFILE_PIC,
                                    jsonData.getString("profile_picture")
                                )
                                //TODO login
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        // TODO show error
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e  : Exception){
                e.printStackTrace()
            }
        }
    }

    // TODO finish
    override suspend fun onTokenReceived(auth_token: String?, userInfo: String) {
        if (auth_token == null) return
        appPreferences?.putString(AppPreferences.TOKEN, auth_token)
        token = auth_token
        getUserInfoByAccessToken(token!!, userInfo)
    }
}