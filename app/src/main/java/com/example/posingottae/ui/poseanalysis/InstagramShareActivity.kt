package com.example.posingottae.ui.poseanalysis

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.posingottae.R

class InstagramShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the intent that started this activity
        val receivedIntent = intent

        // Check if the received intent has the data we need
        if (receivedIntent != null && receivedIntent.action == Intent.ACTION_SEND) {
            val sharedImageUri = receivedIntent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)

            // Check if the received content is an image
            if (sharedImageUri != null) {
                // Handle the Instagram sharing logic here
                shareOnInstagram(sharedImageUri)
            } else {
                // If it's not an image, show a toast or handle it accordingly
                Toast.makeText(this, "Invalid content type", Toast.LENGTH_SHORT).show()
            }
        }

        // Finish the activity
        finish()
    }

    private fun shareOnInstagram(imageUri: Uri) {
        val instagramPackage = "com.instagram.android"

        // Check if Instagram is installed
        if (isAppInstalled(instagramPackage)) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            intent.setPackage(instagramPackage) // Instagram package name

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle error if Instagram sharing fails
                Toast.makeText(this, "Failed to open Instagram", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Instagram is not installed, prompt the user to install it
            openPlayStoreForApp(instagramPackage)
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openPlayStoreForApp(packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // If Play Store app is not available, open the Play Store website
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
    }

}