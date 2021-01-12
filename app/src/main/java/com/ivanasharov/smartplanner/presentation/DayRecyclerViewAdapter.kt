package com.ivanasharov.smartplanner.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.HourItemBinding
import com.ivanasharov.smartplanner.databinding.MinuteItemBinding
import com.ivanasharov.smartplanner.presentation.model.Hour
import com.ivanasharov.smartplanner.presentation.view.adapters.HourDiffCallback
import kotlinx.android.synthetic.main.hour_item.view.*

class DayRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Hour> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HourItemBinding.inflate(inflater, parent, false)
        return HourViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HourViewHolder -> {
                holder.bind(items[position])
            }
        }


        if (items[position].colorMinutes != null){
            val hourAdapter = HourRecyclerViewAdapter(items[position].colorMinutes as List<Int>)
            holder.itemView.hourRecyclerView.adapter = hourAdapter
        } else {
            holder.itemView.hourRecyclerView.adapter = null
        }
    }

    fun submitList(hourList: List<Hour>){
        val oldList = items
        val diffResult = DiffUtil.calculateDiff(HourDiffCallback(oldList, hourList))
        items  = hourList
        diffResult.dispatchUpdatesTo(this)
    }

 /*   class HourDiffCallback(
        var oldHourList: List<Hour>,
        var newHourList: List<Hour>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldHourList[oldItemPosition].time == newHourList[newItemPosition].time
        }

        override fun getOldListSize(): Int = oldHourList.size

        override fun getNewListSize(): Int = newHourList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldHourList[oldItemPosition].color == newHourList[newItemPosition].color && oldHourList[oldItemPosition].colorMinutes == newHourList[newItemPosition].colorMinutes
        }

    }*/

    class HourViewHolder constructor(val binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(hour: Hour) {
            binding.hour = hour
        }
    }

}
