package id.asistenrakyat.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import id.asistenrakyat.R

private var View.isAnimating: Boolean
    get() = getTag(R.id.tag_is_animating) as? Boolean ?: false
    set(value) {
        setTag(R.id.tag_is_animating, value)
    }


fun View.animateClick(action: () -> Unit) {
    if (isAnimating) return

    isAnimating = true
    this.animate()
        .scaleX(0.9f)
        .scaleY(0.9f)
        .setDuration(80)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@animateClick.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(80)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            isAnimating = false
                            action()
                        }
                    })
            }
        })
}
