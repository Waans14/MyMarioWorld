package com.waans.mario_world.core.utils

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

object Utils {

    const val imageURL = "https://raw.githubusercontent.com/waans14/waans14.github.io/main/hosting/resouces/mario_world/"

    fun View.fadeVisibility(visibility: Int, duration: Long = 400) {
        val transition: Transition = Fade()
        transition.duration = duration
        transition.addTarget(this)
        TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
        this.visibility = visibility
    }
}