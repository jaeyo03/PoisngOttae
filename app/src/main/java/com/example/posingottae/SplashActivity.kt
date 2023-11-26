package com.example.posingottae

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.login.Login

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2초 있다 로그인 액티비티로
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}