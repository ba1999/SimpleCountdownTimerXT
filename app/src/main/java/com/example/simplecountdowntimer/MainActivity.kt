package com.example.simplecountdowntimer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import splitties.toast.longToast
import java.util.*

class MainActivity : AppCompatActivity() {

    private val btnShowTimer : Button by lazy { findViewById(R.id.button_showTimer) }
    private val btnSetTimer : Button by lazy { findViewById(R.id.button_setTimer) }
    private val etInput_Termin : EditText by lazy { findViewById(R.id.edit_inputTermin) }
    private val txtZeitAnzeige : TextView by lazy { findViewById(R.id.text_zeitAnzeige) }
    private val TAG = "MainActivity"

    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var zielzeit : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShowTimer.setOnClickListener {
            val intent = Intent(this, ShowTimerActivity::class.java)
            val termin = etInput_Termin.text.toString()
            intent.putExtra(Constants.TIMERSTRING, termin)
            intent.putExtra(Constants.DAUERINT, zielzeit)
            startActivity(intent)
            Log.i(TAG, "Button gedrückt")
        }

        btnSetTimer.setOnClickListener {
            val intent = Intent(this, SetTimerActivity::class.java)
            startActivityForResult(intent, Constants.SETTIMERREQUESTCODE)
            Log.i(TAG, "SetTimerButton wurde gedrückt")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == Constants.SETTIMERREQUESTCODE) && (resultCode == Activity.RESULT_OK)){
            //Ausrufezeichen für Null-Safety
            year = data!!.getIntExtra("Jahr", 2000)
            month = data!!.getIntExtra("Monat", 2)
            day = data!!.getIntExtra("Tag", 2)
            hour = data!!.getIntExtra("Stunde", 12)
            minute = data!!.getIntExtra("Minute", 0)

            //Erstellen einer Calendar-Variable mit der übergebenen Zielzeit
            //Achtung: Das Datum entspricht dem heutigem Datum
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            //absolute Zielzeit in Sekunden seit dem 1.1.1970 bis heute + Uhrzeit
            zielzeit = calendar.timeInMillis/1000

            txtZeitAnzeige.setText("$day.${month+1}.$year $hour:$minute Uhr")

            var zeit = if (hour < 10) "0$hour" else hour
            zeit = "" + zeit + ":" + if(minute < 10) "0$minute" else minute
            txtZeitAnzeige.text = zeit

            longToast("" + hour + ":" + minute)
        }
    }
}