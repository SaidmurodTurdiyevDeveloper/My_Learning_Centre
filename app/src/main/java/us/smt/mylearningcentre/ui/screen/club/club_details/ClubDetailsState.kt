package us.smt.mylearningcentre.ui.screen.club.club_details

import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.util.UserError

data class ClubDetailsState(
    val isLoading: Boolean = true,
    val error: UserError? = null,
    val club: ClubDetailsData? = null,
    val clubMembers: List<StudentData> = emptyList(),
    val president: StudentData? = null

)
