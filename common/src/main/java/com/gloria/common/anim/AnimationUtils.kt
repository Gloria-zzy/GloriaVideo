package com.gloria.common.anim

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


/**
 * 动画工具
 */
object AnimationUtils {
    /**
     * 渐隐渐现动画
     * @param view 需要实现动画的对象
     * @param state 需要实现的状态
     * @param duration 动画实现的时长（ms）
     */
    fun showAndHiddenAnimation(view: View, state: AnimationState, duration: Long) {
        var start = 0f
        var end = 0f
        if (state == AnimationState.STATE_SHOW) {
            end = 1f
            view.visibility = View.VISIBLE
        } else if (state == AnimationState.STATE_HIDDEN) {
            start = 1f
            view.visibility = View.INVISIBLE
        }
        val animation = AlphaAnimation(start, end)
        animation.duration = duration
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.clearAnimation()
            }
        })
        view.animation = animation
        animation.start()
    }

    enum class AnimationState {
        STATE_SHOW, STATE_HIDDEN
    }
}