package us.smt.mylearningcentre.domen.use_case

import us.smt.mylearningcentre.domen.use_case.helper.club.CreateClubCategories
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubCategoriesList
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubList
import us.smt.mylearningcentre.domen.use_case.helper.club.SortClubListWithCategory
import us.smt.mylearningcentre.domen.use_case.helper.student.GetUser

data class ClubListUseCase(
    val getClubList: GetClubList,
    val getClubCategoriesList: GetClubCategoriesList,
    val sortClubListWithCategory: SortClubListWithCategory,
    val getUser: GetUser,
    val createClubCategories: CreateClubCategories
)
