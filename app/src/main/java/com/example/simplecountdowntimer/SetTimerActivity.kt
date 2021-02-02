package com.example.simplecountdowntimer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import splitties.toast.longToast

class SetTimerActivity : AppCompatActivity() {

    private val btnOK : Button by lazy { findViewById(R.id.buttonOKID) }
    private val timePicker : TimePicker by lazy { findViewById(R.id.timepicker) }
    private val datePicker : DatePicker by lazy { findViewById(R.id.datepicker) }
    private val TAG = "SetTimerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_timer)

        timePicker.setIs24HourView(true)

        btnOK.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val hour = timePicker.hour
            val minute = timePicker.minute
            var intent = Intent()
            intent.putExtra("Jahr", year)
            intent.putExtra("Monat", month)
            intent.putExtra( "Tag", day)
            intent.putExtra("Stunde", hour)
            intent.putExtra("Minute", minute)
            setResult(Activity.RESULT_OK, intent)
            finish()

            longToast(hour.toString() + ":" + minute.toString() )
        }

    }
}