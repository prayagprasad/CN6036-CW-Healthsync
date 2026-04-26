package com.example.healthsync.model

data class HealthLog(
    val id: String = "",
    val date: String = "",
    val waterIntakeMl: Int = 0,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
    val weightKg: Double = 0.0
)