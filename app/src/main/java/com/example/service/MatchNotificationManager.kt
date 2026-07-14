package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.receiver.MatchAlarmReceiver
import java.text.SimpleDateFormat
import java.util.Locale

object MatchNotificationManager {
    private const val TAG = "MatchNotificationMgr"

    fun parseMatchTimeToEpoch(dateStr: String, timeStr: String): Long {
        val cleanTime = timeStr.replace("Local", "").trim()
        val dateTimeStr = "$dateStr $cleanTime"
        val sdf = SimpleDateFormat("MMMM d, yyyy HH:mm", Locale.US)
        return try {
            val date = sdf.parse(dateTimeStr)
            date?.time ?: 0L
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing date time: $dateTimeStr", e)
            0L
        }
    }

    fun scheduleMatchAlarm(
        context: Context,
        matchId: String,
        teamName: String,
        flag: String,
        opponent: String,
        stadiumName: String,
        timeStr: String,
        epochMillis: Long
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = epochMillis - 30 * 60 * 1000 // 30 minutes before kickoff
        val now = System.currentTimeMillis()

        if (triggerTime <= now) {
            if (epochMillis > now) {
                // Schedule immediately (in 2 seconds)
                val intent = Intent(context, MatchAlarmReceiver::class.java).apply {
                    putExtra("matchId", matchId)
                    putExtra("teamName", teamName)
                    putExtra("flag", flag)
                    putExtra("opponent", opponent)
                    putExtra("stadium", stadiumName)
                    putExtra("timeStr", timeStr)
                    putExtra("isImmediate", true)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    matchId.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.set(AlarmManager.RTC_WAKEUP, now + 2000, pendingIntent)
                Log.d(TAG, "Scheduled immediate reminder for match: $matchId")
            } else {
                Log.d(TAG, "Match $matchId is in the past, skipping reminder.")
            }
            return
        }

        val intent = Intent(context, MatchAlarmReceiver::class.java).apply {
            putExtra("matchId", matchId)
            putExtra("teamName", teamName)
            putExtra("flag", flag)
            putExtra("opponent", opponent)
            putExtra("stadium", stadiumName)
            putExtra("timeStr", timeStr)
            putExtra("isImmediate", false)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            matchId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
        Log.d(TAG, "Scheduled reminder for match $matchId at: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(java.util.Date(triggerTime))}")
    }

    fun cancelMatchAlarm(context: Context, matchId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MatchAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            matchId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Cancelled reminder for match: $matchId")
        }
    }
}
