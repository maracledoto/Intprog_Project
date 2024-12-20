package com.example.intprog_bausing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Ensure Firebase is initialized
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        val emailEditText = findViewById<EditText>(R.id.editText)
        val passwordEditText = findViewById<EditText>(R.id.editText2)
        val registerButton = findViewById<Button>(R.id.button2)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                saveUserToFirebase(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserToFirebase(email: String, password: String) {
        // Use your correct Firebase Realtime Database URL
        val database = FirebaseDatabase.getInstance("https://intprog-bausing-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        val userId = database.child("users").push().key ?: return
        val user = mapOf("email" to email, "password" to password)

        // Log the user data before saving
        Log.d("RegisterActivity", "Saving user: $email, $password")

        // Save user data to Firebase Realtime Database
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User registered successfully")
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                // After successful registration, return to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Log.e("RegisterActivity", "Registration failed", exception)
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
    }
}
