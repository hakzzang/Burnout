package com.hbs.burnout.utils

import android.graphics.Color
import android.os.Build
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform

object TransitionConfigure {
    const val START_DURATION = 350L
    const val END_DURATION = 350L
    const val ARC_TYPE = "ARC_TYPE"
    const val LINEAR_TYPE = "LINEAR_TYPE"
    const val TRANSITION_TYPE = "TRANSITION_TYPE"
}

object TransitionNavigation{
    const val CHATTING = "CHATTING"
}

interface BurnoutTransitionManager {
    fun setStartArcFadeInTransition(): MaterialContainerTransform?
    fun setReturnArcFadeOutTransition(): MaterialContainerTransform?
    fun setLinearTransition(isForward:Boolean): MaterialContainerTransform?
}

class BurnoutTransitionManagerImpl : BurnoutTransitionManager {
    override fun setStartArcFadeInTransition(): MaterialContainerTransform? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return MaterialContainerTransform().apply {
                scrimColor = Color.TRANSPARENT
                duration = TransitionConfigure.START_DURATION
                interpolator = FastOutSlowInInterpolator()
                pathMotion = MaterialArcMotion()
                fadeMode = MaterialContainerTransform.FADE_MODE_IN
            }
        }
        return null
    }

    override fun setReturnArcFadeOutTransition(): MaterialContainerTransform? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return MaterialContainerTransform().apply {
                scrimColor = Color.TRANSPARENT
                duration = TransitionConfigure.END_DURATION
                interpolator = FastOutSlowInInterpolator()
                pathMotion = MaterialArcMotion()
                fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            }
        }

        return null
    }

    override fun setLinearTransition(isForward: Boolean): MaterialContainerTransform? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return MaterialContainerTransform().apply {
                scrimColor = Color.TRANSPARENT
                duration = TransitionConfigure.END_DURATION
                fadeMode = if(isForward){
                    MaterialContainerTransform.FIT_MODE_AUTO
                }else{
                    MaterialContainerTransform.FIT_MODE_AUTO
                }
            }
        }
        return null
    }
}