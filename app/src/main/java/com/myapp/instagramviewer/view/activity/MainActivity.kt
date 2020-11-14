package com.myapp.instagramviewer.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.myapp.instagramviewer.R
import com.myapp.instagramviewer.view.fragment.FragmentMediaGrid
import com.myapp.instagramviewer.viewmodel.AppViewModel


class MainActivity : AppCompatActivity() {
    lateinit var videModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        videModel = ViewModelProvider(this).get(AppViewModel::class.java)
        addFragmentToActivity(supportFragmentManager, FragmentMediaGrid(), R.id.fl_placeholder)

    }

    private fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.addToBackStack(FragmentManager::class.simpleName)
        transaction.commit()
    }

}