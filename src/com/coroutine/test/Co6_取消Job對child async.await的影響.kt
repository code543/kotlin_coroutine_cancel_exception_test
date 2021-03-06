package com.coroutine.test

import kotlinx.coroutines.*

fun main() {

    val handler = CoroutineExceptionHandler { _, exception ->
        println("====>Caught unprocess exception: $exception")
        throw exception
    }

    val job1 = GlobalScope.launch(ctx4) {

        log("start launch")

        val job2 = launch(ctx1) {
            log("do launch2 job")
            delay(2000)
            log("do launch2 job end")

        }

        job2.invokeOnCompletion {
            log("job2 complete $it is active: ${job2.isActive}, is isCompleted: ${job2.isCompleted}, is isCancelled: ${job2.isCancelled}")
        }

        async(ctx2) {
            log("do async job")
            delay(3000)
            log("do async job end")
        }.await()

        log("end launch ")
    }


    job1.invokeOnCompletion {
        log("job1 complete $it is active: ${job1.isActive}, is isCompleted: ${job1.isCompleted}, is isCancelled: ${job1.isCancelled}")
    }

    GlobalScope.launch(ctx3) {
        delay(2000)
        log("job1 do cancel")
        job1.cancel()
    }

    Thread.sleep(5000)
}