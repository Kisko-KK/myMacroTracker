package com.example.mymacrotracker.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mymacrotracker.model.Diary
import com.example.mymacrotracker.model.Food
import com.example.mymacrotracker.model.FoodDiaryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FirebaseViewModel(private val navController: NavController) : ViewModel(){
    val foodDiaryItems = MutableLiveData<List<FoodDiaryItem>>()
    val foodItems = MutableLiveData<List<Food>>()
    val caloriesGoal = MutableLiveData<Double>()
    val stepsGoal = MutableLiveData<Double>()
    val currentStepsFirebase = MutableLiveData<Double>()

    fun signIn(email: String, password: String, context: Context) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate("mainScreen")
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }

    fun register(email: String, password: String, context: Context) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate("goalsScreen")
                    } else {
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserId(): String? {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        return currentUser?.uid;
    }

    fun addFood( food: Food) {

        val db = FirebaseFirestore.getInstance()
        val date = getCurrentDate()

        val diaryCollectionRef = getUserId()?.let { db.collection("users").document(it).collection("diary_$date") }


        diaryCollectionRef?.get()?.addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                val newDiary = hashMapOf(
                    "foods" to listOf(food)
                )
                diaryCollectionRef.document("diary_$date").set(newDiary)
                    .addOnSuccessListener { documentReference ->
                        println("dsdsd")
                    }
                    .addOnFailureListener { e ->
                        println("Greška prilikom dodavanja novog dnevnika: $e")
                    }
            } else {
                val diaryDocRef = snapshot.documents[0].reference
                diaryDocRef.update("foods", FieldValue.arrayUnion(food))
                    .addOnSuccessListener {
                        println("Hrana dodana u postojeći dnevnik")
                    }
                    .addOnFailureListener { e ->
                        println("Greška prilikom dodavanja hrane u postojeći dnevnik: $e")
                    }
            }
        }?.addOnFailureListener { e ->
            println("Greška prilikom provjere dnevnika: $e")
        }


    }

    fun getFoodDiaryItems(date : String) {
        val db = FirebaseFirestore.getInstance()

        val diaryCollectionRef =
            getUserId()?.let { db.collection("users").document(it).collection("diary_$date").document("diary_$date") }

        diaryCollectionRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val diary = documentSnapshot.toObject(Diary::class.java)
                val foodList = diary?.foods
                val foodDiaryList = mutableListOf<FoodDiaryItem>()
                foodList?.forEach { food ->
                    val subtitle = "${food.calories} cal"
                    val foodDiaryItem = FoodDiaryItem(food.title, subtitle)
                    foodDiaryList.add(foodDiaryItem)
                }
                foodDiaryItems.postValue(foodDiaryList)
                Log.d("Napravljena lista", foodDiaryList.toString());
            } else {
                println("Dnevnik ne postoji za datum: $date")
                foodDiaryItems.postValue(emptyList())
            }
        }?.addOnFailureListener { e ->
            println("Greška prilikom dohvaćanja dnevnika hrane: $e")
        }
    }

    fun getFoodItems(date : String) {
        val db = FirebaseFirestore.getInstance()

        val diaryCollectionRef =
            getUserId()?.let { db.collection("users").document(it).collection("diary_$date").document("diary_$date") }

        diaryCollectionRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val diary = documentSnapshot.toObject(Diary::class.java)
                val foodList = diary?.foods ?: emptyList()

                foodItems.postValue(foodList)
            } else {
                println("Dnevnik ne postoji za datum: $date")
                foodItems.postValue(emptyList())
            }
        }?.addOnFailureListener { e ->
            println("Greška prilikom dohvaćanja dnevnika hrane: $e")
        }
    }

    fun getCaloriesGoal() {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val caloriesGoals = documentSnapshot.getDouble("caloriesGoal")
                // Ažurirajte LiveData s dohvaćenom vrijednošću
                caloriesGoal.value = caloriesGoals ?: 0.0
            } else {
                Log.d("FirebaseViewModel", "User document does not exist")
            }
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error getting user document", e)
        }
    }

    fun getStepsGoal() {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val stepsGoals = documentSnapshot.getDouble("stepsGoal")

                stepsGoal.value = stepsGoals ?: 0.0
            } else {
                Log.d("FirebaseViewModel", "User document does not exist")
            }
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error getting user document", e)
        }
    }

    fun getCurrentSteps() {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val currentStepsTemp = documentSnapshot.getDouble("currentSteps")

                currentStepsFirebase.value = currentStepsTemp ?: 0.0

            } else {
                Log.d("FirebaseViewModel", "User document does not exist")
            }
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error getting user document", e)
        }
    }

    fun setCurrentSteps(steps: Double) {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.update("currentSteps", steps)?.addOnSuccessListener {
            Log.d("FirebaseViewModel", "Current steps updated successfully to: $steps")
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error updating current steps", e)
        }
    }

    fun setStepsGoal(steps: Double) {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.update("stepsGoal", steps)?.addOnSuccessListener {
            Log.d("FirebaseViewModel", "Current steps updated successfully to: $steps")
            navController.navigate("mainScreen")
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error updating steps goal", e)
        }
    }

    fun setCaloriesGoal(cal: Double) {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = getUserId()?.let { db.collection("users").document(it) }

        userDocumentRef?.set(mapOf("caloriesGoal" to cal))?.addOnSuccessListener {
            Log.d("FirebaseViewModel", "Calories goal updated successfully to: $cal")
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error updating calories goal", e)
        }
    }




    fun calculateTotalMacros(foodDiaryItems: State<List<Food>?>): Triple<Double, Double, Double> {
        var totalCarbs = 0.0
        var totalFat = 0.0
        var totalProtein = 0.0

        val foods = foodDiaryItems.value

        foods?.let { foodList ->
            for (food in foodList) {
                totalCarbs += food.carbs
                totalFat += food.fat
                totalProtein += food.protein
            }
        }


        return Triple(totalCarbs, totalFat, totalProtein)
    }

    fun calculateTotalCalories(foodDiaryItems: State<List<Food>?>): Double {
        var totalCalories = 0.0

        val foods = foodDiaryItems.value

        foods?.let { foodList ->
            for (food in foodList) {
                totalCalories += food.calories
            }
        }
        return totalCalories
    }


    fun deleteFoodDiaryItem(foodDiaryItem: FoodDiaryItem, date: String) {
        val db = FirebaseFirestore.getInstance()
        val diaryCollectionRef =
            getUserId()?.let { db.collection("users").document(it).collection("diary_$date").document("diary_$date") }

        diaryCollectionRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val diary = documentSnapshot.toObject(Diary::class.java)
                val foodList = diary?.foods
                val updatedFoodList = foodList?.filter { it.title != foodDiaryItem.title}

                updatedFoodList?.let { foodItems.postValue(it) }
                foodDiaryItems.postValue(updatedFoodList?.map { food ->
                    FoodDiaryItem(food.title, "${food.calories} cal")
                })

                diaryCollectionRef.update("foods", updatedFoodList)
                    .addOnSuccessListener {
                        Log.d("FirebaseViewModel", "Food diary item deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseViewModel", "Error deleting food diary item", e)
                    }
            } else {
                Log.d("FirebaseViewModel", "Food diary document does not exist for date: $date")
            }
        }?.addOnFailureListener { e ->
            Log.e("FirebaseViewModel", "Error getting food diary document", e)
        }
    }

    public fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }



}