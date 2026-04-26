package com.example.healthsync.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthsync.R
import com.example.healthsync.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    // This connects View to the ViewModel!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Find our UI elements from the XML
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // 2. Listen for messages from the ViewModel
        viewModel.authResult.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()

                // Go to the Dashboard screen
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Closes the login screen so the user can't press 'back' to it
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        // 3. Set up the Button Clicks
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            viewModel.login(email, pass)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            startActivity(intent)
        }
    }
}