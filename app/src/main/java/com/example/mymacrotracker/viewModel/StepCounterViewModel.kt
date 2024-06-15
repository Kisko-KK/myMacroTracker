package com.example.mymacrotracker.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import kotlin.math.sqrt


class StepCounterViewModel(private val context: Context) : ViewModel() {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val accelerometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private val _stepCountLiveData = MutableLiveData<Int>()
    val stepCountLiveData: LiveData<Int> = _stepCountLiveData

    private var stepCount = 0

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor == accelerometer) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = sqrt((x * x + y * y + z * z).toDouble())
                if (acceleration > ACCELERATION_THRESHOLD) {
                    stepCount++
                    _stepCountLiveData.postValue(stepCount)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    companion object {
        private const val ACCELERATION_THRESHOLD = 15
    }

    fun registerSensorListener() {
        accelerometer?.let { sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    fun unregisterSensorListener() {
        stepCount = 0
        _stepCountLiveData.postValue(stepCount)
        sensorManager.unregisterListener(sensorListener)

    }

    override fun onCleared() {
        super.onCleared()
        unregisterSensorListener()
    }
}


