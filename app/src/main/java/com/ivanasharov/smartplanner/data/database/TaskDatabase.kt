package com.ivanasharov.smartplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ivanasharov.smartplanner.data.GregorianCalendarConverter
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.entity.Task

@Database(entities = arrayOf(Task::class), version = 2, exportSchema = false)
@TypeConverters(GregorianCalendarConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTeskDao() : TaskDao
}