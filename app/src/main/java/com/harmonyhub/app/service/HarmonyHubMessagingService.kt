package com.harmonyhub.app.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harmonyhub.app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HarmonyHubMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var tokenRepository: TokenRepository
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        Timber.d("Refreshed FCM token: $token")
        // Store and upload the token to Firestore
        serviceScope.launch {
            tokenRepository.updateFcmToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d("Message received from: ${message.from}")
        
        message.notification?.let { notification ->
            // Handle notification message
            val title = notification.title ?: "Harmony Hub"
            val body = notification.body ?: ""
            showNotification(title, body)
        }
        
        message.data.let { data ->
            // Handle data message
            // Use data payload to determine notification type and content
            when (data["type"]) {
                "message" -> handleMessageNotification(data)
                "schedule" -> handleScheduleNotification(data)
                "expense" -> handleExpenseNotification(data)
                "checkin" -> handleCheckinNotification(data)
                else -> {
                    // Default notification if type is not specified
                    if (message.notification == null) {
                        val title = data["title"] ?: "Harmony Hub"
                        val body = data["body"] ?: ""
                        showNotification(title, body)
                    }
                }
            }
        }
    }
    
    private fun handleMessageNotification(data: Map<String, String>) {
        val sender = data["sender"] ?: "Co-parent"
        val message = data["message"] ?: ""
        showNotification("New message from $sender", message)
    }
    
    private fun handleScheduleNotification(data: Map<String, String>) {
        val title = data["title"] ?: "Schedule Update"
        val body = data["body"] ?: "There has been an update to your co-parenting schedule."
        showNotification(title, body)
    }
    
    private fun handleExpenseNotification(data: Map<String, String>) {
        val title = data["title"] ?: "Expense Update"
        val body = data["body"] ?: "There has been an update to shared expenses."
        showNotification(title, body)
    }
    
    private fun handleCheckinNotification(data: Map<String, String>) {
        val title = data["title"] ?: "Check-in Alert"
        val body = data["body"] ?: "A new check-in has been submitted."
        showNotification(title, body)
    }
    
    private fun showNotification(title: String, body: String) {
        // Create notification channel for Android O and above
        createNotificationChannel()
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification) // This will need to be created
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        
        // Check for notification permission (for API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.w("Notification permission not granted")
                return
            }
        }
        
        NotificationManagerCompat.from(this).notify(
            System.currentTimeMillis().toInt(),
            notificationBuilder.build()
        )
    }
    
    private fun createNotificationChannel() {
        // Create the notification channel, but only on API 26+ (Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = "Harmony Hub notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "harmony_hub_channel"
    }
}

/**
 * Interface for token repository to be injected by Hilt
 */
interface TokenRepository {
    suspend fun updateFcmToken(token: String)
}