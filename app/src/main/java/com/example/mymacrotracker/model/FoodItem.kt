package com.example.mymacrotracker.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class FoodItem(
    val uri: String,
    val calories: Double,
    @SerialName("totalCO2Emissions") val totalCo2Emissions: Double,
    @SerialName("co2EmissionsClass") val co2EmissionsClass: String,
    @SerialName("totalWeight") val totalWeight: Double,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val cautions: List<String>,
    @SerialName("totalNutrients") val totalNutrients: TotalNutrients,
    val totalDaily: TotalDaily,
    val ingredients: List<Ingredient>
) : java.io.Serializable

@kotlinx.serialization.Serializable
data class TotalNutrients(
    val ENERC_KCAL: Nutrient,
    val FAT: Nutrient,
    val FASAT: Nutrient,
    val FATRN: Nutrient,
    val FAMS: Nutrient,
    val FAPU: Nutrient,
    val CHOCDF: Nutrient,
    val CHOCDF_NET: Nutrient,
    val FIBTG: Nutrient,
    val SUGAR: Nutrient,
    val PROCNT: Nutrient,
    val CHOLE: Nutrient,
    val NA: Nutrient,
    val CA: Nutrient,
    val MG: Nutrient,
    val K: Nutrient,
    val FE: Nutrient,
    val ZN: Nutrient,
    val P: Nutrient,
    val VITA_RAE: Nutrient,
    val VITC: Nutrient,
    val THIA: Nutrient,
    val RIBF: Nutrient,
    val NIA: Nutrient,
    val VITB6A: Nutrient,
    val FOLDFE: Nutrient,
    val FOLFD: Nutrient,
    val FOLAC: Nutrient,
    val VITB12: Nutrient,
    val VITD: Nutrient,
    val TOCPHA: Nutrient,
    val VITK1: Nutrient,
    val WATER: Nutrient
) : java.io.Serializable

@kotlinx.serialization.Serializable
data class TotalDaily(
    val ENERC_KCAL: Nutrient,
    val FAT: Nutrient,
    val FASAT: Nutrient,
    val CHOCDF: Nutrient,
    val FIBTG: Nutrient,
    val PROCNT: Nutrient,
    val CHOLE: Nutrient,
    val NA: Nutrient,
    val CA: Nutrient,
    val MG: Nutrient,
    val K: Nutrient,
    val FE: Nutrient,
    val ZN: Nutrient,
    val P: Nutrient,
    val VITA_RAE: Nutrient,
    val VITC: Nutrient,
    val THIA: Nutrient,
    val RIBF: Nutrient,
    val NIA: Nutrient,
    val VITB6A: Nutrient,
    val FOLDFE: Nutrient,
    val VITB12: Nutrient,
    val VITD: Nutrient,
    val TOCPHA: Nutrient,
    val VITK1: Nutrient
): java.io.Serializable

@kotlinx.serialization.Serializable
data class Nutrient(
    val label: String,
    val quantity: Double,
    val unit: String
): java.io.Serializable

@kotlinx.serialization.Serializable
data class Ingredient(
    val text: String,
    val parsed: List<Parsed>
): java.io.Serializable

@kotlinx.serialization.Serializable
data class Parsed(
    val quantity: Double,
    val measure: String,
    val foodMatch: String,
    val food: String,
    val foodId: String,
    val weight: Double,
    val retainedWeight: Double,
    val nutrients: Nutrient,
    val measureURI: String,
    val status: String
): java.io.Serializable


@kotlinx.serialization.Serializable
data class Food(
    val title: String = "",
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val calories: Double = 0.0
) {
    constructor() : this("", 0.0, 0.0, 0.0, 0.0)
}


data class FoodDiaryItem(
    val title: String,
    val subtitle : String
) {
    constructor() : this("","")
}



