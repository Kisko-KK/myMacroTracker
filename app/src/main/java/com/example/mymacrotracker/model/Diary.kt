package com.example.mymacrotracker.model

data class Diary(
    val foods: List<Food>
) {
    constructor() : this(emptyList())
}