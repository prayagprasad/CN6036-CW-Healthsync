package com.example.healthsync.model

data class UserProfile(
    val firstName: String = "",
    val surname: String = "",
    val dob: String = "",
    val gender: String = "",
    val heightCm: Int = 0,
    val weightKg: Double = 0.0,
    val maintenanceCalories: Int = 0,
    val targetCalories: Int = 0,
    val targetProtein: Int = 0,
    val targetFats: Int = 0,
    val targetCarbs: Int = 0
)