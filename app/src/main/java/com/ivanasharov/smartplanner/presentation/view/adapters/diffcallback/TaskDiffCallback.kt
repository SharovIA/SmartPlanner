package com.ivanasharov.smartplanner.presentation.view.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus

class TaskDiffCallback: DiffUtil.ItemCallback<IdNameStatus>() {
    override fun areItemsTheSame(oldItem: IdNameStatus, newItem: IdNameStatus): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: IdNameStatus, newItem: IdNameStatus): Boolean {
        return oldItem.name == newItem.name && oldItem.status == newItem.status
    }

}
