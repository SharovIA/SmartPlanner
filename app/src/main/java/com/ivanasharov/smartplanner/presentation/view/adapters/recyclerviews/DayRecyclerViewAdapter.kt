package com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.databinding.HourItemBinding
import com.ivanasharov.smartplanner.presentation.model.Hour
import com.ivanasharov.smartplanner.presentation.view.adapters.diffcallback.HourDiffCallback
import kotlinx.android.synthetic.main.hour_item.view.*

class DayRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Hour> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HourItemBinding.inflate(inflater, parent, false)
        return HourViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HourViewHolder -> {
                holder.bind(items[position])
            }
        }

        if (items[position].colorMinutes != null){
            val hourAdapter =
                HourRecyclerViewAdapter(
                    items[position].colorMinutes as List<Int>
                )
            holder.itemView.hourRecyclerView.adapter = hourAdapter
        } else {
            holder.itemView.hourRecyclerView.adapter = null
        }
    }

    fun submitList(hourList: List<Hour>){
        val oldList = items
        val diffResult = DiffUtil.calculateDiff(
            HourDiffCallback(
                oldList,
                hourList
            )
        )
        items  = hourList
        diffResult.dispatchUpdatesTo(this)
    }

    class HourViewHolder constructor(val binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(hour: Hour) {
            binding.hour = hour
        }
    }

}
