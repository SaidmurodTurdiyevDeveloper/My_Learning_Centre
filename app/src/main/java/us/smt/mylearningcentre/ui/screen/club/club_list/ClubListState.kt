package us.smt.mylearningcentre.ui.screen.club.club_list

import us.smt.mylearningcentre.data.model.ClubData
import us.smt.mylearningcentre.util.UserError

data class ClubListState(
    val isLoading: Boolean = true,
    val list: List<ClubData> = emptyList(),
    val error: UserError? = null
)