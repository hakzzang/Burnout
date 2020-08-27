package com.hbs.burnout.ui.ext.view

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun View.hideBottomDrawer(){
    val bottomDrawerBehavior = BottomSheetBehavior.from(this)
    bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
}

fun FragmentManager.nullCheckAndDismiss(tag:String){
    if(this.findFragmentByTag(tag) != null){
        val prevFragment = this.findFragmentByTag(tag)
        val ft = this.beginTransaction()
        prevFragment?.let { ft.remove(it) }
        val fragment = this.findFragmentByTag(tag)
        (fragment as DialogFragment).dismissAllowingStateLoss()
    }
}