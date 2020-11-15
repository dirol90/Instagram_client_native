/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.transitions

import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet

class ImageTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform()).setDuration(1000)
        }
    }
}