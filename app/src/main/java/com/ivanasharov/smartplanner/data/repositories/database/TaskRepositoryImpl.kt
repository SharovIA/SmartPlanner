package com.ivanasharov.smartplanner.data.repositories.database

import com.ivanasharov.smartplanner.data.converters.ConvertTaskDataToTaskDomain
import com.ivanasharov.smartplanner.data.converters.ConvertTaskDomainToTaskData
import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import com.ivanasharov.smartplanner.data.database.requests_model.NameTimeImportance
import com.ivanasharov.smartplanner.data.database.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.entity.Task
import com.ivanasharov.smartplanner.domain.model.TaskDomain
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val mTaskDao : TaskDao
) : TaskRepository {

    override fun getListCurrentTasks(date: GregorianCalendar): Flow<List<IdNameStatus>> = mTaskDao.getByDate(date)

    override fun getListCurrentTasksForSchedule(date: GregorianCalendar): Flow<List<NameTimeImportance>> = mTaskDao.getByDateForSchedule(date)


    override fun save(taskDomain: TaskDomain): Long? {

        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        return mTaskDao.insert(task)
    }

    override fun changeStatusTask(taskDomain: TaskDomain) {
        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        mTaskDao.updateTask(task.name, task.timeFrom, task.status)
    }

    override fun updateTask(taskDomain: TaskDomain) {
        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        mTaskDao.update(task)
    }

    override fun changeTaskStatus(idNameStatus: IdNameStatus) {
        mTaskDao.update(idNameStatus.id, idNameStatus.status)
    }

    override fun getTaskById(id: Long): Flow<TaskDomain> = mTaskDao.getById(id).map{
        ConvertTaskDataToTaskDomain()
            .convert(it)
    }
}