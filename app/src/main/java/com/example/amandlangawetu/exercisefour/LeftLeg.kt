package com.example.amandlangawetu.multithreading

import com.example.amandlangawetu.exercisefour.MainActivity
import java.util.concurrent.atomic.AtomicBoolean

class LeftLeg(val main: MainActivity) : Thread() {

    private val isPaused = AtomicBoolean(false)
    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (isPaused.get()) {
                    synchronized(isPaused) { isPaused.wait() }
                } else {
                    main.makeStep(true)
                }

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                return
            }
        }
    }

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