package com.example.amandlangawetu.exercisefour

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.amandlangawetu.multithreading.LeftLeg
import com.example.amandlangawetu.multithreading.RightLeg
import com.example.amandlangawetu.multithreading.notifyAll
import com.example.amandlangawetu.multithreading.wait
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        plus_speed_btn.setOnClickListener {
            speed -= delta
        }

        minus_speed_btn.setOnClickListener {
            speed += delta
        }

        start_walking_btn.setOnClickListener {
            if (!isAnyThreadAlive()) {
                go()
                return@setOnClickListener
            }

            if (isAnyThreadPaused()) {
                resume()
                return@setOnClickListener
            }

            stop()
        }
    }

    @Volatile
    private var speed = 1000L

    private val delta = 50

    private val leftLegThread = LeftLeg(this)
    private val rightLegThread = RightLeg(this)

    //First step is left = true
    private var isLeft = true

    @Synchronized
    fun makeStep(step : Boolean) {
        if (step == isLeft) {
            runOnUiThread(updateViewTask(isLeft))
            Thread.sleep(speed)
            isLeft = !step
            notifyAll()
        }
        wait()
    }

    private fun go() {
        leftLegThread.start()
        rightLegThread.start()
        start_walking_btn.text = getString(R.string.stop_walking)
    }

    private fun stop() {
        leftLegThread.pause()
        rightLegThread.pause()
        start_walking_btn.text = getString(R.string.start_walking)
    }

    private fun resume() {
        leftLegThread.unpause()
        rightLegThread.unpause()
        start_walking_btn.text = getString(R.string.stop_walking)
    }

    private fun updateViewTask(isLeft: Boolean): Runnable {
        return Runnable {
            if (isLeft) {
                right_step.visibility = View.INVISIBLE
                left_step.visibility = View.VISIBLE
            } else if (!isLeft) {
                right_step.visibility = View.VISIBLE
                left_step.visibility = View.INVISIBLE
            }
        }
    }

    private fun isAnyThreadAlive(): Boolean {
        return leftLegThread.isAlive || rightLegThread.isAlive
    }

    private fun isAnyThreadPaused() : Boolean {
        return leftLegThread.isPaused() || rightLegThread.isPaused()
    }
}
