package us.smt.mylearningcentre.ui.screen.home

import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.data.model.TaskData
import us.smt.mylearningcentre.util.UserError

data class HomeState(
    val isLoading: Boolean = false,
    val tasks: List<TaskData> = emptyList(),
    val appForm: List<ApplicationFormData> = emptyList(),
    val user: StudentData? = null,
    val error: UserError? = null,
)
