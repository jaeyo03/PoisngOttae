package com.example.posingottae

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.posingottae.databinding.ActivityMainBinding
import com.example.posingottae.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}