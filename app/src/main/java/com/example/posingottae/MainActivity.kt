package com.example.posingottae

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.posingottae.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

//Activity Launcher 초기화
private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
    // 카메라에서 찍은 사진 처리: bitmap 객체를 사용
}
private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    // 갤러리에서 선택한 이미지 처리: uri 객체를 사용
}
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

        val cameraBtn : FloatingActionButton = binding.floatingCameraBtn

        cameraBtn.setOnClickListener{
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(this)
                .setTitle("Choose Picture")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> takePictureLauncher.launch(null)
                        1 -> pickImageLauncher.launch("image/*")
                    }
                }.show()
        }
    }
}