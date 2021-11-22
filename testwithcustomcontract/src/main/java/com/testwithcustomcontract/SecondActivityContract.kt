package com.testwithcustomcontract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * [ActivityResultContract] subclass defining a contract returning a String result.
 */
class SecondActivityContract: ActivityResultContract<Unit, String?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        // No extra needed in this case
        return Intent(context, SecondActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return when {
            resultCode != Activity.RESULT_OK -> null
            else -> intent?.getStringExtra("result_key")
        }
    }
}