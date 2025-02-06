package us.smt.mylearningcentre.ui.screen.chat

sealed interface ChatIntent {
    data class ChangeText(val text: String) : ChatIntent
    data class SendMessage(val message: String) : ChatIntent
    data object Back : ChatIntent
}