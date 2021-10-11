package com.example.alarmfeaturewithdateandtimepicker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.alarmfeaturewithdateandtimepicker.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding
    lateinit var timePicker:MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        calendar = Calendar.getInstance()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager==null){
            alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        // Create Notification Channel
        CreateNotificationChannel()

        viewBinding.setAlarm.setOnClickListener {
           SetAlarm()
            Toast.makeText(applicationContext, "Alarm has been set successfully",Toast.LENGTH_SHORT).show()
        }

        viewBinding.cancelAlarm.setOnClickListener {
            cancelAlarm()
            Toast.makeText(applicationContext, "Alarm has been cancelled",Toast.LENGTH_SHORT).show()
        }

        viewBinding.TimePicker.setOnClickListener {
            ShowDateAndTimePicker()
        }
    }

    fun CreateNotificationChannel() { 

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName:CharSequence = "MyAlarmNotificationChannel"
            val description:String="Channel for my Alarm Notification"
            val importance:Int=NotificationManager.IMPORTANCE_HIGH
            val notificationChannel= NotificationChannel("alarmNotification",channelName,importance)
            notificationChannel.description=description

            val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    fun ShowDateAndTimePicker() {
        timePicker=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time for Alarm")
            .build()

        timePicker.show(supportFragmentManager,"alarmNotification")
        timePicker.addOnPositiveButtonClickListener {
            if (timePicker.hour>12){
                viewBinding.SelectedTime.text=(timePicker.hour-12).toString() + " : " + timePicker.minute.toString()
            }
            else {
                viewBinding.SelectedTime.text=timePicker.hour.toString() + " : " + timePicker.minute.toString()
            }

            calendar= Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.hour)
            calendar.set(Calendar.MINUTE,timePicker.minute)
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)

        }
    }

    fun SetAlarm(){
        alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(this,BroadcastReceiver::class.java)
        pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_HALF_DAY,pendingIntent)
        Toast.makeText(this,"Alarm Set Successfully",Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm() {
        pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        if (alarmManager==null){
            alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager.cancel(pendingIntent)
    }

}
