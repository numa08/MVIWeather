package net.numa08.mviweather.utils

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

@SuppressLint("CommitTransaction")
fun addFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        frameId: Int,
        backStackId: String? = null) {
    fragmentManager.beginTransaction().run {
        add(frameId, fragment)
        addToBackStack(backStackId)
        commit()
    }
}
