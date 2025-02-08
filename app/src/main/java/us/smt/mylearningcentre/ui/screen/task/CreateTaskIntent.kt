package us.smt.mylearningcentre.ui.screen.task

sealed interface CreateTaskIntent {
    data object Back : CreateTaskIntent
    data object CreateTask : CreateTaskIntent
    data class TitleChanged(val value: String) : CreateTaskIntent
    data class DescriptionChanged(val value: String) : CreateTaskIntent
}