package com.example.healthsync.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthsync.model.HealthLog
import com.example.healthsync.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HealthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    val saveStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    val todayLog = MutableLiveData<HealthLog?>()
    val yesterdayLog = MutableLiveData<HealthLog?>()
    val userProfile = MutableLiveData<UserProfile?>()

    // --- TIME MACHINE LOGIC ---
    val viewedDateString = MutableLiveData<String>() // For the Database
    val displayDateString = MutableLiveData<String>() // NEW: For the UI

    private var viewedCalendar = Calendar.getInstance()
    private val dbSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displaySdf = SimpleDateFormat("dd-MM-yy", Locale.getDefault()) // NEW: The UI Format

    init {
        updateDateAndFetch()
    }

    fun changeDateOffset(days: Int) {
        viewedCalendar.add(Calendar.DAY_OF_YEAR, days)
        updateDateAndFetch()
    }

    fun isViewingToday(): Boolean {
        return dbSdf.format(viewedCalendar.time) == dbSdf.format(Date())
    }

    private fun updateDateAndFetch() {
        // 1. Get the DB format and fetch data
        val dateStr = dbSdf.format(viewedCalendar.time)
        viewedDateString.value = dateStr

        // 2. Get the Display format and send it to the Activity
        displayDateString.value = displaySdf.format(viewedCalendar.time)

        fetchLogForDate(dateStr)

        val yesterdayCal = Calendar.getInstance()
        yesterdayCal.time = viewedCalendar.time
        yesterdayCal.add(Calendar.DAY_OF_YEAR, -1)
        fetchYesterdayLog(dbSdf.format(yesterdayCal.time))
    }

    fun fetchUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).child("profile")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userProfile.value = snapshot.getValue(UserProfile::class.java)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fetchLogForDate(dateStr: String) {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).child("logs").child(dateStr)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    todayLog.value = snapshot.getValue(HealthLog::class.java)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fetchYesterdayLog(yesterdayStr: String) {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).child("logs").child(yesterdayStr)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    yesterdayLog.value = snapshot.getValue(HealthLog::class.java)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // save to viewedDateString.value
    fun addDailyLog(
        waterStr: String,
        calsStr: String,
        proStr: String,
        carbStr: String,
        fatStr: String,
        weightStr: String
    ) {
        val userId = auth.currentUser?.uid ?: return
        val dateStr = viewedDateString.value ?: return

        val inWater = waterStr.toIntOrNull() ?: 0
        val inCals = calsStr.toIntOrNull() ?: 0
        val inPro = proStr.toIntOrNull() ?: 0
        val inCarbs = carbStr.toIntOrNull() ?: 0
        val inFats = fatStr.toIntOrNull() ?: 0

        val currWater = todayLog.value?.waterIntakeMl ?: 0
        val currCals = todayLog.value?.calories ?: 0
        val currPro = todayLog.value?.protein ?: 0
        val currCarbs = todayLog.value?.carbs ?: 0
        val currFats = todayLog.value?.fats ?: 0
        val currWeight = todayLog.value?.weightKg ?: userProfile.value?.weightKg ?: 0.0
        val finalWeight = weightStr.toDoubleOrNull() ?: currWeight

        val log = HealthLog(
            id = dateStr, date = dateStr,
            waterIntakeMl = currWater + inWater, calories = currCals + inCals,
            protein = currPro + inPro, carbs = currCarbs + inCarbs,
            fats = currFats + inFats, weightKg = finalWeight
        )

        db.child("users").child(userId).child("logs").child(dateStr)
            .setValue(log)
            .addOnCompleteListener { task -> if (task.isSuccessful) saveStatus.value = true }
    }

    fun deleteViewedLog() {
        val userId = auth.currentUser?.uid ?: return
        val dateStr = viewedDateString.value ?: return
        db.child("users").child(userId).child("logs").child(dateStr).removeValue()
    }
}