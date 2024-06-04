package com.example.mymacrotracker.service

import com.example.mymacrotracker.model.FoodItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.edamam.com/api/"
const val APP_ID = "ebd88aba"
const val APP_KEY = "6288a89f9915f12a42c2d7956aa084cf"

interface RetrofitService {
    @GET("nutrition-data")
    suspend fun searchFood(
        @Query("app_id") appId: String = APP_ID,
        @Query("app_key") appKey: String = APP_KEY,
        @Query("nutrition-type") nutritionType: String = "logging",
        @Query("ingr") ingr: String
    ): FoodItem

    companion object {
        fun create(): RetrofitService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }
}

