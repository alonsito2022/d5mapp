package com.example.d5mandroidapp.utils

import android.os.CountDownTimer
import java.util.Date

class TimerUtils {

    private var timer: CountDownTimer? = null

    fun startTimer(expirationDate: Date, onTick: (Long) -> Unit, onFinish: () -> Unit) {
        timer = object : CountDownTimer(expirationDate.time - System.currentTimeMillis(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }
        timer?.start()
    }

    fun stopTimer() {
        timer?.cancel()
    }

}