package com.ivanasharov.smartplanner.data.database.dao

import androidx.room.*
import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import com.ivanasharov.smartplanner.data.database.requests_model.NameTimeImportance
import com.ivanasharov.smartplanner.data.database.entity.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT id, name, status FROM task WHERE date = :calendar")
    fun getByDate(calendar: GregorianCalendar): Flow<List<IdNameStatus>>

    @Query("SELECT name, timeFrom, timeTo, importance FROM task WHERE date = :calendar")
    fun getByDateForSchedule(calendar: GregorianCalendar): Flow<List<NameTimeImportance>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id : Long): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Long?

    @Query("UPDATE task SET status = :status WHERE (name = :name AND timeFrom = :timeFrom)")
    fun updateTask(name: String?, timeFrom: GregorianCalendar?, status : Boolean): Int

    @Query("UPDATE task SET status=:status WHERE id = :id")
    fun update(id: Long, status: Boolean)

    @Update()
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}