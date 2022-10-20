package com.example.madpractical7_20012021007

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import java.util.*

class MainActivity : AppCompatActivity() {
    var mili : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createAlarmBtn = findViewById<Button>(R.id.btn_create_alarm)
        val cancelAlarmBtn = findViewById<Button>(R.id.btn_cancel_alarm)
        val cancelAlarmCardView = findViewById<MaterialCardView>(R.id.mvc2)
        val textClock = findViewById<TextClock>(R.id.textclock)
        val textView = findViewById<TextView>(R.id.text5)

        textClock.format12Hour = "hh:mm:ss a MMM, dd yyyy"

        cancelAlarmCardView.visibility = View.GONE

        createAlarmBtn.setOnClickListener {
            var cal : Calendar = Calendar.getInstance()
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            var min = cal.get(Calendar.MINUTE)
            var tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, h, m ->
                mili = getMillis(h, m)
                setAlarm(getMillis(h, m), "Start")
                cancelAlarmCardView.visibility = View.VISIBLE
                textView.text = h.toString()+":"+m.toString()
            }, hour, min, false)
            tpd.show()
        }

        cancelAlarmBtn.setOnClickListener {
            setAlarm(mili,"Stop")
            cancelAlarmCardView.visibility = View.GONE
        }
    }

    private fun setAlarm(millisTime : Long, str : String){
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service_1",str)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 234324243, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(str == "Start"){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
        }
        else if(str == "Stop"){
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
        }
    }

    private fun getMillis(hour : Int, min : Int) : Long{
        val setcal = Calendar.getInstance()
        setcal[Calendar.HOUR_OF_DAY] = hour
        setcal[Calendar.MINUTE] = min
        setcal[Calendar.SECOND] = 0
        return setcal.timeInMillis
    }
}