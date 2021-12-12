package com.chloedewyes.walkmydog.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.chloedewyes.walkmydog.other.Constants.ACTION_STOP_SERVICE
import com.chloedewyes.walkmydog.other.Constants.NOTIFICATION_CHANNEL_ID
import com.chloedewyes.walkmydog.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.chloedewyes.walkmydog.other.Constants.NOTIFICATION_ID

class TrackingService : LifecycleService() {

    companion object {
        val isTracking = MutableLiveData<Boolean>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    startForegroundService()
                    Log.d("test", "서비스 시작")
                    Log.d("test", "서비스중..")
                }

                ACTION_STOP_SERVICE -> {
                    stopService()
                    Log.d("test", "서비스 중지")
                }
                else -> Log.d("", "else")
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService() {
        isTracking.postValue(false)
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun startForegroundService() {
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_paw_theme)
            .setContentTitle("어야가자")
            .setContentText("어야가자 앱에서 산책이 진행 중입니다..")

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager : NotificationManager ) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}