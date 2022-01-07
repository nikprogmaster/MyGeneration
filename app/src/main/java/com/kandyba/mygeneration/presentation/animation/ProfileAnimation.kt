package com.kandyba.mygeneration.presentation.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView
import androidx.core.widget.NestedScrollView

class ProfileAnimation {

    private lateinit var editButton: ImageView
    private lateinit var avatar: ImageView
    private lateinit var list: NestedScrollView

    fun setStartAnimation(
        editButton: ImageView,
        avatar: ImageView,
        list: NestedScrollView,
        listener: AnimationListener
    ): MutableList<Animator> {
        this.editButton = editButton
        this.avatar = avatar
        this.list = list

        val animators = mutableListOf<Animator>()
        animators.addAll(setEditButtonAnimation())
        animators.addAll(setAvatarAnimation())
        animators.addAll(setFieldsAnimation(listener))
        return animators
    }


    private fun setEditButtonAnimation(): List<Animator> {
        val mAnimators = mutableListOf<Animator>()
        // alpha
        val alphaAnimator = ValueAnimator.ofFloat(1f, 0f)
        configureAlphaAnimator(alphaAnimator, editButton)
        alphaAnimator.startDelay = 300
        mAnimators.add(alphaAnimator)

        // scale
        val scaleAnimator = ValueAnimator.ofFloat(1f, 0f)
        configureScaleAnimator(scaleAnimator, editButton)
        scaleAnimator.startDelay = 300
        mAnimators.add(scaleAnimator)

        return mAnimators
    }


    private fun setAvatarAnimation(): List<Animator> {
        val mAnimators = mutableListOf<Animator>()

        // alpha
        val alphaAnimator = ValueAnimator.ofFloat(1f, 0f)
        configureAlphaAnimator(alphaAnimator, avatar)
        mAnimators.add(alphaAnimator)

        // scale
        val scaleAnimator = ValueAnimator.ofFloat(1f, 0f)
        configureScaleAnimator(scaleAnimator, avatar)
        mAnimators.add(scaleAnimator)

        return mAnimators
    }

    private fun setFieldsAnimation(
        listener: AnimationListener
    ): List<Animator> {
        val mAnimators = mutableListOf<Animator>()
        val height = (-avatar.height).toFloat()
        val transitionAnimator = ValueAnimator.ofFloat(list.y, height)
        configureTransAnimator(transitionAnimator, listener, list)

        mAnimators.add(transitionAnimator)
        return mAnimators
    }

    private fun configureTransAnimator(animator:ValueAnimator, listener: AnimationListener, list: NestedScrollView) {
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = 0
        animator.duration = 1000
        animator.addUpdateListener { anim: ValueAnimator ->
            list.y = anim.animatedValue as Float
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) { listener.onAnimationEnd() }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun configureAlphaAnimator(animator: ValueAnimator, view: ImageView) {
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = 0
        animator.duration = 300
        animator.addUpdateListener { anim: ValueAnimator ->
            view.alpha = anim.animatedValue as Float
        }
    }

    private fun configureScaleAnimator(animator: ValueAnimator, view: ImageView) {
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = 0
        animator.duration = 300
        animator.addUpdateListener { anim: ValueAnimator ->
            val scale = anim.animatedValue as Float
            view.scaleX = scale
            view.scaleY = scale
        }
    }

    fun setReverseAnimation(editButton: ImageView,
                            avatar: ImageView,
                            list: NestedScrollView, listener: AnimationListener): MutableList<Animator> {
        val animators = mutableListOf<Animator>()
        val height = list.y + avatar.height
        val transitionAnimator = ValueAnimator.ofFloat(list.y, height)
        configureTransAnimator(transitionAnimator, listener, list)

        val alphaEditAnimator = ValueAnimator.ofFloat(0f, 1f)
        configureAlphaAnimator(alphaEditAnimator, editButton)
        alphaEditAnimator.startDelay = 300

        // scale
        val scaleEditAnimator = ValueAnimator.ofFloat(0f, 1f)
        configureScaleAnimator(scaleEditAnimator, editButton)
        scaleEditAnimator.startDelay = 300

        // alpha
        val alphaAvatarAnimator = ValueAnimator.ofFloat(0f, 1f)
        configureAlphaAnimator(alphaEditAnimator, avatar)

        // scale
        val scaleAvatarAnimator = ValueAnimator.ofFloat(0f, 1f)
        configureScaleAnimator(scaleAvatarAnimator, avatar)

        animators.addAll(listOf(
            alphaAvatarAnimator,
            alphaEditAnimator,
            scaleAvatarAnimator,
            scaleEditAnimator,
            transitionAnimator
        ))

        return animators
    }

}

fun MutableList<Animator>.show() {
    for (animator in this) {
        animator.start()
    }
}