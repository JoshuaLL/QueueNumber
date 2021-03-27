package com.joshua.queuenumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joshua.queuenumber.vo.CounterItem

class FirstViewModel : ViewModel() {

    private val _number = MutableLiveData<Int>().also { it.value =0 }
    val number:LiveData<Int> = _number

    private val _queues = MutableLiveData<Int>().also { it.value =0 }
    val queues:LiveData<Int> = _queues

    private val _taskList = MutableLiveData<MutableList<CounterItem>>()
    val taskList:LiveData<MutableList<CounterItem>> = _taskList

    fun initList(counters:MutableList<String>){
        counters.apply { sort() }.let {
            it.mapIndexed{id, CounterName ->
                CounterItem(id, CounterName)
            }.let { counterItems->
                _taskList.value = counterItems.toMutableList()
            }
        }
    }

    fun doTasks(){
        val taskNumber = number.value?.plus(1)
        _number.value = taskNumber

    }

}