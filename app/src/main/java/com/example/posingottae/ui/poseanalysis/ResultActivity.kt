package com.example.posingottae.ui.poseanalysis

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.posingottae.MainActivity
import com.example.posingottae.R
import com.example.posingottae.databinding.ActivityResultBinding
import com.example.posingottae.ui.poseanalysis.PictureFragment.BlankFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBackdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseBacklatFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontAbdFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontTricepsFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontdbFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseFrontspreadFragment
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment1
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseMusFragment2
import com.example.posingottae.ui.poseanalysis.PictureFragment.PoseSideFragment
import java.io.IOException
import kotlin.math.abs


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val intent = intent

        // 인텐트에서 이미지 URI 받기
        val imageUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)

        Log.d("ImageURI",imageUri.toString())
        // URI가 null이 아닌 경우에 이미지를 띄움
        imageUri?.let {
            val bitmap = getBitmapFromUri(this, it)
            // Bitmap을 ImageView에 설정
            binding.yourPose.setImageBitmap(bitmap)
        }



        val userAngle = intent.getStringExtra("UserAngle")
        val answerAngle = intent.getStringExtra("AnswerAngle")
        val poseInformation = intent.getStringExtra("PoseInformation")
        Log.d("ITM",poseInformation.toString())
        if (poseInformation != null) {
            replaceFragment(poseInformation)
        }

        // 대괄호를 제거 , 공백 제거 쉼표로 숫자들을 분리
        val userAnglesString = userAngle?.replace("[", "")?.replace("]", "")?.split(", ")
        val answerAnglesString = answerAngle?.replace("[", "")?.replace("]", "")?.split(", ")

        // 각 숫자를 Double로 변환하여 저장
        val userLeftLeg = userAnglesString?.get(0)?.toDouble()
        val userLeftArm = userAnglesString?.get(1)?.toDouble()
        val userRightLeg = userAnglesString?.get(2)?.toDouble()
        val userRightArm = userAnglesString?.get(3)?.toDouble()

        val answerLeftLeg = answerAnglesString?.get(0)?.toDouble()
        val answerLeftArm = answerAnglesString?.get(1)?.toDouble()
        val answerRightLeg = answerAnglesString?.get(2)?.toDouble()
        val answerRightArm = answerAnglesString?.get(3)?.toDouble()

        binding.userLeftArm.text = userLeftArm.toString()
        binding.userLeftLeg.text= userLeftLeg.toString()
        binding.userRightArm.text = userRightArm.toString()
        binding.userRightLeg.text = userRightLeg.toString()

        binding.answerLeftArm.text = answerLeftArm.toString()
        binding.answerLeftLeg.text = answerLeftLeg.toString()
        binding.answerRightArm.text = answerRightArm.toString()
        binding.answerRightLeg.text = answerRightLeg.toString()


        // 각 숫자의 차이 계산
        val diffLeftLeg = abs(userLeftLeg!! - answerLeftLeg!!)
        val diffLeftArm = abs(userLeftArm!! - answerLeftArm!!)
        val diffRightLeg = abs(userRightLeg!! - answerRightLeg!!)
        val diffRightArm = abs(userRightArm!! - answerRightArm!!)

        val avgDiff = (diffLeftLeg + diffLeftArm + diffRightLeg + diffRightArm) / 4.0

        val rating = when{
            avgDiff < 3.0 -> 5
            avgDiff < 5.0 -> 4
            avgDiff < 7.0 -> 3
            avgDiff < 9.0 -> 2
            avgDiff < 10.0 -> 1
            else -> 0
        }

        binding.reviewText.text = when(rating){
            5 -> "Your pose is awesome!!"
            4 -> "Your pose is great!"
            3 -> "Your pose is not bad"
            2 -> "Your pose is not good"
            1 -> "Your pose is bad"
            else -> "You need to check your pose..."
        }

        val ratingBar = binding.ratingAngleBar

        ratingBar.rating = rating.toFloat()


        val backBtn = binding.backBtn
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun replaceFragment(fragmentName: String) {
        val fragment = when (fragmentName) {
            "FrontAbd" -> PoseFrontAbdFragment()
            "FrontLineup" -> PoseFrontFragment()
            "Frontdb" -> PoseFrontdbFragment()
            "FrontSpread" -> PoseFrontspreadFragment()
            "FrontTriceps" -> PoseFrontTricepsFragment()
            "Muscular1" -> PoseMusFragment1()
            "Muscular2" -> PoseMusFragment2()
            "SideChest" -> PoseSideFragment()
            "BackDouble" -> PoseBackdbFragment()
            "Backlat" -> PoseBacklatFragment()
            else -> BlankFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

//    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
//        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            BitmapFactory.decodeStream(inputStream)
//        }
//    }

    private fun getBitmapFromUri(context: Context,uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        return rotateImageIfRequired(bitmap, uri)
    }

    private fun rotateImageIfRequired(img: Bitmap, uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri) ?: return img
        val exif = try {
            ExifInterface(inputStream)
        } catch (e: IOException) {
            return img
        }

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }
}