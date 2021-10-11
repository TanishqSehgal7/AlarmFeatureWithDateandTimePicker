package com.example.alarmfeaturewithdateandtimepicker

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class BroadcastReceiver : BroadcastReceiver() {

    final val notificationId:Int = 123

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent=Intent(context,Main2Activity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder= context?.let { NotificationCompat.Builder(it,"alarmNotification") }
        notificationBuilder?.setSmallIcon(R.drawable.ic_launcher_background)?.setContentTitle("Alarm Feature Test")
            ?.setContentText("This is a test notification for your alarm")?.setAutoCancel(true)
            ?.setDefaults(NotificationCompat.DEFAULT_ALL)?.priority = NotificationCompat.PRIORITY_HIGH
        notificationBuilder?.setContentIntent(pendingIntent)

        val notificationManager= context?.let { NotificationManagerCompat.from(it) }
        if (notificationBuilder != null) {
            notificationManager?.notify(notificationId,notificationBuilder.build())
        }
    }
}