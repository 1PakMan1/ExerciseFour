package com.example.amandlangawetu.multithreading

import com.example.amandlangawetu.exercisefour.WalkingViewInterface
import java.util.concurrent.atomic.AtomicBoolean

class RightLeg(val view: WalkingViewInterface) : Thread() {

    private val isPaused = AtomicBoolean(false)
    private val isLeft = false

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (isPaused.get()) {
                    synchronized(isPaused) { isPaused.wait() }
                } else {
                    view.makeStep(isLeft)
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