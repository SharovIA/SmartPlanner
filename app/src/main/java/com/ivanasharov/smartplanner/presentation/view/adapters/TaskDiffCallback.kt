package com.ivanasharov.smartplanner.presentation.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel


class TaskDiffCallback: DiffUtil.ItemCallback<TaskViewModel>() {
    override fun areItemsTheSame(oldItem: TaskViewModel, newItem: TaskViewModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TaskViewModel, newItem: TaskViewModel): Boolean {
        return oldItem.description == newItem.description && oldItem.date == newItem.date &&
                oldItem.timeFrom == newItem.timeFrom && oldItem.timeTo == newItem.timeTo &&
                oldItem.importance == newItem.importance && oldItem.address == newItem.address &&
                oldItem.contact == newItem.contact && oldItem.status == newItem.status
    }

}
