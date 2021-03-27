package com.joshua.queuenumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {
    private val _number = MutableLiveData<Int>().also { it.value = 0 }
    val number: LiveData<Int> = _number

    private val _queues = MutableLiveData<MutableList<Task>>().also { it.value = mutableListOf() }
    val queues: LiveData<MutableList<Task>> = _queues

    private val _counterList = MutableLiveData<MutableList<CounterItem>>()
    val counterList: LiveData<MutableList<CounterItem>> = _counterList

    fun initList(counters: MutableList<String>) {
        counters.apply { sort() }.let {
            it.mapIndexed { id, name ->
                CounterItem(id, name)
            }.let { items -> _counterList.value = items.toMutableList() }
        }
    }

    fun addTasks() {
        _number.value?.plus(1)?.let { taskNumber ->
            _number.value = taskNumber
            _queues.value?.let { queues ->
                queues.add(Task(TaskItem(taskNumber)))
                _queues.value =queues
            }
        }
    }

    fun assignTask() {
        viewModelScope.launch {
            _counterList.value?.filter {
                it.processing is EventState.Idle
            }?.takeIf { it.isNotEmpty() }?.get(0)?.let { counter ->
                _queues.value?.let { queues ->
                    queues.firstOrNull{ !it.taskHandled }?.let {
                        it.doTaskIfNotHandled()
                        _queues.value = queues
                        doTask(counter, it)
                    }
                }
            }
        }
    }

    private suspend fun doTask(counter: CounterItem, task: Task) {
        flow {
            counter.processing = EventState.Processing(task.content)
            _counterList.value?.let { counters ->
                counters[counters.indexOf(counter)] = counter
                _counterList.postValue(counters)
            }
            delay(task.content.handledTime)
            emit(task)

        }.flowOn(Dispatchers.IO).map {
            counter.processing = EventState.Idle()
            counter.processed.add(task.content.number)
            _counterList.value?.let { counters ->
                counters[counters.indexOf(counter)] = counter
                _counterList.postValue(counters)
            }
            it
        }.collect {
            if (task.taskHandled) {
                _queues.value?.let { queues ->
                    queues.filter {
                        it.content.number == task.content.number
                    }.forEach { queues.remove(it) }
                    _queues.postValue(queues)
                }
            }
            if(_queues.value?.isNotEmpty() == true) assignTask()
        }
    }


}