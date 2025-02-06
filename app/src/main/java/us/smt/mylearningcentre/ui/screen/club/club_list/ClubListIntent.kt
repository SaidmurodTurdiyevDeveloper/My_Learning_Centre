package us.smt.mylearningcentre.ui.screen.club.club_list

sealed interface ClubListIntent {
    data object Init : ClubListIntent
    data object Back : ClubListIntent
    data object OpenCreateClub : ClubListIntent
    data class ClubClick(val clubId: String, val clubName: String) : ClubListIntent
}