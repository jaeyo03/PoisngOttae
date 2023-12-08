package com.example.posingottae.ui.poseanalysis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.posingottae.R

class LocalNotificationManager(private val context: Context) {

    private val channelId = "YourChannelId"
    private val capturedImageUri = CameraActivity.ImageUriHolder.capturedImageUri
    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Your Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendGoodPositionNotification() {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.posingottae)
            .setContentTitle("WoW you have great pose")
            .setContentText("You can check your pose, and share on Instagram!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)  // 알림을 탭하면 자동으로 삭제되도록 설정
            .setContentIntent(getInstagramShareIntent(capturedImageUri))  // 탭 시의 동작 설정
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun getInstagramShareIntent(capturedImageUri: Uri?): PendingIntent {
        // 인스타그램으로 이미지를 공유하기 위한 Intent를 생성
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"

        val flags = PendingIntent.FLAG_IMMUTABLE
        // 캡처된 이미지의 URI를 설정
        shareIntent.putExtra(Intent.EXTRA_STREAM, capturedImageUri)

        shareIntent.`package` = "com.instagram.android"  // Instagram 앱 패키지명

        // PendingIntent를 생성하여 알림을 탭했을 때 해당 Intent를 실행
        return PendingIntent.getActivity(
            context,
            0,
            Intent.createChooser(shareIntent, "Share on Instagram"),
            flags
        )
    }

}



