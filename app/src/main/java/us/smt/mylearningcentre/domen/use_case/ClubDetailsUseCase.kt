package us.smt.mylearningcentre.domen.use_case

import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.student.GetAllClubStudent
import us.smt.mylearningcentre.domen.use_case.helper.student.GetStudent

data class ClubDetailsUseCase(
    val getClubDetails: GetClubDetails,
    val getAllClubStudent: GetAllClubStudent,
    val getStudent: GetStudent
)
