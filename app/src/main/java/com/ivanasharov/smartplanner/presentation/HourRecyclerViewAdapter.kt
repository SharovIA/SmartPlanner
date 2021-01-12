package com.ivanasharov.smartplanner.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.databinding.HourItemBinding
import com.ivanasharov.smartplanner.databinding.MinuteItemBinding
import com.ivanasharov.smartplanner.presentation.model.Minute

class HourRecyclerViewAdapter(
    private val mListMinutes: List<Int>
) : RecyclerView.Adapter<HourRecyclerViewAdapter.MinuteViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinuteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MinuteItemBinding.inflate(inflater, parent, false)
      //  val binding = DataBindingUtil.inflate(inflater, R.layout.minute_item, parent, false)
        return MinuteViewHolder(binding)
    }

    override fun getItemCount(): Int = mListMinutes.size

    override fun onBindViewHolder(holder: MinuteViewHolder, position: Int) {
        holder.binding.color = mListMinutes[position]
    }

    inner class MinuteViewHolder(
        val binding : MinuteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
/*class HourRecyclerViewAdapter(
    private var tasksList : ArrayList<TaskUI>,
    private val context: Context,
    val listener: (Int) -> Unit
) : RecyclerView.Adapter<HourRecyclerViewAdapter.TaskViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.title?.text = tasksList[position].name.value
            if(tasksList[position].status.value != null && tasksList[position].status.value == true)
            holder.status?.isChecked = true

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

        }
  }

    fun addTasksArrayList(tasksList: ArrayList<TaskUI>){
        this.tasksList = tasksList
        notifyDataSetChanged()
    }


    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var title : TextView? = null
        var status : CheckBox? = null

        init {
            title = itemView.findViewById(R.id.titleTextViewItem)
            status = itemView.findViewById(R.id.currentDayCheckBoxItem)
        }

    }*/
}