package com.example.healthsync.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthsync.R
import com.example.healthsync.viewmodel.AuthViewModel

class SetupActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val layoutStep1 = findViewById<LinearLayout>(R.id.layoutStep1)
        val layoutStep2 = findViewById<LinearLayout>(R.id.layoutStep2)
        val layoutStep3 = findViewById<LinearLayout>(R.id.layoutStep3)

        // Step 1 & 2 UI
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etSurname = findViewById<EditText>(R.id.etSurname)
        val etDob = findViewById<EditText>(R.id.etDob)
        val rbMale = findViewById<RadioButton>(R.id.rbMale)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etWeight = findViewById<EditText>(R.id.etSetupWeight)
        val rbLose = findViewById<RadioButton>(R.id.rbLose)
        val rbGain = findViewById<RadioButton>(R.id.rbGain)

        // Step 3 UI
        val tvExplanation = findViewById<TextView>(R.id.tvMacroExplanation)
        val etTargetCals = findViewById<EditText>(R.id.etTargetCals) // <--- New!
        val etProtein = findViewById<EditText>(R.id.etProtein)
        val etFats = findViewById<EditText>(R.id.etFats)
        val etCarbs = findViewById<EditText>(R.id.etCarbs)

        // --- OBSERVERS ---
        viewModel.stepOneComplete.observe(this) { success ->
            if (success) {
                layoutStep1.visibility = View.GONE
                layoutStep2.visibility = View.VISIBLE
            }
        }

        viewModel.macroReviewReady.observe(this) { ready ->
            if (ready) {
                layoutStep2.visibility = View.GONE
                layoutStep3.visibility = View.VISIBLE

                val target = viewModel.tempTargetCals
                val maint = viewModel.tempMaintenance

                // Generate Custom Explanation Text
                val explainText = when (viewModel.tempGoal) {
                    "Lose Weight" -> "These macronutrient values reflect your cutting calories of $target kcal per day, which is a 500 kcal deficit from your maintenance of $maint kcal."
                    "Gain Weight" -> "These macronutrient values reflect your bulking calories of $target kcal per day, which is +500 kcal from your maintenance of $maint kcal."
                    else -> "These macronutrient values reflect your maintenance calories of $target kcal per day."
                }
                tvExplanation.text = explainText

                val proGrams = ((target * 0.30) / 4).toInt()
                val fatGrams = ((target * 0.35) / 9).toInt()
                val carbGrams = ((target * 0.35) / 4).toInt()

                // Auto-fill editable inputs
                etTargetCals.setText(target.toString()) // <--- Auto-fill calories
                etProtein.setText(proGrams.toString())
                etFats.setText(fatGrams.toString())
                etCarbs.setText(carbGrams.toString())
            }
        }

        viewModel.authResult.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }

        // --- BUTTON CLICKS ---
        findViewById<Button>(R.id.btnCreateAccountOnly).setOnClickListener {
            val email = findViewById<EditText>(R.id.etSetupEmail).text.toString()
            val pass = findViewById<EditText>(R.id.etSetupPass).text.toString()
            viewModel.createBasicAccount(email, pass)
        }

        findViewById<Button>(R.id.btnCalculateMacros).setOnClickListener {
            val gender = if (rbMale.isChecked) "Male" else "Female"
            var goal = "Maintain Weight"
            if (rbLose.isChecked) goal = "Lose Weight"
            if (rbGain.isChecked) goal = "Gain Weight"

            val height = etHeight.text.toString().toIntOrNull() ?: 170
            val weight = etWeight.text.toString().toDoubleOrNull() ?: 70.0

            viewModel.calculateMacros(height, weight, 25, gender, goal)
        }

        findViewById<Button>(R.id.btnFinalizeProfile).setOnClickListener {
            val gender = if (rbMale.isChecked) "Male" else "Female"
            viewModel.saveFullProfile(
                etFirstName.text.toString(),
                etSurname.text.toString(),
                etDob.text.toString(),
                gender,
                etHeight.text.toString().toIntOrNull() ?: 170,
                etWeight.text.toString().toDoubleOrNull() ?: 70.0,
                etTargetCals.text.toString().toIntOrNull() ?: 0, // <--- Grabs edited calories
                etProtein.text.toString().toIntOrNull() ?: 0,
                etFats.text.toString().toIntOrNull() ?: 0,
                etCarbs.text.toString().toIntOrNull() ?: 0
            )
        }
    }
}