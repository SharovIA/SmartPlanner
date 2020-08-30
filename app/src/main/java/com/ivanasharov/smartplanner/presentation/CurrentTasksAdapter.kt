package com.ivanasharov.smartplanner.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.ShowTaskActivity
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import com.ivanasharov.smartplanner.presentation.view.CurrentDayFragment

class CurrentTasksAdapter(
    private var tasksList : ArrayList<TaskUI>,
    private val context: Context,
    val listener: (Int) -> Unit
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

        holder.itemView.setOnClickListener {
            val taskItem  = tasksList[position]
            val intent = Intent(context, ShowTaskActivity::class.java)
            addToIntent(intent, taskItem)
            context.startActivity(intent)
        }

        holder.status?.setOnClickListener{
            listener(position)
            //когда ставлю галку - попадаю сюда.
            //Как мне отсюда изменить вьюмодель? Она же не тут, а во фрагменте...
            //Log.d("test", "check: " +holder.status?.isChecked.toString())

        }
    }

    fun addTasksArrayList(tasksList: ArrayList<TaskUI>){
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    private fun addToIntent(intent: Intent, taskItem : TaskUI) {
        intent.putExtra("name", taskItem.name.value)
        intent.putExtra("description", taskItem.description.value)
        intent.putExtra("date", taskItem.date.value?.timeInMillis)
        intent.putExtra("timeFrom", taskItem.timeFrom.value)
        intent.putExtra("timeTo", taskItem.timeTo.value)
        intent.putExtra("importance", taskItem.importance.value)
        intent.putExtra("address", taskItem.address.value)
        intent.putExtra("isShowMap", taskItem.isShowMap.value)
        intent.putExtra("isSnapContact", taskItem.isSnapContact.value)
        intent.putExtra("contact", taskItem.contact.value)
        intent.putExtra("status", taskItem.status.value)
    }

    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var title : TextView? = null
        var status : CheckBox? = null

        init {
            title = itemView.findViewById(R.id.titleTextViewItem)
            status = itemView.findViewById(R.id.currentDayCheckBoxItem)
        }

    }
}