package com.testwithcustomcontract

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.button.MaterialButton

/**
 * Test activity designed to send back a result to [MainActivity].
 */
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        handleButton()
    }

    /**
     * Handles click events on second activity button.
     */
    private fun handleButton() {
        findViewById<MaterialButton>(R.id.button_second_activity).setOnClickListener {
            sendEditTextValueToMainActivity()
        }
    }

    /**
     * Send result (AppCompatEditText String value) to MainActivity using [SecondActivityContract].
     */
    private fun sendEditTextValueToMainActivity() {
        val editText = findViewById<AppCompatEditText>(R.id.edit_text)
        editText.text?.let { editable ->
            // Get String value from AppCompatEditText
            val result = editable.toString()
            // Create Intent containing activity result
            Intent().apply {
                putExtra("result_key", result)
                setResult(Activity.RESULT_OK, this)
            }
            // Go back to previous activity
            finish()
        }
    }
}