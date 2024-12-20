package com.example.intprog_bausing

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        // Ensure you're using the correct Firebase Realtime Database URL here
        val database = FirebaseDatabase.getInstance("https://intprog-bausing-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("UserListActivity", "onDataChange triggered") // Check if data is being fetched
                linearLayout.removeAllViews() // Clear previous data

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userEmail = userSnapshot.child("email").getValue(String::class.java)
                        Log.d("UserListActivity", "Found user email: $userEmail") // Log user email for debugging

                        if (userEmail != null) {
                            val emailTextView = TextView(this@UserListActivity)
                            emailTextView.text = userEmail
                            emailTextView.textSize = 18f
                            emailTextView.setPadding(16, 16, 16, 16)

                            // Add TextView to LinearLayout
                            linearLayout.addView(emailTextView)
                        }
                    }
                } else {
                    Log.d("UserListActivity", "No users found in the database")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserListActivity", "Error fetching data: ${error.message}")
            }
        })
    }
}
