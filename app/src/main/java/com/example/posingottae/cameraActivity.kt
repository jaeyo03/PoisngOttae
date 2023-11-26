package com.example.posingottae

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.PermissionChecker
import com.example.posingottae.databinding.ActivityCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit

class cameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    // 포즈 인식 클라이언트에 적용되는 옵션. 이미지 분석 -> SINGLE_IMAGE_MODE
    val options by lazy {
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
    }


    // 포즈 인식 클라이언트 생성. 위에서 선언한 옵션값이 적용됨.
    val poseDetector by lazy {
        PoseDetection.getClient(options)
    }

    // CameraX 이미지를 분석후, Landmark(좌표값)을 리턴해준다. 좌표값은 pose에 담겨있다.
    val onPoseDetected: (pose: Pose) -> Unit = { pose ->
        Log.d("PoseInfo", pose.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        // Set up the listeners for take photo
        viewBinding.imageCaptureButton.setOnClickListener {
            takePhoto()
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
        viewBinding.galleryButton.setOnClickListener {
            openGalleryForImage()
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
        cameraExecutor = Executors.newSingleThreadExecutor()




    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    val savedUri = output.savedUri ?: return
                    analyzePoseFromUri(savedUri)
                    Log.d(TAG, msg)
                }
            }
        )
    }

    //카메라 시작하는 함수
    private fun startCamera() {
        // ProcessCameraProvider 인스턴스 생성. 카메라의 생명주기를 수명 주기 소유자와 바인딩
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // 아래는 cameraProviderFuture.addListener(Runnable {}, ContextCompat.getMainExecutor(this)) 이 구조임
        cameraProviderFuture.addListener({
            // Runnable {} 임.
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Image Capture
            imageCapture = ImageCapture.Builder().build()
                .also {
                    cameraActivity.CameraAnalyzer(poseDetector,onPoseDetected)
                }

//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also{
//                    it.setAnalyzer(cameraExecutor,
//                        cameraActivity.CameraAnalyzer(poseDetector, onPoseDetected)
//                    )
//                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture ) //imageAnalyzer 그냥 넣으면 됨

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // 갤러리에서 사진을 가져오는 함수
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // 갤러리에서 사진을 선택한 후 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            data?.data?.let { uri ->
                analyzePoseFromUri(uri)
            }
        }
    }

    // URI로부터 사진을 분석하는 함수
    private fun analyzePoseFromUri(uri: Uri) {
        val image = InputImage.fromFilePath(this, uri)
        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                for (landmark in pose.allPoseLandmarks) {
                    val landmarkType = landmark.landmarkType
                    val position = landmark.position
                    Log.d("PoseInfo", "Type: $landmarkType, x: ${position.x}, y: ${position.y}")
                }
                Log.d("PoseInfo", pose.toString())
                // 여기서 pose 처리
            }
            .addOnFailureListener { e ->
                // 에러 처리
            }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private class CameraAnalyzer(
        private val poseDetector: PoseDetector,
        private val onPoseDetected: (pose: Pose) -> Unit
    ) : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image ?: return
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            poseDetector.process(inputImage)
                .addOnSuccessListener { pose ->
                    onPoseDetected(pose)
                }
                .addOnFailureListener { e ->
                    //handle error
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    mediaImage.close()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    //퍼미션 체크. 권한을 부여할것인지 확인한다
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val GALLERY_REQUEST_CODE = 1001
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}