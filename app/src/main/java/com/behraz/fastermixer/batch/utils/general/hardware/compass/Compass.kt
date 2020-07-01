package com.behraz.fastermixer.batch.utils.general.hardware.compass

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation

class Compass(val interactions: Interactions, private var azimuthFix: Float = 0f) :
    SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private var azimuth = 0f


    fun register(context: Context) {
        mSensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

        val gravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (gravitySensor == null) {
            println("debug: device has no gravity sensor")
            return
        }
        if (accelerometerSensor == null) {
            println("debug: device has no accelerometer sensor")
            return
        }

        mSensorManager.registerListener(
            this,
            gravitySensor,
            SensorManager.SENSOR_DELAY_GAME
        )
        mSensorManager.registerListener(
            this,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun unregister() {
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        println("debug: onAccuracyChanged -> ${sensor.name}:$accuracy")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val alpha = 0.97f
        synchronized(this) {
            if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * sensorEvent.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * sensorEvent.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * sensorEvent.values[2]
            }

            if (sensorEvent.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * sensorEvent.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * sensorEvent.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * sensorEvent.values[2]
            }

            val r = FloatArray(9)
            val i = FloatArray(9)

            val success = SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)

                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + azimuthFix + 360) % 360

                val anim = RotateAnimation(
                    -azimuthFix,
                    -azimuth,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                ).apply {
                    duration = 500
                    repeatCount = 0
                    fillAfter = true
                }
                azimuthFix = azimuth

                interactions.onNewAzimuth(azimuth, anim)

            }

        }
    }


    interface Interactions {
        fun onNewAzimuth(azimuth: Float, animation: RotateAnimation)
    }


}

























