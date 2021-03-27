package com.joshua.queuenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment() {

    private val viewModel: FirstViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.initList(resources.getStringArray(R.array.counters).toMutableList())
    }

    private fun initUI(){
        CounterAdapter().let { counterAdapter ->
            rv_state_list.adapter = counterAdapter
            viewModel.taskList.observe(viewLifecycleOwner){
                counterAdapter.submitList(it)
            }
        }

        viewModel.number.observe(viewLifecycleOwner){
            btn_next.text = getString(R.string.next, it)
        }

        viewModel.queues.observe(viewLifecycleOwner){
            tv_waiting.text = getString(R.string.waiting, it)
        }

        btn_next.setOnClickListener {
            viewModel.doTasks()
        }

    }
}