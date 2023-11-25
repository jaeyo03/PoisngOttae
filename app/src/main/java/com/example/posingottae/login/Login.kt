package com.example.posingottae.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.MainActivity
import com.example.posingottae.R
import com.example.posingottae.login.SignUp
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailLogin).text.toString()
            val password = findViewById<TextInputEditText>(R.id.passwordLogin).text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {
                        task ->
                    if(task.isSuccessful) {
                        Log.d("emaillogin","loginWithEmail : success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Log.d("emaillogin","loginWithEmail : failure", task.exception)
                    }
                }
        }
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}