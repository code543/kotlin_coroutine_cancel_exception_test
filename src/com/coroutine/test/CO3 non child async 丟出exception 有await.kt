package com.coroutine.test

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun main() {

    val handler = CoroutineExceptionHandler { _, exception ->
        println("====>Caught unprocess exception: $exception")
        throw exception
    }


    val job1 = GlobalScope.launch(Dispatchers.Default + handler) {

        log("start launch")
        val job2 = launch(Dispatchers.IO) {
            log("do launch2 job")
            delay(2000)
            log("do launch2 job end")

        }

        job2.invokeOnCompletion {
            log("job2 complete $it is active: ${job2.isActive}, is isCompleted: ${job2.isCompleted}, is isCancelled: ${job2.isCancelled}")
        }

        //await時, exception 會當成結果返回, 造成此launch exception
        GlobalScope.async(Dispatchers.IO) {
            log("do async job")
            delay(1000)
            throw RuntimeException("do async job exception")
            log("do async job end")
        }.await()

        log("end launch ")
    }


    job1.invokeOnCompletion {
        log("job1 complete $it is active: ${job1.isActive}, is isCompleted: ${job1.isCompleted}, is isCancelled: ${job1.isCancelled}")
    }


    log("start sleep")
    Thread.sleep(10000)
    log("end sleep")

}