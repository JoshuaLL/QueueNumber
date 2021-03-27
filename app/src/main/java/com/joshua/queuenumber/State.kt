package com.joshua.queuenumber

data class CounterItem(val id:Int,
                       val name:String,
                       var processing: EventState = EventState.Idle(),
                       var processed:MutableList<Int> = mutableListOf())

sealed class EventState {
    data class Idle(val state: String= "idle"): EventState()
    data class Processing( val taskItem: TaskItem): EventState()
}

data class TaskItem(val number:Int,
                    val handledTime:Long= (500L..1500L).random()
)

data class Task(var content: TaskItem) {
    var taskHandled = false
        private set

    fun doTaskIfNotHandled(){
         if (!taskHandled) taskHandled = true
    }
}


