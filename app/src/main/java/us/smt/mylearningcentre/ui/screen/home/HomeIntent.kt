package us.smt.mylearningcentre.ui.screen.home

sealed interface HomeIntent {
    data object Init : HomeIntent
    data object OpenAddTaskScreen : HomeIntent
}