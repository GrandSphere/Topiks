package com.example.topics2.unused.messageScreen




import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHelper {

    const val REQUEST_CODE_PERMISSION = 100

    fun checkAndRequestPermissions(activity: Activity) {
        val permissionsNeeded = mutableListOf<String>()
        var permissionDenied = ""

        // Check if we have permission to record audio
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
            permissionDenied = "Audio recording permission is denied."
        }

        // Check if we have permission to write to external storage (if necessary for your app)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            permissionDenied = "Storage permission is denied."
//        }

        // Request permissions if they are needed
        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsNeeded.toTypedArray(), REQUEST_CODE_PERMISSION)
        }

        // If a permission is already denied, show a specific message
        if (permissionDenied.isNotEmpty()) {
            // Show a toast with the specific denied permission
            Toast.makeText(activity, permissionDenied, Toast.LENGTH_LONG).show()
        }
    }
}
