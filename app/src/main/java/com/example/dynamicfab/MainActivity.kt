package com.example.dynamicfab

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var dX = 0f
    private var dY = 0f
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var fab : FloatingActionButton
    private lateinit var startButton : Button
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fab)
        startButton = findViewById(R.id.startButton)
        fab.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                }
            }
            true
        }

        startButton.setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30 * 60 * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60

                val timeText = String.format("%02d:%02d", minutes, seconds)
               fab.setImageBitmap(createTimerBitmap(timeText))
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "Time is up!", Toast.LENGTH_LONG).show()
               fab.setImageResource(android.R.drawable.ic_dialog_email)
            }
        }

        countDownTimer.start()
    }

    private fun createTimerBitmap(text: String): Bitmap {
        val size = resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_fab_size_normal)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintText = Paint().apply {
            color = Color.RED
            textSize = 60f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (paintText.descent() + paintText.ascent()) / 2).toInt()

        canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), paintText)

        return bitmap
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
