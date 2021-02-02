package com.example.simplecountdowntimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import splitties.toast.toast
import java.util.*

class ShowTimerActivity : AppCompatActivity() {

    private val tvTermin : TextView by lazy { findViewById(R.id.textView_termin) }
    private val tvDauer : TextView by lazy { findViewById(R.id.textView_dauer) }

    var termin = ""
    var countdown = 0
    private var secLeft : Long = 0
    private var zielzeit : Long = 0
    private val mHandler : Handler by lazy { Handler() }
    private lateinit var mRunnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_timer)

        val intent = intent
        termin = intent.getStringExtra(Constants.TIMERSTRING) ?: ""
        zielzeit = intent.getLongExtra(Constants.DAUERINT, 0)

        tvTermin.text = termin
        tvDauer.text = countdown.toString()

        secLeft = zielzeit

        countdownStart()
    }

    private fun restZeit() : Long {
        val now = Date()
        return zielzeit - now.time/1000
    }

    private fun countdownStart(){
        mRunnable = Runnable {
            mHandler.postDelayed(mRunnable, 1000)
            secLeft = restZeit()
            if(secLeft > 0){
                tvDauer.text = zeitAnzeige(secLeft)
            }
            if (secLeft <= 0){
                tvDauer.text = zeitAnzeige(secLeft)
                val player = MediaPlayer.create(applicationContext, Settings.System.DEFAULT_NOTIFICATION_URI)
                player.start()
            }
        }
        mHandler.postDelayed(mRunnable, 1000)
    }

    private fun zeitAnzeige(sLeft: Long) : String {
        var diff : Long = 0
        var zeit = getString(R.string.time_over)

        if(sLeft >= 0){
            diff = sLeft
            val d = diff / (24 * 60 * 60)
            diff = diff - (d * (24 * 60 * 60))
            val h = diff / (60*60)
            diff = diff - h * (60*60)
            val m = diff / 60
            diff = diff - m * 60
            val s = diff
            zeit = "noch $d Tage und \n"
            zeit = if (h < 10) zeit + "0" + h + ":" else "$zeit$h:"
            zeit = if (m < 10) zeit + "0" + m + ":" else "$zeit$m:"
            zeit = if (s < 10) zeit + "0" + s + ""  else "$zeit$s"
        }
        return zeit
    }

}