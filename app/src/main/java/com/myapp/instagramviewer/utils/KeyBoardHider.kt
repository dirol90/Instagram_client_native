/**
 * Created by Tsymbalyuk Konstantin from  on 15.11.2020.
 */
package com.myapp.instagramviewer.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class KeyBoardHider {

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}