package com.example.posingottae

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.posingottae.databinding.ActivityMainBinding
import com.example.posingottae.ui.socialmedia.KoGPTActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private var isFabOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_poseanalysis, R.id.navigation_socialmedia ,R.id.navigation_map,R.id.navigation_mypage))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val cameraBtn : FloatingActionButton = binding.floatingCameraBtn
//
//        cameraBtn.setOnClickListener{
//            val options = arrayOf("Camera", "Gallery")
//            AlertDialog.Builder(this)
//                .setTitle("Choose Picture")
//                .setItems(options) { _, which ->
//                    when (which) {
//                        0 -> takePictureLauncher.launch(null)
//                        1 -> pickImageLauncher.launch("image/*")
//                    }
//                }.show()
//        }
        setFABClickEvent()
    }

    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding?.fabMain?.setOnClickListener {
            toggleFab()
        }
        // 플로팅 버튼 클릭 이벤트 - 마이크표시
        binding?.fabCapture?.setOnClickListener {
        }
        // 플로팅 버튼 클릭 이벤트 - kogpt
        binding?.fabShare?.setOnClickListener {
            val intent = Intent(this, KoGPTActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toggleFab() {
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding!!.fabShare, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding!!.fabCapture, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding!!.fabMain, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding!!.fabShare, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(binding!!.fabCapture, "translationY", -180f).apply { start() }
            ObjectAnimator.ofFloat(binding!!.fabMain, View.ROTATION, 0f, 45f).apply { start() }
        }

        isFabOpen = !isFabOpen
    }

}

