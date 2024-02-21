package com.sijan.stepcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sijan.stepcounter.ui.theme.StepCounterTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        //splashScreen.setKeepOnScreenCondition { true }
        //startSomeNextActivity()
        //finish()
        setContent {
            StepCounterTheme {
                StepCounterApplication()
            }
        }
    }
}

@Composable
fun StepCounterApplication() {
    val viewModel = viewModel<MainViewModel>()
    val stepCount by viewModel.stepCount.collectAsState(initial = 0)
    val distanceTraveled by viewModel.distanceTraveled.collectAsState(initial = 0.0)
    val caloriesBurned by viewModel.caloriesBurned.collectAsState(initial = 0.0)
    val isTracking by viewModel.isTracking.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Step Count: $stepCount", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Distance Traveled: ${String.format("%.2f", distanceTraveled)} meters", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Calories Burned: ${String.format("%.2f", caloriesBurned)} kcal", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!isTracking) {
                Button(onClick = { viewModel.startTracking() }) {
                    Text("Start", modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                }
            } else {
                Button(onClick = { viewModel.stopTracking() }) {
                    Text("Stop", modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                }
            }
        }
    }
}

