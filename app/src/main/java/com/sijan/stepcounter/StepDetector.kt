package com.sijan.stepcounter

class StepDetector {
    private var currentSample = 0
    private var stepCount = 0
    private var isActiveCounter = true

    companion object {
        private const val MAX_STEPS_COUNT = 10000
        private const val INACTIVE_SAMPLE = 40 // Adjust this value as needed
    }

    fun detect(accelerometerValue: Double, currentThreshold: Double): Boolean {
        if (currentSample == INACTIVE_SAMPLE) {
            currentSample = 0
            if (!isActiveCounter) {
                isActiveCounter = true
            }
        }
        if (isActiveCounter && (accelerometerValue > currentThreshold)) {
            currentSample = 0
            isActiveCounter = false
            stepCount++
            if (stepCount == MAX_STEPS_COUNT) {
                stepCount = 0
            }
            return true
        }

        currentSample++
        return false
    }

    fun getStepCount(): Int {
        return stepCount
    }
}
