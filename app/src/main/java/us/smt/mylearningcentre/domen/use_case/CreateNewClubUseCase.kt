package us.smt.mylearningcentre.domen.use_case

import us.smt.mylearningcentre.domen.use_case.helper.club.CreateClubCategories
import us.smt.mylearningcentre.domen.use_case.helper.club.CreateClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubCategoriesList

data class CreateNewClubUseCase(
    val createClubCategories: CreateClubCategories,
    val getClubCategoriesList: GetClubCategoriesList,
    val createClubDetails: CreateClubDetails
)
