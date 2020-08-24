package com.hbs.burnout.ui.ext.view

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun View.hideBottomDrawer(){
    val bottomDrawerBehavior = BottomSheetBehavior.from(this)
    bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
}