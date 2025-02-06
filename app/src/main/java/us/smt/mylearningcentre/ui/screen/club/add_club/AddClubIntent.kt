package us.smt.mylearningcentre.ui.screen.club.add_club

import us.smt.mylearningcentre.data.model.ClubCategoryData

sealed interface AddClubIntent {
    data object Save : AddClubIntent
    data object Back : AddClubIntent
    data object OpenCreateNewClubCategoryDialog : AddClubIntent
    data object CloseCreateNewClubCategoryDialog : AddClubIntent
    data class CreateNewClubCategory(val name: String) : AddClubIntent
    data class ChangeClubName(val name: String) : AddClubIntent
    data class ChangeClubCategory(val category: ClubCategoryData) : AddClubIntent
    data class ChangeDescription(val description: String) : AddClubIntent
}