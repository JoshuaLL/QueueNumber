package com.joshua.queuenumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_counter_state.view.*

class CounterAdapter : ListAdapter<CounterItem, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_counter_state, parent, false)
        return CounterViewHolder(mView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CounterViewHolder
        getItem(position)?.apply {
            holder.counterName.text = name
            holder.processingState.text = processing.let {
                when(it){
                    is EventState.Idle -> it.state
                    is EventState.Processing -> it.taskItem.number.toString()
                }
            }
            holder.processed.text = processed.joinToString()
        }
    }

    class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var counterName: TextView = itemView.tv_name
        var processingState: TextView = itemView.tv_processing_state
        var processed: TextView = itemView.tv_processed_number
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<CounterItem>() {
    override fun areItemsTheSame(oldItem: CounterItem, newItem: CounterItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: CounterItem, newItem: CounterItem): Boolean {
        return oldItem == newItem
    }
}
