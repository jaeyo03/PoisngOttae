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
import com.google.firebase.auth.auth

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

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Log.d("signup", "createUserWithEmail : success")
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        }
                        else {
                            Log.d("signup", "createUserwithEmail : failure",task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication Failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else {
                Toast.makeText(
                    baseContext,
                    "Passwords do not match.\n Check Again",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}