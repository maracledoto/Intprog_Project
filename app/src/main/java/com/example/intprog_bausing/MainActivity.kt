package com.example.intprog_bausing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ensure Firebase is initialized
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        val emailEditText = findViewById<EditText>(R.id.editText)
        val passwordEditText = findViewById<EditText>(R.id.editText2)
        val loginButton = findViewById<Button>(R.id.button)
        val registerTextView = findViewById<TextView>(R.id.register)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun authenticateUser(email: String, password: String) {
        // Use the correct Firebase Realtime Database URL
        val database = FirebaseDatabase.getInstance("https://intprog-bausing-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Log the login attempt
        Log.d("MainActivity", "Attempting login for user: $email")

        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in snapshot.children) {
                    val userEmail = userSnapshot.child("email").getValue(String::class.java)
                    val userPassword = userSnapshot.child("password").getValue(String::class.java)

                    // Check if the credentials match
                    if (email == userEmail && password == userPassword) {
                        userFound = true
                        break
                    }
                }

                if (userFound) {
                    Log.d("MainActivity", "Login successful for: $email")
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, UserListActivity::class.java))
                    finish()
                } else {
                    Log.d("MainActivity", "Invalid credentials for: $email")
                    Toast.makeText(this@MainActivity, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error in case of failure
                Log.e("MainActivity", "Database error: ${error.message}")
                Toast.makeText(this@MainActivity, "Error occurred. Try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
