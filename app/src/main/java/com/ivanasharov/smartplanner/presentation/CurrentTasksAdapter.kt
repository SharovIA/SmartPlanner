package com.ivanasharov.smartplanner.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.Model.TaskUI

class CurrentTasksAdapter(
    private var tasksList : ArrayList<TaskUI>
) : RecyclerView.Adapter<CurrentTasksAdapter.TaskViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentTasksAdapter.TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(tasksList != null)
            return tasksList.size
        else return 0
    }

    override fun onBindViewHolder(holder: CurrentTasksAdapter.TaskViewHolder, position: Int) {
        if (tasksList != null) {
            holder.title?.text = tasksList[position].name.value
            if(tasksList[position].status.value != null && tasksList[position].status.value == true)
            holder.status?.isChecked = true
        }
    }

    fun addTasksArrayList(tasksList: ArrayList<TaskUI>){
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var title : TextView? = null
        var status : CheckBox? = null

        init {
            title = itemView.findViewById(R.id.titleTextViewItem)
            status = itemView.findViewById(R.id.currentDayCheckBoxItem)
        }
    }
}