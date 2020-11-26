package com.behraz.fastermixer.batch.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlin.math.hypot

object MyAnimationUtil {
    private val ANIMATE_TRASNLATE_Y by lazy { "translationY" }
    private val ANIMATE_TRASNLATE_X by lazy { "translationX" }
    private val ANIMATE_TRASNLATE_Z by lazy { "translationZ" }
    private val ANIMATE_ROTATION by lazy { "rotation" }
    private val ANIMATE_ALPHA by lazy { "alpha" }
    fun animateEnterRight(view: View?, duration: Int) {
        val animatorSet = AnimatorSet()
        val animatorTranslateX =
            ObjectAnimator.ofFloat(
                view,
                ANIMATE_TRASNLATE_X,
                -150f,
                0f
            )
        animatorTranslateX.interpolator = AccelerateDecelerateInterpolator()
        animatorTranslateX.duration = duration.toLong()
        animatorSet.playTogether(animatorTranslateX)
        animatorSet.start()
    }

    fun animateExitDown(view: View?, duration: Int) {
        val animatorSet = AnimatorSet()
        val animatorTranslateY =
            ObjectAnimator.ofFloat(
                view,
                ANIMATE_TRASNLATE_Y,
                0f,
                150f
            )
        animatorTranslateY.interpolator = AccelerateDecelerateInterpolator()
        animatorTranslateY.duration = duration.toLong()
        animatorSet.playTogether(animatorTranslateY)
        animatorSet.start()
    }

    fun animateEnterDown(view: View?, duration: Int) {
        val animatorSet = AnimatorSet()
        val animatorTranslateX =
            ObjectAnimator.ofFloat(
                view,
                ANIMATE_TRASNLATE_Y,
                -150f,
                0f
            )
        animatorTranslateX.interpolator = AccelerateDecelerateInterpolator()
        animatorTranslateX.duration = duration.toLong()
        animatorSet.playTogether(animatorTranslateX)
        animatorSet.start()
    }

    fun animateEnterUp(view: View?, duration: Int) {
        val animatorSet = AnimatorSet()
        val animatorTranslateY =
            ObjectAnimator.ofFloat(
                view,
                ANIMATE_TRASNLATE_Y,
                150f,
                0f
            )
        animatorTranslateY.interpolator = AccelerateDecelerateInterpolator()
        animatorTranslateY.duration = duration.toLong()
        animatorSet.playTogether(animatorTranslateY)
        animatorSet.start()
    }

    fun animateEnterLeft(view: View?, duration: Int) {
        val animatorSet = AnimatorSet()
        val animatorTranslateX =
            ObjectAnimator.ofFloat(
                view,
                ANIMATE_TRASNLATE_X,
                150f,
                0f
            )
        animatorTranslateX.interpolator = AccelerateDecelerateInterpolator()
        animatorTranslateX.duration = duration.toLong()
        animatorSet.playTogether(animatorTranslateX)
        animatorSet.start()
    }
}


fun Fragment.crossfade(contentView: View?, vararg progressView: View?) {
    requireActivity().crossfade(contentView, *progressView)
}

fun Activity.crossfade(contentView: View?, vararg progressView: View?) {
    // Retrieve and cache the system's default "short" animation time.
    val duration = resources.getInteger(android.R.integer.config_longAnimTime)


    contentView?.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .setDuration(duration.toLong())
            .setListener(null)
    }
    // Animate the loading view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    progressView.toList().forEach {
        it?.apply {
            animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                        alpha = 1f
                    }
                })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun startReveal(view: View, onAnimationEnd: () -> Unit) {
    val cx = view.width / 2
    val cy = view.height / 2
    val finalRadius = hypot(cx.toFloat(), cy.toFloat())
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            onAnimationEnd()
        }
    })
    anim.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun closeReveal(view: View, onAnimationEnd: () -> Unit) {
    val cx = view.width / 2
    val cy = view.height / 2
    val initialRadius = hypot(cx.toFloat(), cy.toFloat())
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            onAnimationEnd()
        }
    })
    anim.start()
}


fun bounce(view: View, duration: Long = 1000) {
    val animY = ObjectAnimator.ofFloat(view, "translationY", -100f, 0f)
    animY.duration = duration //1sec
    animY.interpolator = BounceInterpolator()
    animY.repeatCount = 0
    animY.start()
}

/*fun Activity.startActivityWithTransition(intent: Intent) {
    startActivity(intent)
    //todo dar bazi az gushi ha ba mishe bad az barrasi ezafe shavad
   // overridePendingTransition(R.anim.activity_start_enter, R.anim.nothing)
}

fun Activity.finishWithAnimation() {
    finish()
    overridePendingTransition(R.anim.activity_finish_enter, R.anim.activity_finish_exit);
}*/

