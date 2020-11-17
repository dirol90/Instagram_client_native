package com.myapp.instagramviewer.interfaces

interface AuthenticationListener {
    suspend fun onTokenReceived(auth_token: String?, usetInfo: String)
}