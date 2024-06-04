package com.example.mymacrotracker.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymacrotracker.ui.theme.BackgroundMain
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mymacrotracker.viewModel.FirebaseViewModel
import com.example.mymacrotracker.R
import com.example.mymacrotracker.viewModel.NotificationViewModel
import com.example.mymacrotracker.viewModel.StepCounterViewModel

@Composable
fun HomeScreen(firebaseViewModel: FirebaseViewModel, stepCounterViewModel: StepCounterViewModel, notificationViewModel: NotificationViewModel) {

    var totalCalories by remember { mutableDoubleStateOf(0.0) }

    var totalCarbs by remember { mutableDoubleStateOf(0.0) }
    var totalFat by remember { mutableDoubleStateOf(0.0) }
    var totalProtein by remember { mutableDoubleStateOf(0.0) }
    var currentSteps by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    val stepCountLiveData by stepCounterViewModel.stepCountLiveData.observeAsState()
    currentSteps = stepCountLiveData ?: 0

    val caloriesGoal by firebaseViewModel.caloriesGoal.observeAsState(0.0)
    val currentStepsFirebase by firebaseViewModel.currentStepsFirebase.observeAsState(1.0)
    val stepsGoal by firebaseViewModel.stepsGoal.observeAsState(0.0)
    val foodItems = firebaseViewModel.foodItems.observeAsState()


    LaunchedEffect(Unit) {
        firebaseViewModel.getFoodItems(firebaseViewModel.getCurrentDate())
        notificationViewModel.createNotificationChannel("0", context)
    }
    LaunchedEffect(currentSteps) {
        notificationViewModel.checkAndSendNotification(currentSteps, currentStepsFirebase, stepsGoal, context)
    }
    LaunchedEffect(foodItems.value) {
        val totalMacros = firebaseViewModel.calculateTotalMacros(foodItems)
        totalCalories = firebaseViewModel.calculateTotalCalories(foodItems)
        firebaseViewModel.getCaloriesGoal()

        totalCarbs = totalMacros.first
        totalFat = totalMacros.second
        totalProtein = totalMacros.third
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Naziv aplikacije
            Text(
                text = "MY MACRO TRACKER",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.merge(LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Natpis "Today"
            Text(
                text = "TODAY",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .background(color = Color(0xFF4341BF), shape = RoundedCornerShape(30.dp))
                    .border(
                        BorderStroke(1.dp, Color.Transparent),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(16.dp)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalCalories cal",
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                        color = Color.White
                    )
                    Text(
                        text = "OUT OF",
                        style = MaterialTheme.typography.subtitle2.copy(fontStyle = FontStyle.Italic),
                        color = Color.White
                    )
                    Text(
                        text = "$caloriesGoal cal",
                        style = MaterialTheme.typography.subtitle1.copy(fontStyle = FontStyle.Italic),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Macros(fat = totalFat, protein = totalProtein, carbs = totalCarbs)

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(color = BackgroundMain, shape = RoundedCornerShape(30.dp))
                    .border(
                        BorderStroke(3.dp, Color(0xFF5754F7)),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(25.dp),
                contentAlignment = Alignment.Center // Pomicanje sadržaja prema sredini
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically // Centriranje vertikalno
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "CURRENT: ${currentSteps + currentStepsFirebase}",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontStyle = FontStyle.Italic,
                                fontSize = 20.sp // Povećanje veličine teksta
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "GOAL: ${stepsGoal.toInt()}",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontStyle = FontStyle.Italic,
                                fontSize = 20.sp // Povećanje veličine teksta
                            ),
                            color = Color.White
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.iconwalk), // Povežite ikonu s vašim resursima
                                contentDescription = null, // Postavite opis sadržaja na null ako ikona nema poseban opis
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                text = "${"%.2f".format((currentSteps.toDouble() + currentStepsFirebase.toDouble()) / stepsGoal.toDouble() * 100)}%",

                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 18.sp // Povećanje veličine teksta
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }


        }

    }
    // Registracija senzora pri kreiranju HomeScreen-a
    DisposableEffect(Unit) {
        firebaseViewModel.getStepsGoal()
        stepCounterViewModel.registerSensorListener()
        firebaseViewModel.getCurrentSteps()

        onDispose {

            firebaseViewModel.setCurrentSteps(currentSteps+currentStepsFirebase)
            currentSteps = 0;
            stepCounterViewModel.unregisterSensorListener()
        }
    }

}

