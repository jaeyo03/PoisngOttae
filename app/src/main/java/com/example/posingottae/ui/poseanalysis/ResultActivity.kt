package com.example.posingottae.ui.poseanalysis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.posingottae.databinding.ActivityMainBinding
import com.example.posingottae.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val poseResult = intent.getBooleanExtra("PoseResult",false)
        val targetAngle = intent.getDoubleExtra("TargetAngle",0.0)
        val yourAngle = intent.getDoubleExtra("YourAngle",0.0)
        Log.d("ITM","$poseResult $targetAngle $yourAngle")
        val backBtn = binding.backBtn
        backBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}