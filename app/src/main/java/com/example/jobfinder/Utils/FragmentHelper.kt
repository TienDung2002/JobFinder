package com.example.jobfinder.Utils

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.example.jobfinder.R

object FragmentHelper {
    fun replaceFragment(fragmentManager: FragmentManager, frame: FrameLayout, fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(frame.id, fragment)
        fragmentTransaction.commit()
    }

    fun replaceFragmentWithBackStack(fragmentManager: FragmentManager, frame: FrameLayout, fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(frame.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
