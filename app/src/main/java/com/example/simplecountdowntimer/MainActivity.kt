package com.example.simplecountdowntimer

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import splitties.alertdialog.alertDialog
import splitties.alertdialog.negativeButton
import splitties.alertdialog.okButton
import splitties.alertdialog.positiveButton
import splitties.toast.longToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val btnShowTimer : Button by lazy { findViewById(R.id.button_showTimer) }
    private val btnSetTimer : Button by lazy { findViewById(R.id.button_setTimer) }
    private val lvTermine : ListView by lazy { findViewById(R.id.list_termine) }
    private val TAG = "MainActivity"

    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var zielzeit : Long = 0

    private var list = ArrayList <String> ()
    private lateinit var adapter : ArrayAdapter <String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list)
        lvTermine.adapter = adapter

        btnShowTimer.setOnClickListener {

            val termineintrag = btnShowTimer.text.toString()

            if(termineintrag == getString(R.string.showTimerButton)){
                alertDialog(
                        title = getString(R.string.hint),
                        message = getString(R.string.select_one)){
                    okButton()
                }.show()
                return@setOnClickListener
            }

            val intent = Intent(this, ShowTimerActivity::class.java)
            val parts = termineintrag.split("\n")
            val termin = parts[0]
            val formatter = SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN)
            val date : Date = formatter.parse(parts[1])


            intent.putExtra(Constants.TIMERSTRING, termin)
            intent.putExtra(Constants.DAUERINT, date.time/1000)
            startActivity(intent)
            Log.i(TAG, "Button gedrückt")
        }

        btnSetTimer.setOnClickListener {
            val intent = Intent(this, SetTimerActivity::class.java)
            startActivityForResult(intent, Constants.SETTIMERREQUESTCODE)
            Log.i(TAG, "SetTimerButton wurde gedrückt")
        }

        lvTermine.setOnItemClickListener { adapterView, view, i, l ->

            alertDialog(
                    title = getString(R.string.alert_title),
                    message = lvTermine.getItemAtPosition(i).toString()) {
                positiveButton(R.string.choose_date) {
                    btnShowTimer.text = lvTermine.getItemAtPosition(i).toString()
                }
                negativeButton(R.string.delete_date){
                    list.removeAt(i)
                    adapter.notifyDataSetChanged()
                }
            }.show()
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
            var termin = data?.getStringExtra("Termin") ?: ""

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

            val formatter = SimpleDateFormat("dd.MM.YY HH:mm")
            val listeneintrag = formatter.format(calendar.time)

            list.add("$termin\n$listeneintrag")
            adapter.notifyDataSetChanged()


            //Speicher den neuen Termin in den Shared Preferences
            val sp = getPreferences(Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putInt("AnzahlTermine", list.size)
            val i = list.size - 1
            edit.putString("Termin_$i", list.get(i))
            edit.apply()

        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")

        // speicher die Terminliste
        val sp = getPreferences(Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putInt("AnzahlTermine", list.size)
        for(i in 0 until list.size){
            edit.putString("Termin_$i", list.get(i))
        }
        edit.commit()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")

        // Termine wieder einlesen
        val sp = getPreferences(Context.MODE_PRIVATE)
        val anzahl = sp.getInt("AnzahlTermine", 0)
        for(i in 0 until anzahl){
            val t = sp.getString("Termin_$i", "").toString()
            if(!list.contains(t)){
                list.add(t)
            }
        }
    }

}