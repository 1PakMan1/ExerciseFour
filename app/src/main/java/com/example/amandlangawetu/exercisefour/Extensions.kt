package com.example.amandlangawetu.multithreading

fun Any.notify() {
    (this as java.lang.Object).notify()
}

fun Any.notifyAll() {
    (this as java.lang.Object).notifyAll()
}

fun Any.wait() {
    (this as java.lang.Object).wait()
}