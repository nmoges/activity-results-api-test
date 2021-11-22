package com.testpermission

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton

/**
 * Main activity used to test two use cases with the Activity Results API pre-built contract
 * "RequestPermission()".
 * - Use case #1 : Single permission request
 * - Use case #2 : Multi permissions request
 */
class MainActivity : AppCompatActivity() {

    /** String value defining the camera permission access */
    private val cameraPermission = CAMERA

    /** [ActivityResultLauncher] for a single permission request */
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show()
        }
        else {
            if (shouldShowRequestPermissionRationale(this, CAMERA) ) {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
            else { // "Don't ask again" selected
                accessApplicationSettings()
            }
        }
    }

    /** List of String values defining several permission accesses */
    private val listPermissions: Array<String> = arrayOf(READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION)

    /** [ActivityResultLauncher] for a multi permissions request */
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mapResult ->
            if (mapResult[READ_EXTERNAL_STORAGE] == true &&
                mapResult[ACCESS_COARSE_LOCATION] == true) { // "Don't ask again" selected for both permissions
                Toast.makeText(this, R.string.all_permission_granted, Toast.LENGTH_SHORT).show()
            }
            else {
                if (!shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE) &&
                        !shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)) {
                    accessApplicationSettings()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleButtons()
    }

    /**
     * Handles click events on activity buttons.
     */
    private fun handleButtons() {
        // First button : handle camera permission request
        findViewById<MaterialButton>(R.id.request_permission_button).setOnClickListener {
            requestPermission.launch(cameraPermission)
        }

        // Second button : handle list of permission request
        findViewById<MaterialButton>(R.id.request_multi_permissions_button).setOnClickListener {
            requestMultiplePermissions.launch(listPermissions)
        }
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