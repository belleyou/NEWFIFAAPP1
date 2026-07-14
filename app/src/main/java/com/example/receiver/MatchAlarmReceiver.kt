package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class MatchAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val matchId = intent.getStringExtra("matchId") ?: "unknown"
        val teamName = intent.getStringExtra("teamName") ?: "Favorite Team"
        val flag = intent.getStringExtra("flag") ?: "⚽"
        val opponent = intent.getStringExtra("opponent") ?: "Opponent"
        val stadium = intent.getStringExtra("stadium") ?: "World Cup Arena"
        val timeStr = intent.getStringExtra("timeStr") ?: ""
        val isImmediate = intent.getBooleanExtra("isImmediate", false)

        val channelId = "match_reminders"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Match Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts before matches start"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            matchId.hashCode(),
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "$flag Upcoming Match Alert!"
        val text = if (isImmediate) {
            "The match $teamName vs $opponent starts very soon! Tap to follow live."
        } else {
            "Your favorite team $teamName is playing $opponent in 30 minutes! Kickoff at $timeStr."
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(matchId.hashCode(), notification)
    }
}
