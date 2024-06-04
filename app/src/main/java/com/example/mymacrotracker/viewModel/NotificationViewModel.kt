package com.example.mymacrotracker.viewModel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.mymacrotracker.R

class NotificationViewModel() : ViewModel() {

    fun createNotificationChannel(channelId: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MakeitEasy"
            val desc = "My Channel MakeitEasy"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = desc
            }
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun simpleNotification(
        context: Context,
        channelId: String,
        notificationId: Int,
        textTitle: String,
        textContent: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.home)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(priority)
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }

    fun checkAndSendNotification(currentSteps: Int, currentStepsFirebase: Double, stepsGoal: Double, context: Context) {
        val totalSteps = currentSteps + currentStepsFirebase.toInt()
        if (totalSteps.toDouble() == stepsGoal && currentSteps != 0) {
            simpleNotification(context, "0", 0, "Congratulations!", "You reached your daily goal of ${stepsGoal.toInt()} steps. Keep it up!")
        }
    }
}