package com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.databinding.MinuteItemBinding

class HourRecyclerViewAdapter(
    private val mListMinutes: List<Int>
) : RecyclerView.Adapter<HourRecyclerViewAdapter.MinuteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinuteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MinuteItemBinding.inflate(inflater, parent, false)
        return MinuteViewHolder(binding)
    }

    override fun getItemCount(): Int = mListMinutes.size

    override fun onBindViewHolder(holder: MinuteViewHolder, position: Int) {
        holder.binding.color = mListMinutes[position]
    }

    inner class MinuteViewHolder(
        val binding : MinuteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {}

}