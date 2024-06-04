package com.example.mymacrotracker.model

import com.example.mymacrotracker.R

data class NavScreen(val title: String, val icon: Int) {
    companion object {
        val Home = NavScreen("Home", R.drawable.home)
        val Diary = NavScreen("Diary", R.drawable.diary)
        val Search = NavScreen("Search", R.drawable.outline_search_24)
    }
}

