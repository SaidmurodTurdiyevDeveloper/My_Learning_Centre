package us.smt.mylearningcentre.ui.screen.home

import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.TaskData

sealed interface HomeIntent {
    data object Init : HomeIntent
    data object OpenAddTaskScreen : HomeIntent
    data class AcceptApplication(val data: ApplicationFormData) : HomeIntent
    data class DeclineApplication(val data: ApplicationFormData) : HomeIntent
    data class AcceptTask(val data: TaskData) : HomeIntent
    data class DeclineTask(val data: TaskData) : HomeIntent
    data class CompleteTask(val data: TaskData) : HomeIntent
    data class DeleteTask(val data: TaskData) : HomeIntent
}