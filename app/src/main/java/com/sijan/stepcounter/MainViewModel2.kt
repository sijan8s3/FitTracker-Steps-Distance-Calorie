package com.sijan.stepcounter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijan.stepcounter.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class MainViewModel2 @Inject constructor(
    private val accelerometerSensor: MeasurableSensor
) : ViewModel() {
    private val _stepCount = MutableStateFlow(0)
    val stepCount: Flow<Int> = _stepCount

    private val _distanceTraveled = MutableStateFlow(0.0)
    val distanceTraveled: Flow<Double> = _distanceTraveled

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: Flow<Double> = _caloriesBurned

    private val _isTracking = MutableStateFlow(false)
    val isTracking: Flow<Boolean> = _isTracking


    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var lastStepTime = System.currentTimeMillis()
    private var totalSteps = 0

    init {
        startTracking()
    }

    fun startTracking() {
        accelerometerSensor.startListening()
        Log.d("MainViewModel", "Starting tracking")
        _isTracking.value = true
        viewModelScope.launch {
            accelerometerSensor.setOnSensorValuesChangedListener { values ->
                val x = values[0]
                val y = values[1]
                val z = values[2]
                val acceleration = sqrt(x * x + y * y + z * z)

                if (_isTracking.value && isStepDetected(acceleration)) {
                    println("step detected")
                    handleStepDetected()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    private fun isStepDetected(acceleration: Float): Boolean {
        // Implement step detection logic based on acceleration change
        return acceleration > 15 // Example threshold for step detection
    }

    private fun handleStepDetected() {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastStepTime
        if (timeDifference in 251..1999) {
            totalSteps++
            _stepCount.value = totalSteps
            updateDistance()
            updateCaloriesBurned()
            lastStepTime = currentTime
        }
    }

    private fun updateDistance() {
        val strideLength = calculateStrideLength() // Calculate stride length based on user's height
        _distanceTraveled.value = totalSteps * strideLength
    }

    private fun updateCaloriesBurned() {
        // Implement calorie calculation based on distance traveled and user's weight
        val weight = 70 // Example weight in kilograms
        val caloriesPerMeter = 0.04 // Example calories burned per meter walked
        _caloriesBurned.value = _distanceTraveled.value * caloriesPerMeter * weight
    }

    private fun calculateStrideLength(): Double {
        // Implement stride length calculation based on user's height
        // Example: Average stride length is approximately 0.415 * height (in centimeters)
        val heightInCentimeters = 170 // Example height in centimeters
        return 0.415 * heightInCentimeters / 100 // Convert height to meters
    }

    fun stopTracking() {
        Log.d("MainViewModel", "Starting tracking")
        accelerometerSensor.stopListening()
        _isTracking.value = false
    }



    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }
}
