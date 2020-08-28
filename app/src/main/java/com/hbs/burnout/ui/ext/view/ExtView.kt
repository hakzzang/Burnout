package com.hbs.burnout.ui.ext.view

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable

fun View.hideBottomDrawer(){
    val bottomDrawerBehavior = BottomSheetBehavior.from(this)
    bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
}

fun BottomAppBar.drawEdgeShapeAppearance(){
    val topEdge = BottomAppBarCutCornersTopEdge(
        fabCradleMargin,
        fabCradleRoundedCornerRadius,
        cradleVerticalOffset
    )
    val background = background as MaterialShapeDrawable
    background.shapeAppearanceModel = background.shapeAppearanceModel.toBuilder().setTopEdge(topEdge).build()
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