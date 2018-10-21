package com.example.amandlangawetu.exercisefour

import com.example.amandlangawetu.multithreading.notify
import com.example.amandlangawetu.multithreading.wait
import java.util.concurrent.atomic.AtomicBoolean

class Leg(val view : WalkingViewInterface, val isLeft : Boolean) : Thread() {
    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                synchronized(isPaused) {
                    while (isPaused.get()) {
                        isPaused.wait()
                    }
                }
                view.makeStep(isLeft)
            } catch (e: InterruptedException) {
                //Do nothing!
            }
        }
    }

    private val isPaused = AtomicBoolean(false)

    fun pause() {
        isPaused.set(true)
    }

    fun unpause() {
        isPaused.set(false)
        synchronized(isPaused) {
            isPaused.notify()
        }
    }

    fun isPaused(): Boolean {
        return isPaused.get()
    }
}