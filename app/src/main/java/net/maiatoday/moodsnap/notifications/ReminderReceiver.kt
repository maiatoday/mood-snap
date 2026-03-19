package net.maiatoday.moodsnap.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.maiatoday.moodsnap.data.UserPreferenceRepository
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository

    @Inject
    lateinit var notificationHelper: INotificationHelper

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderReceiver", "onReceive triggered! Intent: ${intent.action}")
        
        // Tell Android to keep the CPU awake because we are doing async work
        val pendingResult = goAsync()
        
        scope.launch {
            try {
                Log.d("ReminderReceiver", "Fetching preferences from DataStore...")
                val prefs = userPreferenceRepository.preferencesFlow.first()
                Log.d("ReminderReceiver", "Preferences fetched. Reminder enabled: ${prefs.reminderEnabled}")
                
                if (prefs.reminderEnabled) {
                    Log.d("ReminderReceiver", "Showing notification...")
                    notificationHelper.showReminderNotification()
                } else {
                    Log.d("ReminderReceiver", "Reminder is disabled, skipping notification.")
                }
            } catch (e: Exception) {
                Log.e("ReminderReceiver", "Error in onReceive coroutine", e)
            } finally {
                // Must be called so the system knows it can sleep the device again
                Log.d("ReminderReceiver", "Releasing wakelock (pendingResult.finish)")
                pendingResult.finish()
            }
        }
    }
}
