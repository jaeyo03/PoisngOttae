package com.example.posingottae.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.R
import com.example.posingottae.login.Login
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        val signupBtn = findViewById<Button>(R.id.signUpButton)
        signupBtn.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<TextInputEditText>(R.id.password2).text.toString()

            if (password == confirmPassword){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            // Set display name for the user
                            val userName = findViewById<TextInputEditText>(R.id.username).text.toString()
                            val profileUpdate = UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build()

                            user?.updateProfile(profileUpdate)
                                ?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        Log.d("signup", "User profile updated.")
                                    } else {
                                        Log.w("signup", "Failed to update user profile.", profileUpdateTask.exception)
                                    }
                                }

                            // Save user data to Firestore
                            val firestore = FirebaseFirestore.getInstance()
                            val userMap = hashMapOf(
                                "email" to email,
                                "username" to userName
                                // Add any other user-related data you want to store in Firestore
                            )

                            firestore.collection("users").document(user!!.uid)
                                .set(userMap)
                                .addOnSuccessListener {
                                    Log.d("signup", "User data added to Firestore.")
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("signup", "Error adding user data to Firestore", e)
                                    Toast.makeText(
                                        baseContext,
                                        "Failed to add user data to Firestore.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Log.d("signup", "createUserwithEmail: failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication Failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext,
                    "Passwords do not match.\n Check Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
