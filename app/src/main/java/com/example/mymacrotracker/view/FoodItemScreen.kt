package com.example.mymacrotracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymacrotracker.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.mymacrotracker.model.Food
import com.example.mymacrotracker.ui.theme.BackgroundMain
import com.example.mymacrotracker.ui.theme.BackgroundSecondary
import com.example.mymacrotracker.viewModel.FirebaseViewModel
import com.example.mymacrotracker.viewModel.SearchViewModel


@Composable
fun FoodItemScreen(navController: NavController, searchViewModel: SearchViewModel, firebaseViewModel: FirebaseViewModel) {

    var numOfServings by remember { mutableDoubleStateOf(1.0) }
    val context = LocalContext.current

    val foodItem = searchViewModel.getFoodItem(navController)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Strelica za povratak
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )


                Text(
                    text = "ADD FOOD",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )


                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        if (foodItem != null) {
                            firebaseViewModel.addFood("ZVXZJfnhj3T8s6mntVSrdsQPupj2", Food(foodItem.ingredients[0].parsed[0].food , foodItem.totalNutrients.PROCNT.quantity * numOfServings, foodItem.totalNutrients.FAT.quantity *numOfServings, foodItem.totalNutrients.CHOCDF.quantity * numOfServings, foodItem.calories * numOfServings))
                        }
                        navController.popBackStack()
                    }
                )
            }

            // Naziv itema
            if (foodItem != null) {
                Text(
                    text = "${foodItem.ingredients[0].parsed[0].food}",
                    color = Color.White,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp // Promijenite veličinu prema vašim preferencijama
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            // Linije
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Gray)
            )

            // Number of servings
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "NUMBER OF SERVINGS",
                    modifier = Modifier.weight(1f),
                    color = Color.White, // Bijela boja teksta
                )

                // Input text
                // Input text
                OutlinedTextField(
                    value = numOfServings.toString(),
                    onValueChange = { text ->
                        if (text.isNotBlank() && text != ".") {
                            numOfServings = text.toDoubleOrNull() ?: numOfServings
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = {}),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.White),
                    modifier = Modifier
                        .width(100.dp)
                        .background(BackgroundSecondary)
                        .border(2.dp, BackgroundSecondary, shape = RoundedCornerShape(8.dp)), // Postavljanje zaobljenih rubova
                )

            }

            // Crta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SERVING SIZE",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                )
                if (foodItem != null) {
                    Text(
                        text = "${foodItem.ingredients[0].parsed[0].quantity} ${foodItem.ingredients[0].parsed[0].measure}",
                        color = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (foodItem != null){
                Macros(fat = foodItem.totalNutrients.FAT.quantity * numOfServings, protein = foodItem.totalNutrients.PROCNT.quantity * numOfServings, carbs = foodItem.totalNutrients.CHOCDF.quantity * numOfServings)
            }



            Spacer(modifier = Modifier.height(20.dp))

            // Scrollable box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(BackgroundSecondary, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                    if (foodItem != null) {
                        NutritionRow("ENERGY", "${foodItem.totalNutrients.ENERC_KCAL.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.ENERC_KCAL.unit}")
                        NutritionRow("SUGAR", "${foodItem.totalNutrients.SUGAR.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.SUGAR.unit}")
                        NutritionRow("FIBER", "${foodItem.totalNutrients.FIBTG.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.FIBTG.unit}")
                        NutritionRow("SODIUM", "${foodItem.totalNutrients.NA.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.NA.unit}")
                        NutritionRow("CHOLESTEROL", "${foodItem.totalNutrients.CHOLE.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.CHOLE.unit}")
                        NutritionRow("POTASSIUM", "${foodItem.totalNutrients.K.quantity.toInt() * numOfServings} ${foodItem.totalNutrients.K.unit}")
                    }

                }
            }

        }
    }
}