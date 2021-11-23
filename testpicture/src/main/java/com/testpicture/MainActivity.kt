package com.testpicture

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.FileProvider
import android.Manifest.permission.CAMERA
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    /** Defines an [ActivityResultLauncher] which will be used to execute a contract to
        launch camera activity, and get a picture as result. */
    private val requestPhoto: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isAvailable ->
            if (isAvailable) updateImageViewWithCameraCapture()
        }

    /** String value defining the camera permission access */
    private val cameraPermission = CAMERA

    /** [ActivityResultLauncher] for a camera permission request */
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isEnabled ->
            if (isEnabled) { // Permission Granted
                requestPhoto.launch(uri)
            }
            else { // Permission Denied
                if (!shouldShowRequestPermissionRationale(this, cameraPermission))
                    accessApplicationSettings()
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }

    /** Contains URI reference for camera capture */
    private lateinit var uri: Uri

    /** Contains File reference for camera capture */
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createImageFile()
        createUri()
        handleButton()
    }

    /**
     * Handles click events on main activity button.
     */
    private fun handleButton() {
        findViewById<MaterialButton>(R.id.button).setOnClickListener {
            // Must check if app is authorized to access Camera before requesting any photo
            requestPermission.launch(cameraPermission)
        }
    }

    /**
     * Defines an image file.
     */
    @SuppressLint("SimpleDateFormat")
    private fun createImageFile() {
        val timeStamp: String = SimpleDateFormat("yyyHHdd_HHmmss").format(Date())
        val storageDirectory: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        file = File.createTempFile("JPEG_$timeStamp", ".jpg", storageDirectory)
    }

    /**
     * Defines an uri.
     */
    private fun createUri() {
        uri = FileProvider.getUriForFile(this, resources.getString(R.string.authority), file)
    }

    /**
     * Update background AppCompatImageView with camera result.
     */
    private fun updateImageViewWithCameraCapture() {
        val imageView = findViewById<AppCompatImageView>(R.id.image_from_camera)
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .override(imageView.width, imageView.height)
            .into(imageView)
    }

    /**
     * Allows user to access application settings to manually update permissions status.
     * Called when user has clicked the "Don't ask again" option when requesting permissions.
     */
    private fun accessApplicationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(resources.getString(R.string.package_name))
        startActivity(intent)
    }
}