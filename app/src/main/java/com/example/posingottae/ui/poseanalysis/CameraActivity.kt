package com.example.posingottae.ui.poseanalysis

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.posingottae.databinding.ActivityCameraBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
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

    data class TargetPose(
        val targets: List<PoseAnalysis.TargetShape>
    )

    // 정답 각도
    private val poseFrontSpread: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                170.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                80.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                170.0
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                80.0
            )
        )
    )


    // 자세마다 이렇게 하나하나 만들어줘야함
    private val poseFrontDoubleBiceps: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                167.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                71.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                167.0
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                71.0
            )
        )
    )

    private val poseFrontAbd: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                172.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                70.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                180.0
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                55.0
            )
        )
    )

    private val poseFrontLineup: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                168.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                106.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                168.0
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                106.0
            )
        )
    )

    private val poseFrontTriceps: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                144.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                171.7
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                124.9
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                164.5
            )
        )
    )

    private val poseBackDouble: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                166.1
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                72.6
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                173.2
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                67.8
            )
        )
    )

    private val poseBacklat: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                163.4
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                71.0
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                175.3
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                79.9
            )
        )
    )

    private val poseMuscular1: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                170.4
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                96.5
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                177.8
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                88.6
            )
        )
    )

    private val poseMuscular2: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                176.1
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                76.9
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                179.2
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                72.2
            )
        )
    )

    private val poseSideChest: TargetPose = TargetPose(
        listOf(
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP,
                146.5
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.LEFT_WRIST,
                PoseLandmark.LEFT_ELBOW,
                PoseLandmark.LEFT_SHOULDER,
                101.7
            ),
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_ANKLE,
                PoseLandmark.RIGHT_KNEE,
                PoseLandmark.RIGHT_HIP,
                119.7
            )
            ,
            PoseAnalysis.TargetShape(
                PoseLandmark.RIGHT_WRIST,
                PoseLandmark.RIGHT_ELBOW,
                PoseLandmark.RIGHT_SHOULDER,
                89.2
            )
        )
    )

    var poseAnswer = false
    private var isAutoCaptureEnabled = true
    val list1 : MutableList<Double> = mutableListOf()
    val list2 : MutableList<Double> = mutableListOf()
    var angleResult = PoseAnalysis.AnglesResult(list1,list2)

    var userAnglesList : MutableList<Double> = mutableListOf()
    var answerAnglesList : MutableList<Double> = mutableListOf()

    var poseInformation = ""

    // CameraX 이미지를 분석후, Landmark(좌표값)을 리턴해준다. 좌표값은 pose에 담겨있다.
    val onPoseDetected: (pose: Pose) -> Unit = { pose ->
//        for (landmark in pose.allPoseLandmarks) {
//            val landmarkType = landmark.landmarkType
//            val position = landmark.position
//            Log.d("PoseInfo", "Type: $landmarkType, x: ${position.x}, y: ${position.y}")
//            // 여기서 landmarkType은 그냥 0~32 까지의 숫자로만 나옴
//        }

        // 선택된 포즈 정보 불러오기
        val selectedPhoto = intent.getStringExtra("selectedPhoto")
        poseInformation = selectedPhoto.toString()
        val selectedPose = when(selectedPhoto){
            "FrontAbd" -> poseFrontAbd
            "FrontLineup" -> poseFrontLineup
            "Frontdb" -> poseFrontDoubleBiceps
            "FrontSpread" -> poseFrontSpread
            "FrontTriceps" -> poseFrontTriceps
            "Muscular1" -> poseMuscular1
            "Muscular2" -> poseMuscular2
            "SideChest" -> poseSideChest
            "BackDouble" -> poseBackDouble
            "Backlat" -> poseBacklat
            else -> {
                null
            }
        }

        if (selectedPose != null){
            val poseAnalysis = PoseAnalysis(pose,selectedPose)
            // 유사도 분석으로 넘어가는 코드
            //유사도 true,false로 결과 출력
            poseAnswer = poseAnalysis.match(pose,selectedPose)
            angleResult = poseAnalysis.showAngle(pose,selectedPose)
            userAnglesList = angleResult.userAnglesList
            answerAnglesList = angleResult.answerAnglesList
            //밑에 알람으로 가는 코드... 보완이 필요
            checkAndCapture(poseAnswer)
        } else{
            Toast.makeText(this, "No selected Pose! Please select the Pose!", Toast.LENGTH_SHORT).show()
        }
    }

    var photoUri : Uri? = null
    // Start onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }



        // Set up the listeners for take photo
        viewBinding.imageCaptureButton.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Take Photo?")
                .setMessage("Do you want to take the photo?")
                .setPositiveButton("Agree") { dialog, which ->
                    takePhoto()
                }
                .setNegativeButton("Disagree", null)
                .show()
        }

        // Set up the listeners for gallery
        viewBinding.galleryButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Open Gallery?")
                .setMessage("Do you want to open the gallery?")
                .setPositiveButton("Agree") { dialog, which ->
                    openGalleryForImage()
                }
                .setNegativeButton("Disagree", null)
                .show()
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
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
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
                    photoUri = output.savedUri
                    val savedUri = output.savedUri ?: return
                    analyzePoseFromUri(savedUri)
                    Log.d(TAG, msg)
                }
            }
        )
    }
    object ImageUriHolder {
        var capturedImageUri: Uri? = null
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
                    CameraActivity.CameraAnalyzer(poseDetector,onPoseDetected)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also{
                    it.setAnalyzer(cameraExecutor,
                        CameraActivity.CameraAnalyzer(poseDetector, onPoseDetected)
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture ,imageAnalyzer) //imageAnalyzer 그냥 넣으면 됨

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
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            data?.data?.let { uri ->
                analyzePoseFromUri(uri)
                photoUri = uri
            }
            val intent = Intent(this, ResultActivity::class.java)
            intent.apply {
                putExtra("UserAngle" , userAnglesList.toString() )
                putExtra("AnswerAngle", answerAnglesList.toString())
                putExtra("PoseInformation",poseInformation)
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, photoUri)
                type = "image/jpeg"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        }
    }

    // URI로부터 사진을 분석하는 함수
    private fun analyzePoseFromUri(uri: Uri) {
        val image = InputImage.fromFilePath(this, uri)
        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                onPoseDetected(pose)
                val intent = Intent(this, ResultActivity::class.java)
                intent.apply {
                    putExtra("UserAngle" , userAnglesList.toString() )
                    putExtra("AnswerAngle", answerAnglesList.toString())
                    putExtra("PoseInformation",poseInformation)
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, photoUri)
                    type = "image/jpeg"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // 에러 처리
            }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
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
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }


    // -----------------------------------------------------------------  여기서 부터 실시간 캡쳐와 푸쉬 알림 ------------------------------------------------


    // 로컬푸시알림 구현

    private val localNotificationManager: LocalNotificationManager by lazy {
        LocalNotificationManager(this)
    }
    // 자동 캡쳐 및 푸시 알림 여부를 검사하는 함수
    private fun checkAndCapture(answerScore:Boolean) {
        // 최적의 포즈와 현재 포즈가 일치하는지 확인
        if (answerScore && isAutoCaptureEnabled) {
            // 자동으로 캡쳐
            takePhoto()
//            // 푸시 알림 전송
//            getFCMToken()
            localNotificationManager.sendGoodPositionNotification()
            // 자동 캡쳐를 비활성화하거나 필요에 따라 플래그를 수정할 수 있음
            isAutoCaptureEnabled = false
        }
    }

    // 푸시 알림을 보내는 함수
    private fun sendPushNotification(token: String) {
        // 여기에 푸시 알림을 보내는 로직을 추가
        // FCM 메시지 생성
        val notification = JSONObject()
        val notificationBody = JSONObject()

        try {
            notificationBody.put("title", "알림 제목")
            notificationBody.put("body", "알림 내용")

            notification.put("to", token)
            notification.put("data", notificationBody)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // FCM 메시지 전송
        val request = object : JsonObjectRequest(
            Method.POST,
            "https://fcm.googleapis.com/fcm/send",
            notification,
            Response.Listener { response ->
                // 푸시 알림 전송 성공 시의 처리
                Log.d(TAG, "Push notification sent successfully")
            },
            Response.ErrorListener { error ->
                // 푸시 알림 전송 실패 시의 처리
                Log.e(TAG, "Error sending push notification: $error")
            }
        ) {
            // FCM 서버에 요청 헤더 추가
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "AAAA-VdO4-M:APA91bErPZ4l2NP7ml7iwVBW-ySuEyIY7jlSeM7l56QKOWFDR1s34O7b1jf3QMDoGnm3HrjL326HCLj45H8zuDQeUgUftvem1KbNbzn_4f2BM5LcNxvJO21QihE3cvCn5jno1fq55ARg" // Firebase Console에서 확인한 서버 키로 변경
                return headers
            }
        }

        // 요청 큐에 요청 추가
        Volley.newRequestQueue(this).add(request)
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // 토큰을 사용하여 푸시 알림을 보낼 수 있음
                sendPushNotification(token)
            } else {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            }
        })
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