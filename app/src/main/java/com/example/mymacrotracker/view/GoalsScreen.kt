package com.example.mymacrotracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymacrotracker.R
import com.example.mymacrotracker.ui.theme.BackgroundMain
import com.example.mymacrotracker.viewModel.FirebaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GoalsScreen(firebaseViewModel: FirebaseViewModel) {
    val textFieldStyle = MaterialTheme.typography.body1.copy(
        fontStyle = FontStyle.Italic,
        color = Color.White
    )

    var stepsGoal by remember { mutableStateOf("") }
    var caloriesGoal by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain),
        contentAlignment = Alignment.Center,

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SET YOUR GOALS!",
                style = MaterialTheme.typography.h4.copy(
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "DAILY STEPS GOAL:",
                style = MaterialTheme.typography.body1.copy(
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.35f),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = stepsGoal,
                    onValueChange = { text -> stepsGoal = text },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body2.copy(color = Color.White),
                    label = { Text("e.g. 5000", color = Color.White) },
                    singleLine = true,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DAILY CALORIES GOAL:",
                style = MaterialTheme.typography.body1.copy(
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.35f),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = caloriesGoal,
                    onValueChange = { text -> caloriesGoal = text },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body2.copy(color = Color.White),
                    label = { Text("e.g. 2200", color = Color.White) },
                    singleLine = true,
                )
            }

            Image(
                painter = painterResource(id = R.drawable.goal),
                contentDescription = "Goal Image",
                modifier = Modifier.size(270.dp)
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val caloriesGoalValue = caloriesGoal.toDoubleOrNull()
                        val stepsGoalValue = stepsGoal.toDoubleOrNull()
                        if (caloriesGoalValue != null && stepsGoalValue != null) {
                            firebaseViewModel.setCaloriesGoal(caloriesGoalValue)
                            firebaseViewModel.setStepsGoal(stepsGoalValue)
                        } else {
                            // Ovdje možete postaviti poruku o pogrešci ako unos nije valjan
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 25.dp)
            ) {
                Text(
                    text = "I'M READY!",
                    style = textFieldStyle
                )
            }
        }
    }
}