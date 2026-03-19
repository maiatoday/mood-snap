package net.maiatoday.moodsnap.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

interface IReminderScheduler {
    fun scheduleReminder(hour: Int, minute: Int)
    fun cancelReminder()
}

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : IReminderScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleReminder(hour: Int, minute: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        
        // Use FLAG_UPDATE_CURRENT to update any existing alarm with the same intent,
        // and FLAG_IMMUTABLE for security on Android 12+ (API 31+).
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Completely clear the calendar and start fresh
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // If the time has already passed today, schedule for tomorrow
        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        Log.d("ReminderScheduler", "Current time: ${java.util.Date(now)}")
        Log.d("ReminderScheduler", "Scheduling inexact repeating alarm for: ${calendar.time}")

        // Use inexact repeating to respect system battery optimizations
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun cancelReminder() {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        // Also cancel the PendingIntent itself
        pendingIntent.cancel()
    }

    companion object {
        const val REMINDER_REQUEST_CODE = 1001
    }
}
