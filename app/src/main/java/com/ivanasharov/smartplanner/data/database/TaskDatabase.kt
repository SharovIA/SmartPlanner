package com.ivanasharov.smartplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ivanasharov.smartplanner.data.converters.GregorianCalendarConverter
import com.ivanasharov.smartplanner.data.database.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.entity.Task

@Database(entities = arrayOf(Task::class), version = 2, exportSchema = false)
@TypeConverters(GregorianCalendarConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTaskDao() : TaskDao
}