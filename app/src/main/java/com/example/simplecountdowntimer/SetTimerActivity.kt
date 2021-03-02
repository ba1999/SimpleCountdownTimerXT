package com.example.simplecountdowntimer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import splitties.toast.longToast

class SetTimerActivity : AppCompatActivity() {

    private val editTermin : EditText by lazy { findViewById(R.id.edit_termin) }
    private val btnCancle : Button by lazy { findViewById(R.id.buttonCancel) }
    private val btnOK : Button by lazy { findViewById(R.id.buttonOKID) }
    private val timePicker : TimePicker by lazy { findViewById(R.id.timepicker) }
    private val datePicker : DatePicker by lazy { findViewById(R.id.datepicker) }
    private val TAG = "SetTimerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_timer)

        timePicker.setIs24HourView(true)


        //onClickListener für SetButton
        //Daten aus Layoutelementen werden eingelesen
        //Aktivität wird beendet und result OK zurückgesendet
        btnOK.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val hour = timePicker.hour
            val minute = timePicker.minute
            val termin = editTermin.text.toString()

            val intent = Intent()
            intent.putExtra("Jahr", year)
            intent.putExtra("Monat", month)
            intent.putExtra( "Tag", day)
            intent.putExtra("Stunde", hour)
            intent.putExtra("Minute", minute)
            intent.putExtra("Termin", termin)
            setResult(Activity.RESULT_OK, intent)
            finish()

            longToast(hour.toString() + ":" + minute.toString() )
        }

        //onClickListener für Cancel Button
        //Aktivität wird mit result Canceled beendet
        btnCancle.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

    }
}