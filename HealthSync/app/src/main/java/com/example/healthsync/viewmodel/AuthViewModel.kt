package com.example.healthsync.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthsync.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

    // Get the Firebase Authentication instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to tell the View what happened
    val authResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    // New LiveData just for Step 1 of onboarding
    val stepOneComplete = MutableLiveData<Boolean>()

    // --- LOGIN (For Returning Users) ---
    fun login(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "Please fill in all fields"
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authResult.value = true
                } else {
                    errorMessage.value = task.exception?.message
                    authResult.value = false
                }
            }
    }

    //ONBOARDING STEP 1: Just create the secure login
    fun createBasicAccount(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "Email and Password required"
            return
        }
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                stepOneComplete.value = true
            } else {
                errorMessage.value = task.exception?.message
                stepOneComplete.value = false
            }
        }
    }

    // Temporary variables for Step 3
    var tempMaintenance = 0
    var tempTargetCals = 0
    var tempGoal = ""
    val macroReviewReady = MutableLiveData<Boolean>()

    // STEP 2: Do the Math and prepare Step 3
    fun calculateMacros(height: Int, weight: Double, age: Int, gender: String, goal: String) {
        var bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5
        if (gender == "Female") bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161

        tempMaintenance = (bmr * 1.375).toInt()
        tempGoal = goal
        tempTargetCals = tempMaintenance

        when (goal) {
            "Lose Weight" -> tempTargetCals -= 500
            "Gain Weight" -> tempTargetCals += 500
        }

        macroReviewReady.value = true // Tell UI to show Step 3
    }

    // STEP 3: Save the final edited profile
    fun saveFullProfile(
        fName: String,
        sName: String,
        dob: String,
        gender: String,
        height: Int,
        weight: Double,
        targetCals: Int,
        protein: Int,
        fats: Int,
        carbs: Int
    ) {
        val userId = auth.currentUser?.uid ?: return

        val profile = UserProfile(
            firstName = fName, surname = sName, dob = dob, gender = gender,
            heightCm = height, weightKg = weight,
            maintenanceCalories = tempMaintenance,
            targetCalories = targetCals, // <--- Now uses their custom edited calories!
            targetProtein = protein, targetFats = fats, targetCarbs = carbs
        )

        FirebaseDatabase.getInstance().reference.child("users").child(userId).child("profile")
            .setValue(profile).addOnCompleteListener { task ->
                if (task.isSuccessful) authResult.value = true
                else errorMessage.value = task.exception?.message
            }
    }
}