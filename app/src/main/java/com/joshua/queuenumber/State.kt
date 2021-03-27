package com.joshua.queuenumber.vo

data class CounterItem(
                 val id:Int,
                 val name:String,
                 var processing:EventState =EventState.Idle(),
                 var processed:MutableList<Int> = mutableListOf())


sealed class EventState {
    data class Idle(val state: String= "idle"): EventState()
    data class Processing(
        val number: String
     ): EventState()
}

class Task<out T>(private val content: T) {

    var taskHandled = false
        private set

    fun handledTime()= (500L..1500L).random()

    fun doTaskIfNotHandled(): T? {
        return if (taskHandled) {
            null
        } else {
            taskHandled = true
            content
        }
    }
}


