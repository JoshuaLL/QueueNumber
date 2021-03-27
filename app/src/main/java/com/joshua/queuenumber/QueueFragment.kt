package com.joshua.queuenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_queue.*

class QueueFragment : Fragment() {

    private val viewModel: QueueViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_queue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setData()
    }

    private fun initUI(){
        CounterAdapter().let { counterAdapter ->
            rv_state_list.adapter = counterAdapter
            viewModel.counterList.observe(viewLifecycleOwner){
                counterAdapter.submitList(it)
                counterAdapter.notifyDataSetChanged()
            }
        }

        viewModel.number.observe(viewLifecycleOwner){
            btn_next.text = getString(R.string.next, it)
        }

        viewModel.queues.observe(viewLifecycleOwner){tasks->
            tasks.takeIf { it.size > 0 }?.let {
                it.filter { !it.taskHandled }.takeIf { it.isNotEmpty()}?.let {
                    viewModel.assignTask()
                }
                tv_waiting.text = getString(R.string.waiting, tasks.size)
                btn_clear.isEnabled =false
            } ?: run {
                tv_waiting.text = getString(R.string.waiting, 0)
                btn_clear.isEnabled =true
            }
        }

        btn_next.setOnClickListener {
            viewModel.addTasks()
        }

        btn_clear.setOnClickListener {
            viewModel.reset()
            setData()
        }

    }

    private fun setData(){
        viewModel.initList(resources.getStringArray(R.array.counters).toMutableList())
    }
}