package com.example.topics2.utilities.helper

import android.content.Context
import android.content.Intent
import com.example.topics2.activities.MainActivity

fun restartMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    Runtime.getRuntime().exit(0) // Kill the current process
}