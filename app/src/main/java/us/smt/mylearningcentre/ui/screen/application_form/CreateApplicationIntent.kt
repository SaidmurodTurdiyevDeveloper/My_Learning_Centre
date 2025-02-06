package us.smt.mylearningcentre.ui.screen.application_form

sealed interface CreateApplicationIntent {
    data class Save(val clubId: String) : CreateApplicationIntent
    data object Back : CreateApplicationIntent
    data class ChangeDescription(val description: String) : CreateApplicationIntent
}