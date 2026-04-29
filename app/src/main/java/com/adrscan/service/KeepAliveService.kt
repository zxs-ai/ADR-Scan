package com.adrscan.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.adrscan.R

class KeepAliveService : Service() {

    companion object {
        private const val CHANNEL_ID = "adrscan_keepalive"
        private const val NOTIFICATION_ID = 1001
        private const val KEEP_ALIVE_DURATION_MS = 5 * 60 * 1000L // 5 minutes
    }

    private val handler = Handler(Looper.getMainLooper())
    private val stopRunnable = Runnable { stopSelf() }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        handler.removeCallbacks(stopRunnable)
        handler.postDelayed(stopRunnable, KEEP_ALIVE_DURATION_MS)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(stopRunnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun cancelTimer() {
        handler.removeCallbacks(stopRunnable)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.keepalive_channel),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.keepalive_notification))
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}
