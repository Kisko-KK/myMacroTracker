package com.example.mymacrotracker.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.mymacrotracker.model.FoodItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mymacrotracker.service.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val apiService = RetrofitService.create()

    private val _foodList = MutableLiveData<List<FoodItem>>(emptyList())
    val foodList: LiveData<List<FoodItem>> = _foodList

    fun searchFood(foodName: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val ingrGrams = "100 grams $foodName"
            val ingrCups = "1 cup $foodName"
            val ingrTablespoon = "1 tablespoon $foodName"

            try {
                val results1 = async { apiService.searchFood(ingr = foodName) }
                val results2 = async { apiService.searchFood(ingr = ingrGrams) }
                val results3 = async { apiService.searchFood(ingr = ingrCups) }
                val results4 = async { apiService.searchFood(ingr = ingrTablespoon) }

                val resultList = listOf(results1.await(), results2.await(), results3.await(), results4.await())

                val invalidFood = resultList.filter { it.calories == 0.0 && it.totalWeight == 0.0 }
                if (invalidFood.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Couldn't find valid food items", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    _foodList.postValue(resultList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e(TAG, "Failed to fetch food items", e)
            }
        }
    }


    fun getFoodTitleSearch(foodItem: FoodItem): String {
        val quantity = foodItem.ingredients[0].parsed[0].quantity.toString()
        val measure = foodItem.ingredients[0].parsed[0].measure
        val calories = foodItem.calories.toString()

        return "$calories cal, $quantity $measure"
    }

    fun getTitle(foodItem: FoodItem): String {
        val measure = foodItem.ingredients[0].parsed[0].measure
        val name = foodItem.ingredients[0].parsed[0].food
        return "$name ($measure)";
    }

    fun getFoodItem(navController: NavController): FoodItem? {
        return navController.previousBackStackEntry?.savedStateHandle?.get<FoodItem>("foodItem")
    }
}

