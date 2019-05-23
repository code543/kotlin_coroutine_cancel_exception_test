package com.coroutine.test

import jdk.nashorn.internal.runtime.arrays.ArrayIndex
import kotlinx.coroutines.*

fun log(msg:String){
    println("[${Thread.currentThread().name}:${Thread.currentThread().id}] $msg")
}

val ctx1 = newSingleThreadContext("CTX1")
val ctx2 = newSingleThreadContext("CTX2")
val ctx3 = newSingleThreadContext("CTX3")
val ctx4 = newSingleThreadContext("CTX4")
val ctx5 = newSingleThreadContext("CTX5")

fun main() = runBlocking{

    val job1 = GlobalScope.launch {

        log("start launch")

        launch {
            log("do launch2 job")
            delay(2000)
            log("do launch2 job end")
            throw RuntimeException("do launch job exception")
        }

        try {
            val result = withContext(Dispatchers.Default) {
                log("do with context")
                delay(2000)
                log("do with context done")
                //throw RuntimeException("withcontext exception")
                "WITHCONTEXT1"
            }
            log("end context ${result}")
        }
        catch (e : Error){
            log("catch with context ex $e")
        }

        GlobalScope.launch {
            log("do launch job")
            delay(2000)
            log("do launch job end")
            throw RuntimeException("do launch job exception")
        }

        //throw Error("do job1 exception")

        log("end launch ")
    }


    job1.invokeOnCompletion {
        log("job1 complete $it")
    }

    Thread.sleep(1000)
    log("job1.cancel")
    job1.cancelAndJoin()
    log("job1.cancel done")

    Thread.sleep(5000)

}