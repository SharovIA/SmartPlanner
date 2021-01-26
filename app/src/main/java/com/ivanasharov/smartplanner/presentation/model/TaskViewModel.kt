package com.ivanasharov.smartplanner.presentation.model

import java.io.Serializable

data class TaskViewModel( val taskUILoad: TaskUILoad,
                          val name : String = taskUILoad.name,
                          val description : String? = taskUILoad.description,
                          val date  : String = taskUILoad.date,
                          val timeFrom  : String = taskUILoad.timeFrom,
                          val timeTo  : String = taskUILoad.timeTo,
                          val importance : String? = taskUILoad.importance,
                          val address : String? = taskUILoad.address,
                          val contact : String? = taskUILoad.contact,
                          val status : Boolean = taskUILoad.status,
                          val id: Long? = taskUILoad.id
): Serializable