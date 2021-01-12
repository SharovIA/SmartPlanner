package com.ivanasharov.smartplanner.presentation.view.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ivanasharov.smartplanner.domain.NameDurationAndImportanceTask
import com.ivanasharov.smartplanner.presentation.model.Hour
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel

class HourDiffCallback(
    var oldHourList: List<Hour>,
    var newHourList: List<Hour>
): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHourList[oldItemPosition].time == newHourList[newItemPosition].time
    }

    override fun getOldListSize(): Int = oldHourList.size

    override fun getNewListSize(): Int = newHourList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHourList[oldItemPosition].color == newHourList[newItemPosition].color &&
                oldHourList[oldItemPosition].colorMinutes == newHourList[newItemPosition].colorMinutes
    }

}