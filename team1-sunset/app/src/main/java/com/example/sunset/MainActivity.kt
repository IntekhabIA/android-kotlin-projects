package com.example.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.example.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAnimation(0)
        startAnimationRef(0)
        binding.scene.setOnClickListener {
            startAnimation(1)
            startAnimationRef(1)
        }
    }

    private var isSunset = false
    private fun startAnimation(num: Int){
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()+15


        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())


        val raysAnimator = ObjectAnimator
            .ofFloat(binding.rays, "rotation", 0f, 720f)
            .setDuration(6000)
        raysAnimator.repeatCount = ObjectAnimator.INFINITE


        //sunrise
        val sunriseHeightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYEnd, sunYStart)
            .setDuration(3000)


        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor",nightSkyColor,sunsetSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val blueSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(4000)
        blueSkyAnimator.setEvaluator(ArgbEvaluator())

        val pulsatingSunAnimatorX = ObjectAnimator
            .ofFloat(binding.sun, "scaleX", 1.0f, 1.1f,1.0f)
            .setDuration(1000)
        pulsatingSunAnimatorX.repeatCount = ObjectAnimator.INFINITE
        pulsatingSunAnimatorX.repeatMode = ObjectAnimator.REVERSE

        val pulsatingSunAnimatorY = ObjectAnimator
            .ofFloat(binding.sun, "scaleY", 1.0f, 1.1f,1.0f)
            .setDuration(1000)
        pulsatingSunAnimatorY.repeatCount = ObjectAnimator.INFINITE
        pulsatingSunAnimatorY.repeatMode = ObjectAnimator.REVERSE


        val animatorSet = AnimatorSet()

        if(!isSunset && num == 1){
            animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)

                .before(nightSkyAnimator)
            isSunset = !isSunset
        } else if (num == 1) {
            animatorSet.play(sunriseHeightAnimator)
                .with(pulsatingSunAnimatorX)
                .with(pulsatingSunAnimatorY)
                .with(sunriseSkyAnimator)
                .with(blueSkyAnimator)
                .with(raysAnimator)
            isSunset = !isSunset
        } else {
            animatorSet.play(raysAnimator)
                .with(pulsatingSunAnimatorX)
                .with(pulsatingSunAnimatorY)
        }
        animatorSet.start()

    }

    private var isSunsetRef = false

private fun startAnimationRef(num: Int){
    val sunYStart = binding.sunRef.top.toFloat()
    val sunYEnd = -binding.sky.height.toFloat()-15

    val heightAnimator = ObjectAnimator
        .ofFloat(binding.sunRef, "y",sunYStart , sunYEnd)
        .setDuration(3000)
    heightAnimator.interpolator = AccelerateInterpolator()

    val raysAnimator = ObjectAnimator
        .ofFloat(binding.raysRef, "rotation",720f , 0f)
        .setDuration(6000)
    raysAnimator.repeatCount = ObjectAnimator.INFINITE

    //sunrise
    val sunriseHeightAnimator = ObjectAnimator
        .ofFloat(binding.sunRef, "y",sunYEnd ,sunYStart )
        .setDuration(3000)


    val pulsatingSunAnimatorX = ObjectAnimator
        .ofFloat(binding.sunRef, "scaleX", 1.0f, 1.1f,1.0f)
        .setDuration(1000)
    pulsatingSunAnimatorX.repeatCount = ObjectAnimator.INFINITE
    pulsatingSunAnimatorX.repeatMode = ObjectAnimator.REVERSE

    val pulsatingSunAnimatorY = ObjectAnimator
        .ofFloat(binding.sunRef, "scaleY", 1.0f, 1.1f,1.0f)
        .setDuration(1000)
    pulsatingSunAnimatorY.repeatCount = ObjectAnimator.INFINITE
    pulsatingSunAnimatorY.repeatMode = ObjectAnimator.REVERSE

    val animatorSet = AnimatorSet()

    if(!isSunsetRef && num == 1){
        animatorSet.play(heightAnimator)

        isSunsetRef = !isSunsetRef
    } else if (num == 1) {
        animatorSet.play(sunriseHeightAnimator)
            .with(pulsatingSunAnimatorX)
            .with(pulsatingSunAnimatorY)
            .with(raysAnimator)
        isSunsetRef = !isSunsetRef
    } else {
        animatorSet.play(raysAnimator)
            .with(pulsatingSunAnimatorX)
            .with(pulsatingSunAnimatorY)
    }
    animatorSet.start()
}
}