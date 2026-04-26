package com.example.healthsync.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthsync.R
import com.example.healthsync.viewmodel.HealthViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private val viewModel: HealthViewModel by viewModels()

    private lateinit var tvCurrentDate: TextView
    private lateinit var tvCals: TextView
    private lateinit var tvPro: TextView
    private lateinit var tvCarbs: TextView
    private lateinit var tvFats: TextView
    private lateinit var tvWater: TextView
    private lateinit var tvWeight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tvCurrentDate = findViewById(R.id.tvCurrentDate)
        tvCals = findViewById(R.id.tvCals)
        tvPro = findViewById(R.id.tvPro)
        tvCarbs = findViewById(R.id.tvCarbs)
        tvFats = findViewById(R.id.tvFats)
        tvWater = findViewById(R.id.tvWater)
        tvWeight = findViewById(R.id.tvWeight)

        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etCals = findViewById<EditText>(R.id.etCalories)
        val etPro = findViewById<EditText>(R.id.etProtein)
        val etCarbs = findViewById<EditText>(R.id.etCarbs)
        val etFats = findViewById<EditText>(R.id.etFats)
        val etWater = findViewById<EditText>(R.id.etWater)

        // --- OBSERVERS ---
        viewModel.saveStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Logged Successfully!", Toast.LENGTH_SHORT).show()
                etWeight.text.clear(); etCals.text.clear(); etPro.text.clear()
                etCarbs.text.clear(); etFats.text.clear(); etWater.text.clear()
            }
        }

        viewModel.displayDateString.observe(this) { displayStr ->
            if (viewModel.isViewingToday()) {
                tvCurrentDate.text = "Today\n($displayStr)"
            } else {
                tvCurrentDate.text = displayStr
            }
        }

        viewModel.todayLog.observe(this) { updateUI() }
        viewModel.yesterdayLog.observe(this) { updateUI() }
        viewModel.userProfile.observe(this) { updateUI() }

        // --- BUTTON CLICKS ---
        findViewById<Button>(R.id.btnPrevDay).setOnClickListener { viewModel.changeDateOffset(-1) }
        findViewById<Button>(R.id.btnNextDay).setOnClickListener { viewModel.changeDateOffset(1) }

        findViewById<Button>(R.id.btnSaveLog).setOnClickListener {
            viewModel.addDailyLog(
                etWater.text.toString(),
                etCals.text.toString(),
                etPro.text.toString(),
                etCarbs.text.toString(),
                etFats.text.toString(),
                etWeight.text.toString()
            )
        }

        findViewById<Button>(R.id.btnDeleteLog).setOnClickListener { viewModel.deleteViewedLog() }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Init
        viewModel.fetchUserProfile()
    }

    private fun updateUI() {
        val log = viewModel.todayLog.value
        val yLog = viewModel.yesterdayLog.value
        val profile = viewModel.userProfile.value ?: return

        val cCals = log?.calories ?: 0
        val cPro = log?.protein ?: 0
        val cCarbs = log?.carbs ?: 0
        val cFats = log?.fats ?: 0
        val cWater = log?.waterIntakeMl ?: 0

        val tCals = profile.targetCalories
        val tPro = profile.targetProtein
        val tCarbs = profile.targetCarbs
        val tFats = profile.targetFats

        val pCals = if (tCals > 0) (cCals * 100) / tCals else 0
        val pPro = if (tPro > 0) (cPro * 100) / tPro else 0
        val pCarbs = if (tCarbs > 0) (cCarbs * 100) / tCarbs else 0
        val pFats = if (tFats > 0) (cFats * 100) / tFats else 0

        tvCals.text = "Calories $cCals / $tCals ......... $pCals%"
        tvPro.text = "Protein $cPro / $tPro ......... $pPro%"
        tvCarbs.text = "Carb $cCarbs / $tCarbs ......... $pCarbs%"
        tvFats.text = "Fat $cFats / $tFats ......... $pFats%"
        tvWater.text = "Water: $cWater ml"

        val currWeight = log?.weightKg ?: profile.weightKg
        val yesterdayWeight = yLog?.weightKg ?: profile.weightKg
        val diff = currWeight - yesterdayWeight

        val formattedDiff = String.format(Locale.getDefault(), "%.1f", diff)
        val trendStr = when {
            diff > 0 -> "(+$formattedDiff kg ⬆️)"
            diff < 0 -> "($formattedDiff kg ⬇️)"
            else -> "(-)"
        }
        tvWeight.text = "Weight: $currWeight kg $trendStr"
    }
}