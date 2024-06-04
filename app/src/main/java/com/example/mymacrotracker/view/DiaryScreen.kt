package com.example.mymacrotracker.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymacrotracker.ui.theme.BackgroundMain
import com.example.mymacrotracker.ui.theme.BackgroundSecondary
import com.example.mymacrotracker.viewModel.DiaryViewModel
import com.example.mymacrotracker.viewModel.FirebaseViewModel

@Composable
fun DiaryScreen(diaryViewModel: DiaryViewModel, firebaseViewModel: FirebaseViewModel) {

    val currentDate by diaryViewModel.currentDate.observeAsState("")

    var totalCarbs by remember { mutableDoubleStateOf(0.0) }
    var totalFat by remember { mutableDoubleStateOf(0.0) }
    var totalProtein by remember { mutableDoubleStateOf(0.0) }
    var totalCalories by remember { mutableDoubleStateOf(0.0) }

    val foodDiaryItems = firebaseViewModel.foodDiaryItems.observeAsState()
    val foodItems = firebaseViewModel.foodItems.observeAsState()
    val caloriesGoal by firebaseViewModel.caloriesGoal.observeAsState(0.0)

    LaunchedEffect(currentDate) {
        firebaseViewModel.getFoodDiaryItems(diaryViewModel.getCurrentDate())
        firebaseViewModel.getFoodItems(diaryViewModel.getCurrentDate())
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { diaryViewModel.moveBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White
                )
                IconButton(
                    onClick = { diaryViewModel.moveForward() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calories section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = BackgroundSecondary, shape = RoundedCornerShape(13.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    Text("CALORIES INTAKE", color = Color.White, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly

                    ) {
                        Column {
                            Text(caloriesGoal.toInt().toString(), style = MaterialTheme.typography.body1, color = Color.White)
                        }
                        Column {
                            Text("-", style = MaterialTheme.typography.body1, color = Color.White)
                        }
                        Column {
                            Text(totalCalories.toInt().toString(), style = MaterialTheme.typography.body1, color = Color.White)
                        }
                        Column {
                            Text("=", style = MaterialTheme.typography.body1, color = Color.White)
                        }
                        Column {
                            Text((caloriesGoal - totalCalories).toInt().toString(), style = MaterialTheme.typography.body1, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column {
                            Text("GOAL", style = MaterialTheme.typography.body2, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text("FOOD", style = MaterialTheme.typography.body2, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("REMAINING", style = MaterialTheme.typography.body2, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Macros(fat = totalFat, protein = totalProtein, carbs = totalCarbs)
            Spacer(modifier = Modifier.height(16.dp))

            // FOOD DIARY section
            Text(
                "FOOD DIARY",
                style = MaterialTheme.typography.h6,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (foodDiaryItems.value.isNullOrEmpty()) {
                Text(
                    text = "No items in my diary for this day yet.",
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = BackgroundSecondary, shape = RoundedCornerShape(13.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    foodDiaryItems.value?.let { items ->
                        LazyColumn {
                            items(items) { foodDiaryItem ->
                                FoodDiaryItem(
                                    title = foodDiaryItem.title,
                                    subtitle = foodDiaryItem.subtitle,
                                    onDeleteClick = {
                                        firebaseViewModel.deleteFoodDiaryItem(foodDiaryItem, diaryViewModel.getCurrentDate())

                                    }
                                )
                            }
                        }
                    }
                }
        }

    }

}}

