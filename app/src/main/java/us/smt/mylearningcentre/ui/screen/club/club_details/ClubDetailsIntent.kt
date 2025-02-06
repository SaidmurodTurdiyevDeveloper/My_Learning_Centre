package us.smt.mylearningcentre.ui.screen.club.club_details

sealed interface ClubDetailsIntent {
    data class Init(val clubId: String) : ClubDetailsIntent
    data object JoinClub : ClubDetailsIntent
    data object Back : ClubDetailsIntent
}