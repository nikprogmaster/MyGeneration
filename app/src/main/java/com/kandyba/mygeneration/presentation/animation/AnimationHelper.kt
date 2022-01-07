package com.kandyba.mygeneration.presentation.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView

class AnimationHelper {

    fun setAnimation(imageView: ImageView, animationListener: AnimationListener): List<Animator> {
        // alpha
        val alphaAnimator = ValueAnimator.ofFloat(1f, 0.6f)
        alphaAnimator.duration = 800
        alphaAnimator.repeatMode = ValueAnimator.REVERSE
        alphaAnimator.repeatCount = -1
        alphaAnimator.addUpdateListener { animator ->
            imageView.alpha = (animator.animatedValue as Float)
        }

        // scale
        val scaleAnimator = ValueAnimator.ofFloat(1f, 0.96f)
        scaleAnimator.duration = 800
        scaleAnimator.repeatMode = ValueAnimator.REVERSE
        scaleAnimator.repeatCount = -1
        scaleAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            imageView.scaleX = scale
            imageView.scaleY = scale
        }

        alphaAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                animationListener.onAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        return listOf(scaleAnimator, alphaAnimator)
    }

    fun showAnimation(animators: List<Animator>, isShown: Boolean) {
        for (animator in animators) {
            if (isShown) {
                animator.start()
            } else {
                animator.end()
            }
        }
    }

}