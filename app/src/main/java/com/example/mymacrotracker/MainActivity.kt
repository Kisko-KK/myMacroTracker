package com.example.mymacrotracker

import MainScreen
import SearchScreen
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymacrotracker.view.FoodItemScreen
import com.example.mymacrotracker.view.GoalsScreen
import com.example.mymacrotracker.view.LoginRegisterScreen
import com.example.mymacrotracker.viewModel.FirebaseViewModel
import com.example.mymacrotracker.viewModel.NotificationViewModel
import com.example.mymacrotracker.viewModel.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "mainScreen") {
                composable("loginRegisterScreen") {
                    LoginRegisterScreen(navController = navController)
                }
                composable("mainScreen") {
                    MainScreen(navController = navController)
                }
                composable("homeScreen") {
                    SearchScreen(SearchViewModel() ,navController = navController)
                }
                composable("foodItemScreen") {
                    FoodItemScreen( navController = navController, SearchViewModel(), FirebaseViewModel(navController))
                }
                composable("goalsScreen") {
                    GoalsScreen(firebaseViewModel = FirebaseViewModel(navController))
                }

            }

        }
    }
}
