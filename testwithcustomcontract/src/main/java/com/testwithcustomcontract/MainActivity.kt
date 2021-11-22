package com.testwithcustomcontract

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

/**
 * Test activity.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Defines an [ActivityResultLauncher] which will be used to execute a [SecondActivityContract],
     * and get back the String result.
     */
    private val register: ActivityResultLauncher<Unit> =
        registerForActivityResult(SecondActivityContract()) { result ->
            result?.let { updateMaterialTextView(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleButton()
    }

    /**
     * Handles click events on main activity button.
     */
    private fun handleButton() {
        findViewById<MaterialButton>(R.id.button_main_activity).setOnClickListener {
            // Launch the contract
            register.launch()
        }
    }

    /**
     * Updates background MaterialTextView with [SecondActivityContract] String result.
     * @param newText : result returned by the contract
     */
    private fun updateMaterialTextView(newText: String) {
        findViewById<MaterialTextView>(R.id.background_text).apply {
            this.text = newText
        }
    }
}