package ru.playground.kotlin.coroutines

import java.time.OffsetDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

@OptIn(DelicateCoroutinesApi::class)
val scope = CoroutineScope(newFixedThreadPoolContext(6, "custom pool") + SupervisorJob())
fun testSharedFlow() {
    val sharedNumbersFlow = flow {
        (1..50).forEach {
            run {
                delay(100)
                emit(it)
            }
        }
    }.shareIn(scope, SharingStarted.Eagerly, 3)

    Thread.sleep(1000)
    scope.launch {
        sharedNumbersFlow.collect { println("flow 1: $it ${OffsetDateTime.now()}") }
    }
    Thread.sleep(3000)

    scope.launch {

        sharedNumbersFlow.collect { println("flow 2: $it ${OffsetDateTime.now()}") }
    }
    Thread.sleep(2000)
}

fun testSharedState() {
    val sharedNumbersFlow = flow {
        (1..5).forEach {
            run {
                delay(100)
                emit(it)
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, 0)

    Thread.sleep(1000)
    scope.launch {
        sharedNumbersFlow.collect { println("flow 1: $it ${OffsetDateTime.now()}") }
    }
    Thread.sleep(3000)

    scope.launch {
        sharedNumbersFlow.collect { println("flow 2: $it ${OffsetDateTime.now()}") }
    }
    Thread.sleep(2000)
}

fun main(args: Array<String>) {
//    testSharedFlow()
    testSharedState()

}